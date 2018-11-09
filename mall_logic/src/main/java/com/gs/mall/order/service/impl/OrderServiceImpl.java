package com.gs.mall.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gs.common.result.ResponseResult;
import com.gs.common.result.StatusCodeConfig;
import com.gs.common.util.DateUtil;
import com.gs.common.util.StringUtils;
import com.gs.mall.commodity.CommodityFactory;
import com.gs.mall.commodity.dao.CommodityDao;
import com.gs.mall.commodity.dao.CommodityDetailDao;
import com.gs.mall.commodity.dao.CommoditySnapshootDao;
import com.gs.mall.commodity.po.Commodity;
import com.gs.mall.commodity.po.CommodityDetail;
import com.gs.mall.commodity.po.CommoditySnapshoot;
import com.gs.mall.common.constant.Constant;
import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.common.dao.LogisticsDao;
import com.gs.mall.common.po.Logistics;
import com.gs.mall.common.service.AbstractBaseService;
import com.gs.mall.enums.*;
import com.gs.mall.finance.FinanceFactory;
import com.gs.mall.finance.dao.TradeDetailDao;
import com.gs.mall.finance.dao.TradeStatementDao;
import com.gs.mall.finance.po.TradeDetail;
import com.gs.mall.finance.po.TradeStatement;
import com.gs.mall.inventory.service.InventoryService;
import com.gs.mall.mq.provider.OrderProvider;
import com.gs.mall.order.OrderFactory;
import com.gs.mall.order.dao.OrderDao;
import com.gs.mall.order.dao.OrderLogDao;
import com.gs.mall.order.dto.OrderDto;
import com.gs.mall.order.po.Order;
import com.gs.mall.order.po.OrderLog;
import com.gs.mall.order.po.OrderPayData;
import com.gs.mall.order.service.OrderPayService;
import com.gs.mall.order.service.OrderService;
import com.gs.mall.order.vo.OrderFinanceDetailVo;
import com.gs.mall.user.dao.UserAddressDao;
import com.gs.mall.user.po.User;
import com.gs.mall.user.po.UserAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;


/**
 * Order service implement
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:12
 */
@Service("orderService")
public class OrderServiceImpl extends AbstractBaseService<Order,java.lang.String> implements OrderService {

    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Resource
    private OrderDao orderDao;

    @Resource
    private OrderLogDao orderLogDao;

    @Resource
    private CommodityDetailDao commodityDetailDao;

    @Resource
    private CommoditySnapshootDao commoditySnapshootDao;

    @Resource
    private UserAddressDao userAddressDao;

     @Resource
    private TradeDetailDao tradeDetailDao;

    @Resource
    private TradeStatementDao tradeStatementDao;

    @Resource
    private CommodityDao commodityDao;

    @Resource
    private LogisticsDao logisticsDao;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private OrderPayService orderPayService;

    @Autowired
    private OrderProvider orderProvider;

    public OrderServiceImpl() {
        //commodityDetailDao = new ThreadLocal<>();
    }

    @Override
    protected BaseDao<Order,java.lang.String> getBaseDao() {
        return orderDao;
    }

	

    @Transactional
    @Override
    public Boolean saveOrUpdate(Order t) {
        int i = 0;
        //Order order = OrderFactory.getOrderInstance();		
        if( t.getOrderId() == null ) {			
				t.setOrderId(UUID.randomUUID().toString().replaceAll("-",""));
            i = orderDao.insert(t); 			
        } else {
            i = orderDao.update(t);
        }
        return (i > 0);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public ResponseResult save(Map<String, String> params) {
        UserAddress userAddress = null;
        try {
            userAddress = JSONObject.parseObject(params.get("buyer"), UserAddress.class);
            userAddress.setCreateTime(new Date());
            userAddress.setOpenId(params.get("openId"));
        } catch (Exception e) {
            logger.error("parse buyer json error:", e);
            return ResponseResult.instance(102005);
        }
        JSONArray array = null;
        try {
            String commodities = params.get("commodities");
            array = JSONArray.parseArray(commodities);
        } catch (Exception e) {
            logger.error("parse commodities json array error:", e);
            return ResponseResult.instance(102003);
        }
        //验证订单总价
        //总价=运费-优惠+SUM(商品单价*数量)
        Integer calculateTotal = getIntegerValue(params.get("freightPrice")) - getIntegerValue(params.get("discountPrice"));
        JSONObject selectedItem = null;
        Map<String,Object> queryParams = new HashMap<>(2);
        CommodityDetail itemDetail = null;
        for ( int i = 0 ; i < array.size() ; ++ i ) {
            selectedItem = array.getJSONObject(i);
            queryParams.put("commodityId",selectedItem.getString("id"));
            queryParams.put("skuId",selectedItem.getString("skuId"));
            itemDetail = commodityDetailDao.queryByParam(queryParams);
            //验证规格，默认规则商品至少有一个
            if(itemDetail==null){
                return ResponseResult.instance(102011);
            }
            //验证价格，价格小于或等于0的商品不允许下单
            if(null==itemDetail.getSalePrice()||itemDetail.getSalePrice()<=0){
                return ResponseResult.instance(102051);
            }
            calculateTotal += getIntegerValue(selectedItem.getString("num"))*itemDetail.getSalePrice();
        }
        if(!calculateTotal.equals(getIntegerValue(params.get("totalPrice"))) || calculateTotal<=0){
            return ResponseResult.instance(102012);
        }
        //先锁定库存
        boolean b = inventoryService.subtract(array);
        if (!b) { //库存锁定失败
            return ResponseResult.instance(102004);
        }
        final JSONArray finalArray = array;
        //创建订单对象
        Order order = OrderFactory.getOrderInstance(params);
        //创建支付订单
        ResponseResult result = orderPayService.createTrade(order);
        if (result.getCode() == 0) {
            try {
                //保存用户信息
                userAddressDao.insert(userAddress);
                params.put("userAddrId", userAddress.getUserAddrId().toString());
                order.setUserAddrId(userAddress.getUserAddrId());
                //商品快照
                List<CommoditySnapshoot> snapshoots = new ArrayList<CommoditySnapshoot>(array.size());
                //商品详情查询参数
                Map<String, Object> param = new HashMap<String, Object>(2);
                int commodityNum = 0, type = 0;
                for (int i = 0, size = array.size(); i < size; ++i) {
                    JSONObject json = array.getJSONObject(i);
                    param.put("commodityId", json.getString("id"));
                    param.put("skuId", json.getString("skuId"));
                    CommodityDetail detail = commodityDetailDao.queryByParam(param);
                    CommoditySnapshoot shoot = CommodityFactory.getSnapshootInstance(detail);
                    shoot.setNum(json.getInteger("num"));
                    shoot.setPrice(json.getInteger("price"));
                    shoot.setOrderId(order.getOrderId());
                    commodityNum += shoot.getNum();
                    if (type == 0) {
                        type = json.getInteger("type");
                    } else if (type != 1) {
                        type = json.getInteger("type");
                    }
                    snapshoots.add(shoot);
                }
                order.setCommodityNum( commodityNum);
                order.setCommodityType( type);
                order.setMerchantId(Constant.merchantId);
                int i = orderDao.insert(order);
                if (i > 0) {
                    final JSONObject json = (JSONObject) result.getData();
                    params.put("trade_no",json.getString("trade_no"));
                    params.put("out_trade_no",json.getString("out_trade_no"));
                    threadPoolTaskExecutor.execute(() -> {
                        try {
                            //添加快照
                            commoditySnapshootDao.batchInsert(snapshoots);
                            //添加日志
                            OrderLog orderLog = OrderFactory.getOrderLogInstance(order);
                            orderLog.setContent(StatusCodeConfig.getValue("102101"));
                            orderLog.setOperator(params.get("userName"));
                            orderLog.setOperatorId(order.getOwnerOpenId());
                            orderLog.setOperatorType(OperatorTypeEnum.USER.getValue());
                            orderLogDao.insert(orderLog);
                            //添加交易流水
                            TradeDetail tradeDetail = FinanceFactory.getTradeDetailInstance(params);
                            tradeDetail.setName("交易订单号-" + order.getOrderId());
                            tradeDetail.setPayerId(order.getOwnerOpenId()); //付款人ID
                            tradeDetail.setPayer(params.get("userName")); //付款人名称
                            tradeDetail.setType(TradeTypeEnum.INCOME.getValue());
                            tradeDetail.setAmount(order.getPayPrice());
                            tradeDetail.setMerchantId(order.getMerchantId());
                            tradeDetail.setTradeStatus("WAIT_BUYER_PAY");
                            tradeDetail.setStatus(TradeStatusEnum.CREATE.getValue());
                            tradeDetail.setPayeeId(Constant.appID);
                            tradeDetail.setPayee(Constant.appName);
                            tradeDetailDao.insert(tradeDetail);
                            //添加交易数据 result
                            //JSONObject json = (JSONObject) result.getData();
                            orderDao.inertOrderPayData(OrderFactory.getOrderPayDataInstance(json));
                            //下单后放入MQ
                            orderProvider.closeOrderProvider(order.getOrderId(), order.getCreateTime());
                        } catch (Exception e1) {
                            logger.error("save extend fail : ", e1);
                        }
                    });
                }
            } catch (Exception e) {
                threadPoolTaskExecutor.execute(() -> {
                    try {
                        inventoryService.increase(finalArray);
                    } catch (Exception e2) {
                    }
                    try {
                        JSONObject json = (JSONObject) result.getData();
                        orderPayService.closeTrade(json.getString("trade_no"), json.getString("out_trade_no"));
                    } catch (Exception e3) {
                    }
                });
                logger.error("create order error:", e);
                throw e;
            }
        } else {
            threadPoolTaskExecutor.execute(() -> {
                try {
                    inventoryService.increase(finalArray);
                } catch (Exception e2) {
                    logger.error("添加库存失败：",e2);
                }
            });
        }
        return result;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public ResponseResult updatePaySuccess(String orderId, Date payTime, User operator) {
        try {
            Order order = orderDao.selectById(orderId);
            if (order == null) {
                return ResponseResult.instance(102007);
            } else if (!order.getStatus().equals(OrderStatusEnum.WAITPAYMENT.getValue())) {
                return ResponseResult.instance(102008);
            }
            Order updateOrder = OrderFactory.getEmptyOrderInstance();
            updateOrder.setOrderId(orderId);
            String logConent = "";
            if (order.getCommodityType().equals(CommodityTypeEnum.PHYSICAL.getValue())) {
                updateOrder.setStatus(OrderStatusEnum.WAITDELIVERY.getValue());
                logConent = StatusCodeConfig.getValue("102102");
            } else {
                updateOrder.setStatus(OrderStatusEnum.SUCCESS.getValue());
                logConent = StatusCodeConfig.getValue("102104");
            }
            updateOrder.setPayStatus(OrderPayStatusEnum.PAYMENTED.getValue());
            updateOrder.setPayTime(payTime == null ? new Date() : payTime);
            int i = orderDao.update(updateOrder);
            if (i > 0) { //添加日志
                String finalConent = logConent;
                threadPoolTaskExecutor.execute(() -> {
                    try {
                        //添加日志
                        OrderLog log = OrderFactory.getOrderLogInstance(updateOrder);
                        log.setContent(finalConent);
                        log.setOperator(operator.getOperator());
                        log.setOperatorId(operator.getOperatorId());
                        log.setOperatorType(OperatorTypeEnum.USER.getValue());
                        orderLogDao.insert(log);
                        //添加交易记录
                        TradeStatement trade = FinanceFactory.getTradeStatementInstance(order);
                        List<CommoditySnapshoot> list = commoditySnapshootDao.queryByOrderId(order.getOrderId());
                        Commodity com = commodityDao.selectById(list.get(0).getCommodityId());
                        trade.setSupplierId(com.getSupplierId());
                        tradeStatementDao.insert(trade);
                        //修改商品最后购买时间、累计销量字段
                        List<CommoditySnapshoot> shoots = commoditySnapshootDao.queryByOrderId(orderId);
                        Long currentCid = shoots.get(0).getCommodityId();
                        Date date = updateOrder.getPayTime();
                        Integer nums = 0;
                        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>(4);
                        if (shoots.size() > 1) { //大于一件商品时
                            shoots.sort((CommoditySnapshoot cs1, CommoditySnapshoot cs2) -> cs1.getCommodityId().compareTo(cs2.getCommodityId()));
                            for (CommoditySnapshoot cs : shoots) {
                                if (!currentCid.equals(cs.getCommodityId())) {
                                    Map<String, Object> map = new HashMap<String, Object>(3);
                                    map.put("id", currentCid);
                                    map.put("lastBuy", date);
                                    map.put("saleNum", nums);
                                    maps.add(map);
                                    currentCid = cs.getCommodityId();
                                    nums = cs.getNum();
                                } else {
                                    nums += cs.getNum();
                                }
                            }
                        }
                        Map<String, Object> map = new HashMap<String, Object>(3);
                        map.put("id", currentCid);
                        map.put("lastBuy", date);
                        map.put("saleNum", nums);
                        maps.add(map);
                        commodityDao.batchUpdateSaleTotalLastBuy(maps);
                    } catch (Exception e) {
                        logger.error(" save tradeStatement fail : ", e);
                    }
                });
            }

            return ResponseResult.successInstance();
        } catch (Exception e) {
            logger.error("update pay success fail : ", e);
            throw e;
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public ResponseResult updateDeliver(String orderId, Date deliverTime, User operator, Logistics logistics) {
        try {
            Order order = orderDao.selectById(orderId);
            if (order == null) {
                return ResponseResult.instance(102007);
            } else if (!order.getStatus().equals(OrderStatusEnum.WAITDELIVERY.getValue())) {
                return ResponseResult.instance(102008);
            }
            //添加物流信息
            //Logistics lgs = CommonFactory.getLogisticsInstance(logisticsCode,logisticsName);
            logistics.setCreateTime(new Date());
            logistics.setUserAddrId(order.getUserAddrId());
            int ia = logisticsDao.insert(logistics);
            if (ia <= 0) {
                return ResponseResult.instance(102009);
            }
            //修改订单信息
            Order updateOrder = OrderFactory.getEmptyOrderInstance();
            updateOrder.setOrderId(orderId);
            updateOrder.setLogisticsId(logistics.getLogisticsId());
            updateOrder.setDeliverTime(deliverTime);
            updateOrder.setStatus(OrderStatusEnum.WAITRECEIPT.getValue());
            int i = orderDao.update(updateOrder);
            if (i > 0) { //添加日志
                OrderLog log = OrderFactory.getOrderLogInstance(updateOrder);
                log.setContent(StatusCodeConfig.getValue("102103"));
                log.setOperator(operator.getOperator());
                log.setOperatorId(operator.getOperatorId());
                log.setOperatorType(OperatorTypeEnum.MECHANT.getValue());
                orderLogDao.insert(log);
                //发货后放入缓存
                orderProvider.finishOrderProvider(order.getOrderId(), order.getDeliverTime());
            }
            return ResponseResult.successInstance();
        } catch (Exception e) {
            logger.error("update deliver fail : ", e);
            throw e;
        }
    }

    @Override
    public ResponseResult updateFinish(String orderId, Date finishTime, User operator) {
        try {
            Order order = orderDao.selectById(orderId);
            if (order == null) {
                return ResponseResult.instance(102007);
            } else if (!order.getStatus().equals(OrderStatusEnum.WAITRECEIPT.getValue())) {
                return ResponseResult.instance(102008);
            }
            //修改订单信息
            /*Order updateOrder = OrderFactory.getEmptyOrderInstance();
            updateOrder.setOrderId(orderId);
            updateOrder.setStatus(OrderStatusEnum.SUCCESS.getValue());
            updateOrder.setFinishTime(finishTime == null ? new Date() : finishTime);
            int i = orderDao.update(updateOrder);*/
            int i = orderDao.updateStatus(orderId, OrderStatusEnum.WAITRECEIPT.getValue(),
                    OrderStatusEnum.SUCCESS.getValue(), finishTime == null ? new Date() : finishTime );
            if (i > 0) { //添加日志
                Order updateOrder = OrderFactory.getEmptyOrderInstance();
                updateOrder.setOrderId(orderId);
                updateOrder.setStatus(OrderStatusEnum.SUCCESS.getValue());
                OrderLog log = OrderFactory.getOrderLogInstance(updateOrder);
                log.setContent(StatusCodeConfig.getValue("102104"));
                log.setOperator(operator.getOperator());
                log.setOperatorId(operator.getOperatorId());
                log.setOperatorType(OperatorTypeEnum.USER.getValue());
                orderLogDao.insert(log);
            }
            return ResponseResult.successInstance();
        } catch (Exception e) {
            logger.error("update finish fail : ", e);
            throw e;
        }
    }

    @Override
    public ResponseResult updateClose(String orderId, User operator) {
        try {
        	List<CommoditySnapshoot> snapshoot = commoditySnapshootDao.queryByOrderId(orderId);
        	Map<String,Object> m = new HashMap<>(snapshoot.size());
        	snapshoot.forEach(s -> {
        		m.put("id", s.getCommodityId());
        		m.put("skuId", s.getSkuId());
        		m.put("num", s.getNum());
        	});
        	JSONArray array = null;
        	if (JSON.toJSONString(m).startsWith("[")) {
        		array = JSON.parseArray(JSON.toJSONString(m));
			}else {
				array = new JSONArray();
				array.add(JSON.parseObject(JSON.toJSONString(m)));
			}
            Order order = orderDao.selectById(orderId);
            if (order == null) {
                return ResponseResult.instance(102007);
                //待付款状态下才能关闭
            } else if (!order.getStatus().equals(OrderStatusEnum.WAITPAYMENT.getValue())) {
                return ResponseResult.instance(102008);
            }
            //修改订单信息
            Order updateOrder = OrderFactory.getEmptyOrderInstance();
            updateOrder.setOrderId(orderId);
            updateOrder.setStatus(OrderStatusEnum.CLOSE.getValue());
            //int i = orderDao.update(updateOrder);
            int i = orderDao.updateStatus(orderId, OrderStatusEnum.WAITPAYMENT.getValue(),
                    OrderStatusEnum.CLOSE.getValue(), null);
            boolean increase = inventoryService.increase(array);
            if (!increase) {
				logger.error("取消订单库存回滚失败");
			}
            if (i > 0) { //添加日志
                OrderLog log = OrderFactory.getOrderLogInstance(updateOrder);
                log.setContent(StatusCodeConfig.getValue("102105"));
                log.setOperator(operator.getOperator());
                log.setOperatorId(operator.getOperatorId());
                log.setOperatorType(OperatorTypeEnum.USER.getValue());
                orderLogDao.insert(log);
            }
            return ResponseResult.successInstance();
        } catch (Exception e) {
            logger.error("update close fail : ", e);
            throw e;
        }
    }

    @Override
    public OrderPayData getPayDataByOrderId(String orderId) {
        return orderDao.queryPayDataByOrderId(orderId);
    }

    @Override
    public OrderDto getOrderDtoByOrderId(String orderId, String openId) {
        return orderDao.queryOrderDtoByOrderId(orderId, openId);
    }

    Integer getIntegerValue(String str){
        if( StringUtils.isEmpty(str) ) {
            return Integer.valueOf(0);
        }
        return Integer.valueOf(str);
    }

    @Override
	public OrderFinanceDetailVo countConsume(String startTime, String endTime) {
		int countConsume = orderDao.countConsume(startTime,endTime);
		Date date = new Date();
		OrderFinanceDetailVo detailVo = new OrderFinanceDetailVo();
		detailVo.setConsumeMoney((long) countConsume);
		detailVo.setFromName("自营商城");
		detailVo.setFromType("6");
		detailVo.setPurMoney(0L);
		detailVo.setReportDay(DateUtil.formatDate(date,"yyyyMMdd"));
		detailVo.setReportTime(DateUtil.formatDate(date,"yyyy-MM-dd HH:mm:ss"));
		logger.info("定时统计销售数据:"+JSON.toJSONString(detailVo));
		return detailVo;
	}
}
