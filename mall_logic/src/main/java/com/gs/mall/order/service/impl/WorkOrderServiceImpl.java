package com.gs.mall.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gs.common.cache.JedisCache;
import com.gs.common.result.ResponsePageResult;
import com.gs.common.result.ResponseResult;
import com.gs.common.result.StatusCodeConfig;
import com.gs.mall.commodity.dao.CommodityDao;
import com.gs.mall.commodity.dao.CommoditySnapshootDao;
import com.gs.mall.commodity.po.Commodity;
import com.gs.mall.commodity.po.CommoditySnapshoot;
import com.gs.mall.common.constant.Constant;
import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.common.dao.LogisticsDao;
import com.gs.mall.common.po.Logistics;
import com.gs.mall.common.service.AbstractBaseService;
import com.gs.mall.enums.*;
import com.gs.mall.finance.FinanceFactory;
import com.gs.mall.finance.dao.TradeDetailDao;
import com.gs.mall.finance.po.TradeDetail;
import com.gs.mall.mq.provider.OrderProvider;
import com.gs.mall.order.OrderFactory;
import com.gs.mall.order.dao.OrderDao;
import com.gs.mall.order.dao.RefundCommodityDao;
import com.gs.mall.order.dao.WorkOrderLogDao;
import com.gs.mall.order.dto.*;
import com.gs.mall.order.po.Order;
import com.gs.mall.order.po.RefundCommodity;
import com.gs.mall.order.po.WorkOrder;
import com.gs.mall.order.dao.WorkOrderDao;
import com.gs.mall.order.po.WorkOrderLog;
import com.gs.mall.order.service.WorkOrderPayService;
import com.gs.mall.order.service.WorkOrderService;

import com.gs.mall.partner.dao.MerchantDao;
import com.gs.mall.partner.po.Merchant;
import com.gs.mall.user.dao.UserAddressDao;
import com.gs.mall.user.po.User;
import com.gs.mall.user.po.UserAddress;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

import static java.util.stream.Collectors.toList;


/**
 * WorkOrder service implement
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:13
 */
@Service("workOrderService")
public class WorkOrderServiceImpl extends AbstractBaseService<WorkOrder,java.lang.String> implements WorkOrderService {

    @Resource
    private WorkOrderDao workOrderDao;

    @Resource
    private OrderDao orderDao;

    @Resource
    private UserAddressDao userAddressDao;

    @Resource
    private CommoditySnapshootDao commoditySnapshootDao;

    @Resource
    private RefundCommodityDao refundCommodityDao;

    @Resource
    private WorkOrderLogDao workOrderLogDao;

    @Resource
    private TradeDetailDao tradeDetailDao;

    @Resource
    private LogisticsDao logisticsDao;

    @Resource
    private MerchantDao merchantDao;

    @Autowired
    private OrderProvider orderProvider;

    @Autowired
    private WorkOrderPayService workOrderPayService;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    
    @Resource
    private CommodityDao commodityDao;

    @Override
    protected BaseDao<WorkOrder,java.lang.String> getBaseDao() {
        return workOrderDao;
    }

	

    @Transactional
    @Override
    public Boolean saveOrUpdate(WorkOrder t) {
        int i = 0;
        //WorkOrder order = WorkOrderFactory.getWorkOrderInstance();		
        if( t.getWoId() == null ) {			
				t.setWoId(UUID.randomUUID().toString().replaceAll("-",""));
            i = workOrderDao.insert(t); 			
        } else {
            i = workOrderDao.update(t);
        }
        return (i > 0);
    }


    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public ResponseResult save(Map<String, Object> params, User operator) {
        int i = 0;
        int verifyResult = verifyHadRefundApply(params);
        if (verifyResult > 0) {
            return ResponseResult.instance(verifyResult);
        }
        JSONArray commodities = JSONArray.parseArray(params.get("commodities").toString());
        WorkOrder workOrder = OrderFactory.getWorkOrderInstance(params);
        Order order = orderDao.selectById(workOrder.getOrderId());
        if (null != order.getUserAddrId()) {
            UserAddress userAddress = userAddressDao.selectById(order.getUserAddrId());
            if (userAddress != null) {
                workOrder.setReceiver(userAddress.getUsername());
                workOrder.setReceiverTel(userAddress.getMobile());
            }
        }
        if (!order.getOwnerOpenId().equals(operator.getOperatorId())) {
            return ResponseResult.instance(103020);
        }
        workOrder.setMerchantId(order.getMerchantId());
        workOrder.setAmount(0);
        workOrder.setStatus(WorkOrderStatusEnum.APPLY.getValue());
        workOrder.setCreateTime(new Date());
        i = workOrderDao.insert(workOrder);
        JSONObject commdity = null;
        CommoditySnapshoot snapshoot = null;
        RefundCommodity workOrderDetail = null;
        List<RefundCommodity> workOrderRefundCommodities = new ArrayList<>();
        for (int j = 0; j < commodities.size(); j++) {
            workOrderDetail = new RefundCommodity();
            commdity = commodities.getJSONObject(j);
            snapshoot = commoditySnapshootDao.selectById(commdity.getLong("snapshootId"));
            workOrderDetail.setWoId(workOrder.getWoId());
            workOrderDetail.setSnapshootId(commdity.getLong("snapshootId"));
            workOrderDetail.setNum(commdity.getInteger("num"));
            workOrderDetail.setCommodityId(snapshoot.getCommodityId());
            workOrderDetail.setSkuId(snapshoot.getSkuId());
            workOrderDetail.setTitle(snapshoot.getTitle());
            workOrderDetail.setAttributes(snapshoot.getAttributes());
            workOrderDetail.setPrice(calculateRefundCommdity(snapshoot, commdity.getInteger("num")));
            workOrderDetail.setCreateTime(new Date());
            workOrderDetail.setImgUri(snapshoot.getImgUri());
            refundCommodityDao.insert(workOrderDetail);
            workOrderRefundCommodities.add(workOrderDetail);
        }
        Integer ammount = calculateShouldRefund(workOrder, workOrderRefundCommodities);
        workOrder.setAmount(ammount);
        workOrderDao.update(workOrder);
        orderRefundUpdate(order);
        tagingWorkOrderLog(operator, i, workOrder, StatusCodeConfig.getValue("103101"),false);
        return i > 0 ? ResponseResult.successInstance() : ResponseResult.failInstance();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public ResponseResult updateAgreeDelivery(String workOrderId, User operator) {
//        int i = 0;
//        WorkOrder workOrder = workOrderDao.selectById(workOrderId);
//        if (workOrder != null) {
//            String logMsg = "";
//            if (WorkOrderStatusEnum.WAITDELIVERY.judge(workOrder.getStatus())) {
//                return ResponseResult.successInstance();
//            }
//            List<RefundCommodity> refundCommodities = refundCommodityDao.queryByWorkOrderId(workOrder.getWoId());
//            if (WorkOrderStatusEnum.APPLY.judge(workOrder.getStatus())
//                    || WorkOrderStatusEnum.WAITDELIVERY.judge(workOrder.getStatus())) {
//                if (WorkOrderTypeEnum.ONLYREFUND.judge(workOrder)
//                        && !WorkOrderStatusEnum.SUCCESS.judge(workOrder.getStatus())) {
//                    workOrder.setAmount(workOrder.getAmount() == null || 0 == workOrder.getAmount() ? calculateShouldRefund(workOrder, refundCommodities) : workOrder.getAmount());
//                    ResponseResult payBakResult = refundPayback(workOrder, workOrder.getAmount(), operator);
//                    if (0 == payBakResult.getCode()) {
//                        workOrder.setStatus(WorkOrderStatusEnum.SUCCESS.getValue());
//                        logMsg = StatusCodeConfig.getValue("103105");
//                        Order order = orderDao.selectById(workOrder.getOrderId());
//                        orderRefundUpdate(order, true);
//                    } else {
//                        return payBakResult;
//                    }
//
//                } else {
//                    workOrder.setStatus(WorkOrderStatusEnum.WAITDELIVERY.getValue());
//                    logMsg = StatusCodeConfig.getValue("103102");
//                }
//                i = workOrderDao.update(workOrder);
//                tagingWorkOrderLog(operator, i, workOrder, logMsg,false);
//            }
//        } else {
//            return ResponseResult.instance(103012);
//        }
//        return i > 0 ? ResponseResult.successInstance() : ResponseResult.failInstance();
    	int i = 0;
        WorkOrder workOrder = workOrderDao.selectById(workOrderId);
        Order order = orderDao.selectById(workOrder.getOrderId());
        if (workOrder != null) {
            String logMsg = "";
            if (WorkOrderStatusEnum.WAITDELIVERY.judge(workOrder.getStatus())) {
                return ResponseResult.successInstance();
            }
            List<RefundCommodity> refundCommodities = refundCommodityDao.queryByWorkOrderId(workOrder.getWoId());
            if (WorkOrderStatusEnum.APPLY.judge(workOrder.getStatus())
                    || WorkOrderStatusEnum.WAITDELIVERY.judge(workOrder.getStatus())) {
                if (WorkOrderTypeEnum.ONLYREFUND.judge(workOrder)
                        && !WorkOrderStatusEnum.SUCCESS.judge(workOrder.getStatus())) {
                    workOrder.setAmount(workOrder.getAmount() == null || 0 == workOrder.getAmount() ? calculateShouldRefund(workOrder, refundCommodities) : workOrder.getAmount());
                    workOrder.setStatus(WorkOrderStatusEnum.WAITREFUND.getValue());
                    logMsg = "退货申请已经同意，等待退款处理";
                } else {
                	workOrder.setStatus(WorkOrderStatusEnum.WAITDELIVERY.getValue());
                	logMsg = "退货申请已经同意，请寄回商品";
                	if (isVirtual(workOrder.getWoId()) || order.getCommodityType() != 1) {
                		workOrder.setAmount(workOrder.getAmount() == null || 0 == workOrder.getAmount() ? calculateShouldRefund(workOrder, refundCommodities) : workOrder.getAmount());
                        workOrder.setStatus(WorkOrderStatusEnum.WAITREFUND.getValue());
                        logMsg = "退货申请已经同意，等待退款处理";
					}
                }
                i = workOrderDao.update(workOrder);
                tagingWorkOrderLog(operator, i, workOrder, logMsg,false);
            }
        } else {
            return ResponseResult.instance(103012);
        }
        return i > 0 ? ResponseResult.successInstance() : ResponseResult.failInstance();
    }
    /**
     * 判断是否虚拟商品
     * @return
     */
    private boolean isVirtual(String wOrderId) {
    	Map<String, Object> params = new HashMap<>();
    	Boolean re = Boolean.FALSE;
    	params.put("woId", wOrderId);
		List<RefundCommodity> data = refundCommodityDao.selectByParam(params, 0, 10000);
		if (data != null &&!data.isEmpty()) {
			for (RefundCommodity d : data) {
				if (re) 
                {return re;}
				Commodity c = commodityDao.selectById(d.getCommodityId());
				re = c.getType() != 1 ? true:false;
			}
		}
    	return re;
    }


    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public ResponseResult updateDelivery(String workOrderId, Logistics logistics, User operator) {
        int i = 0;
        WorkOrder workOrder = workOrderDao.selectById(workOrderId);
        if (workOrder != null) {
            String logMsg = "";
            if (logistics.getLogisticsId() == null) {
                logisticsDao.insert(logistics);
                logMsg = StatusCodeConfig.getValue("103103");
            } else {
                logisticsDao.update(logistics);
                logMsg = StatusCodeConfig.getValue("103107");
            }
            workOrder.setLogisticsId(logistics.getLogisticsId());
            if (WorkOrderStatusEnum.WAITDELIVERY.judge(workOrder.getStatus())) {
                workOrder.setStatus(WorkOrderStatusEnum.WAITREFUND.getValue());
            }
            i = workOrderDao.update(workOrder);
            tagingWorkOrderLog(operator, i, workOrder, logMsg + logistics.getName() + "  " + logistics.getCode(),true);
        } else {
            return ResponseResult.instance(103012);
        }

        return i > 0 ? ResponseResult.successInstance() : ResponseResult.failInstance();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public ResponseResult updateFinish(String workOrderId, Integer amount, User operator) {
        int i = 0;
        WorkOrder workOrder = workOrderDao.selectById(workOrderId);
        Order order = orderDao.selectById(workOrder.getOrderId());
        if (workOrder != null) {
            if (WorkOrderStatusEnum.SUCCESS.judge(workOrder.getStatus())) {
                return ResponseResult.successInstance();
            }
//            Integer calculateRefund = calculateShouldRefund(workOrder, refundCommodityDao.queryByWorkOrderId(workOrderId));
            Integer calculateRefund = workOrder.getAmount()+1500+order.getFreightPrice();
            Integer calculateMoreRefund = calculateShouldMoreRefund(workOrder, refundCommodityDao.queryByWorkOrderId(workOrderId));
            if (amount > calculateRefund) {
                return ResponseResult.instance(103022);
            }
            if ( amount > calculateMoreRefund) {
            	workOrder.setIsFreight(1);
            }
            //验证关联的已退工单总额
            Map<String, Object> params = new HashMap<>(2);
            params.put("orderId", order.getOrderId());
            params.put("workOrderId", workOrder.getWoId());
            List<WorkOrder> linksWorkOrder = workOrderDao.queryLink(params);
            int refundedCount = 0;
            if (linksWorkOrder != null) {
                //已完成退款
                refundedCount = linksWorkOrder.stream().filter(wo -> filterDone(wo)).mapToInt(WorkOrder::getAmount).sum();
            }
            refundedCount += amount;
            if (refundedCount > order.getPayPrice()) {
                return ResponseResult.instance(103021);
            }
            ResponseResult payBakResult = refundPayback(workOrder, amount, operator);
            if (0 == payBakResult.getCode()) {
                workOrder.setAmount(amount);
                workOrder.setStatus(WorkOrderStatusEnum.SUCCESS.getValue());
                i = workOrderDao.update(workOrder);
                String logMsg = WorkOrderTypeEnum.ONLYREFUND.judge(workOrder) ? StatusCodeConfig.getValue("103105") : StatusCodeConfig.getValue("103104");
                tagingWorkOrderLog(operator, i, workOrder, logMsg,false);
                orderRefundUpdate(order, true);
            } else {
                return payBakResult;
            }
        } else {
            return ResponseResult.instance(103012);
        }
        
        return i > 0 ? ResponseResult.successInstance() : ResponseResult.failInstance();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public ResponseResult updateClose(String workOrderId, String reason, User operator) {
        int i = 0;
        String logMsg = StatusCodeConfig.getValue("103106");
        WorkOrder workOrder = workOrderDao.selectById(workOrderId);
        if (workOrder != null) {
            if (WorkOrderStatusEnum.CLOSE.judge(workOrder.getStatus())) {
                return ResponseResult.successInstance();
            }
            workOrder.setCloseReason(reason);
            workOrder.setStatus(WorkOrderStatusEnum.CLOSE.getValue());
            i = workOrderDao.update(workOrder);
            tagingWorkOrderLog(operator, i, workOrder, logMsg + reason,false);

            orderRefundUpdate(orderDao.selectById(workOrder.getOrderId()));
        } else {
            return ResponseResult.instance(103012);
        }
        return i > 0 ? ResponseResult.successInstance() : ResponseResult.failInstance();
    }



    @Override
    public ResponseResult sendBackAddr(String workOrderId) {
        Map<String, String> data = new HashMap<>(3);
        WorkOrder workOrder = workOrderDao.selectById(workOrderId);
        Merchant merchant = merchantDao.selectById(workOrder.getMerchantId());
        data.put("name", merchant.getName());
        data.put("tel", merchant.getContactPhone());
        data.put("address", merchant.getAddress());
        ResponseResult result = ResponseResult.successInstance();
        result.setData(data);
        return result;
    }

    @Override
    public ResponseResult countTodoNums() {
        Map<String, Object> queryRslt = new HashMap<>();
        queryRslt.put("status", WorkOrderStatusEnum.APPLY.getValue());
        int todoNums=0;
        Object cacheToDoNums  = JedisCache.getRedisObject(Constant.GS_MALL_WORKORDER_NUM);
        if(cacheToDoNums==null){
            todoNums = workOrderDao.selectCountByParam(queryRslt);
            JedisCache.setRedisObjectExpired(Constant.GS_MALL_WORKORDER_NUM,todoNums,20);
        }else{
            todoNums = Integer.valueOf(cacheToDoNums.toString());
        }

        queryRslt.put("status",OrderStatusEnum.WAITDELIVERY.getValue());
        queryRslt.put("commodityType",1);
        int deliveryNums = 0;
        Object cacheDeliveryNums = JedisCache.getRedisObject(Constant.GS_MALL_WAITDELIVERY_NUM);
        if(cacheDeliveryNums==null){
            deliveryNums = orderDao.selectCountByParam(queryRslt);
            JedisCache.setRedisObjectExpired(Constant.GS_MALL_WAITDELIVERY_NUM,deliveryNums,20);
        }else{
            deliveryNums = Integer.valueOf(cacheDeliveryNums.toString());
        }

        ResponseResult result = ResponseResult.successInstance();
        queryRslt.remove("status");
        queryRslt.remove("commodityType");
        if (todoNums > 9) {
            queryRslt.put("workNums", "9+");
        } else {
            queryRslt.put("workNums", todoNums);
        }
        if(deliveryNums>9){
            queryRslt.put("orderNums", "9+");
        } else {
            queryRslt.put("orderNums", deliveryNums);
        }
        result.setData(queryRslt);
        return result;
    }

    @Override
    public WorkOrderCommodityDto getWorkOrderSnapshoot(WorkOrder workOrder) {
        WorkOrderCommodityDto commodityDto = new WorkOrderCommodityDto();
        BeanUtils.copyProperties(workOrder, commodityDto);
        commodityDto.setRefundCommodities(refundCommodityDao.queryByWorkOrderId(workOrder.getWoId()));
        Order order = orderDao.selectById(workOrder.getOrderId());
        OrderWorkOrderDto orderDto = new OrderWorkOrderDto();
        BeanUtils.copyProperties(order, orderDto);
        commodityDto.setOrder(orderDto);
        commodityDto.setUserInfo(userAddressDao.selectById(order.getUserAddrId()));
        if (null != workOrder.getLogisticsId()) {
            commodityDto.setLogistics(logisticsDao.selectById(workOrder.getLogisticsId()));
        }
        return commodityDto;
    }

    @Override
    public WorkOrderDetailDto getWorkOrderDetail(WorkOrder workOrder) {
        WorkOrderDetailDto detailDto = new WorkOrderDetailDto();
        BeanUtils.copyProperties(workOrder, detailDto);
        detailDto.setType(workOrder.getType());
        Map<String, Object> params = new HashMap<>(2);
        params.put("orderId", workOrder.getOrderId());
        params.put("workOrderId", workOrder.getWoId());
        List<WorkOrder> linksWorkOrders = workOrderDao.queryLink(params);
        List<WorkOrder> linksDoneWorkOrder = linksWorkOrders.stream().filter(wo -> filterDone(wo)).collect(toList());
        List<RefundCommodity> linksDoneRefundCommodities = getAllRefundCommodities(linksDoneWorkOrder);
        List<WorkOrder> linksCheckingWorkOrder = linksWorkOrders.stream().filter(wo -> filterChecking(wo)).collect(toList());
        List<RefundCommodity> linksCheckingRefundCommodities = getAllRefundCommodities(linksCheckingWorkOrder);
        Order order = orderDao.selectById(workOrder.getOrderId());
        OrderDto orderDbDto = orderDao.queryOrderDtoByOrderId(order.getOrderId(), order.getOwnerOpenId());
        OrderWorkOrderDto orderDto = new OrderWorkOrderDto();
        BeanUtils.copyProperties(orderDbDto, orderDto);
        List<CommoditySnapshoot> commoditySnapshoots = commoditySnapshootDao.queryByOrderId(order.getOrderId());
        for (CommoditySnapshoot snapshoot : commoditySnapshoots) {
            for (Map<String, Object> commodity : orderDbDto.getCommodities()) {
                if (snapshoot.getSnapshootId().equals(Long.valueOf(commodity.get("snapshootId").toString()))) {
                    //计算已退数
                    int refunded = 0;
                    refunded = linksDoneRefundCommodities.stream().filter(rc -> rc.getSnapshootId().equals(snapshoot.getSnapshootId())).mapToInt(RefundCommodity::getNum).sum();
                    //计算处理中
                    int process = 0;
                    process = linksCheckingRefundCommodities.stream().filter(rc -> rc.getSnapshootId().equals(snapshoot.getSnapshootId())).mapToInt(RefundCommodity::getNum).sum();
                    commodity.put("refunded", refunded);
                    commodity.put("process", process);
                }
            }
        }
        detailDto.setOrder(orderDto);
        detailDto.setUserInfo(userAddressDao.selectById(order.getUserAddrId()));
        detailDto.setRefundCommodities(refundCommodityDao.queryByWorkOrderId(workOrder.getWoId()));

        if (null != linksWorkOrders) {
            detailDto.setLinksWorkOrder(linksWorkOrders.stream().map(wo -> new WorkOrderSimpleDto(wo)).collect(toList()));
        }
        if (null != workOrder.getLogisticsId()) {
            detailDto.setLogistics(logisticsDao.selectById(workOrder.getLogisticsId()));
        }
        return detailDto;
    }

    @Override
    public ResponseResult workOrderLogs(String workOrderId) {
        ResponseResult result = ResponseResult.successInstance();
        WorkOrder workOrder = workOrderDao.selectById(workOrderId);
        Map<String, Object> data = new HashMap<>(2);
        data.put("reason", workOrder.getComment());
        data.put("records", workOrderLogDao.queryByWorkId(workOrderId));
        result.setData(data);
        return result;
    }

    @Override
    public List<RefundCommodity> getRefundCommodities(String workOrderId) {
        return refundCommodityDao.queryByWorkOrderId(workOrderId);
    }


    /**
     * 验证申请退款
     *
     * @param params
     * @return
     */
    private int verifyHadRefundApply(Map<String, Object> params) {

        Order order = orderDao.selectById(params.get("orderId").toString());
        if (order != null) {
            if (!OrderPayStatusEnum.PAYMENTED.getValue().equals(Integer.valueOf(order.getPayStatus()))) {
                return 103006;
            }
            if (System.currentTimeMillis() - order.getCreateTime().getTime() > orderProvider.getFinishMills()) {
                return 103023;
            }
            //仅已付款未发货订单可只退款不退货
            if(WorkOrderTypeEnum.ONLYREFUND.getValue().equals(Short.valueOf(params.get("type").toString()))&&
                    !OrderStatusEnum.WAITDELIVERY.getValue().equals(order.getStatus())){
                return 103025;
            }
            //仅已收货订单可退款退货
//            if(WorkOrderTypeEnum.REFUNDANDGOODS.getValue().equals(Short.valueOf(params.get("type").toString()))&&
//                    !OrderStatusEnum.SUCCESS.getValue().equals(order.getStatus())){
//                return 103026;
//            }
        } else {
            return 103007;
        }
        JSONArray submitRefundCommodities = null;
        try {
            submitRefundCommodities = JSONArray.parseArray(params.get("commodities").toString());
        } catch (Exception e) {
            return 103008;
        }
        List<CommoditySnapshoot> snapshoots = commoditySnapshootDao.queryByOrderId(order.getOrderId());
        Map<String, Object> simpleParams = new HashMap<>(1);
        simpleParams.put("orderId", params.get("orderId"));
        List<WorkOrder> linkWorkOrders = workOrderDao.query(simpleParams);
        linkWorkOrders = linkWorkOrders.stream().filter(wo -> filterCheckingAndDone(wo)).collect(toList());
        List<RefundCommodity> allOrderRefundCommodities = new ArrayList<>();
        if (linkWorkOrders != null) {
            for (WorkOrder workOrder : linkWorkOrders) {
                allOrderRefundCommodities.addAll(refundCommodityDao.queryByWorkOrderId(workOrder.getWoId()));
            }
            int fullCount=0;
            //加上此次提交的退款商品
            for (int i = 0; i < submitRefundCommodities.size(); i++) {
                JSONObject submitRefundCommodity = submitRefundCommodities.getJSONObject(i);
                Integer subNum = submitRefundCommodity.getInteger("num");
                Long snapshootId = submitRefundCommodity.getLongValue("snapshootId");
                //提交退款商品的数量不能小于0
                if (subNum < 0) {
                    return 103024;
                }
                List<CommoditySnapshoot> shoot = snapshoots.stream().filter(snapshoot->snapshoot.getSnapshootId().equals(snapshootId)).collect(toList());
                if(null==shoot||shoot.isEmpty()){
                    return 103027;
                }
                if(shoot.get(0).getNum().equals(subNum)){
                    fullCount++;
                }
                allOrderRefundCommodities.add(new RefundCommodity(snapshootId, subNum));
            }
            if(WorkOrderTypeEnum.ONLYREFUND.getValue().equals(Short.valueOf(params.get("type").toString()))
                    &&fullCount!=snapshoots.size()){
                return 103028;
            }
            for (CommoditySnapshoot orderSnapshoot : snapshoots) {
                Integer snapTotalNums = allOrderRefundCommodities.stream()
                        .filter(workOrderDetail -> workOrderDetail.getSnapshootId().equals(orderSnapshoot.getSnapshootId()))
                        .mapToInt(workOrderDetail -> workOrderDetail.getNum()).sum();
                if (orderSnapshoot.getNum().intValue() < snapTotalNums) {
                    return 103009;
                }
            }
        }
        return 0;
    }


    /**
     * 计算退款商品在此次工单中的退款金额
     *
     * @param snapshoot
     * @param num
     * @return
     */
    private Integer calculateRefundCommdity(CommoditySnapshoot snapshoot, Integer num) {
        if (snapshoot.getNum().equals(num)) {
            return snapshoot.getPrice();
        } else {
            BigDecimal snapshootPrice = new BigDecimal(snapshoot.getPrice());
            BigDecimal snapshootNum = new BigDecimal(snapshoot.getNum());
            return snapshootPrice.divide(snapshootNum,0,BigDecimal.ROUND_DOWN).intValue()*num;
        }
    }

    /**
     * Method:计算默认应退金额
     * Created by chenchuan
     */
    private Integer calculateShouldRefund(WorkOrder workOrder, List<RefundCommodity> workOrderCommodities) {
        Order order = orderDao.selectById(workOrder.getOrderId());
        List<CommoditySnapshoot> snapshoots = commoditySnapshootDao.queryByOrderId(order.getOrderId());
        Integer snapshootsTotal = snapshoots.stream().mapToInt(CommoditySnapshoot::getPrice).sum();
        BigDecimal refundCommodityPay = new BigDecimal(0);
        BigDecimal realPay = new BigDecimal(order.getPayPrice() - order.getFreightPrice());
        BigDecimal workOrderCommodityRefund;
        BigDecimal numRate;
        BigDecimal snapshootPriceRate;
        for (RefundCommodity refundCommdity : workOrderCommodities) {
            CommoditySnapshoot refundSnapshoot = snapshoots.stream()
                    .filter((snapshoot) -> snapshoot.getSnapshootId().equals(refundCommdity.getSnapshootId())).collect(toList()).get(0);
            numRate = new BigDecimal(refundCommdity.getNum()).divide(new BigDecimal(refundSnapshoot.getNum()), 2, BigDecimal.ROUND_HALF_UP);
            snapshootPriceRate = new BigDecimal(refundSnapshoot.getPrice()).divide(new BigDecimal(snapshootsTotal), 2, BigDecimal.ROUND_HALF_UP);
            workOrderCommodityRefund = realPay.multiply(numRate).multiply(snapshootPriceRate);
//            refundCommodityPay = refundCommodityPay.add(workOrderCommodityRefund.setScale(0, BigDecimal.ROUND_HALF_UP));
            refundCommodityPay = refundCommodityPay.add(new BigDecimal(refundSnapshoot.getPrice()).divide(
            		new BigDecimal(refundSnapshoot.getNum())).multiply(new BigDecimal(refundCommdity.getNum())));
        }
//        int freight = validateNeedRefundFreight(order, snapshoots, workOrder, workOrderCommodities);
        int freight = 0;
        workOrder.setIsFreight(0);
        if (freight > 0) {
            workOrder.setIsFreight(1);
        }
        return refundCommodityPay.intValue() + freight;
    }
    /**
     * Method:计算默认应退金额是否大于商品价格
     */
    private Integer calculateShouldMoreRefund(WorkOrder workOrder, List<RefundCommodity> workOrderCommodities) {
    	Order order = orderDao.selectById(workOrder.getOrderId());
    	List<CommoditySnapshoot> snapshoots = commoditySnapshootDao.queryByOrderId(order.getOrderId());
    	BigDecimal refundCommodityPay = new BigDecimal(0);
    	for (RefundCommodity refundCommdity : workOrderCommodities) {
    		CommoditySnapshoot refundSnapshoot = snapshoots.stream()
    				.filter((snapshoot) -> snapshoot.getSnapshootId().equals(refundCommdity.getSnapshootId())).collect(toList()).get(0);
    		BigDecimal refundCommdityTotalPrice = 
    				new BigDecimal(refundSnapshoot.getPrice()).divide(
    	            		new BigDecimal(refundSnapshoot.getNum())).multiply(new BigDecimal(refundCommdity.getNum()));
    		refundCommodityPay = refundCommodityPay.add(refundCommdityTotalPrice);
    	}
    	return refundCommodityPay.intValue();
    }

    /**
     * 计算是否需要退运费
     * 当订单所退各项商品数等于订单购买各项商品数，则需要退回运费
     *
     * @param order
     * @param snapshoots
     * @param workOrder
     * @return
     */
    private int validateNeedRefundFreight(Order order, List<CommoditySnapshoot> snapshoots, WorkOrder workOrder, List<RefundCommodity> workOrderRefundCommodities) {
        //退货退款的不退运费
//        if(WorkOrderTypeEnum.REFUNDANDGOODS.judge(workOrder)){
//            return 0;
//        }
        boolean isTotalCommodities = true;
        Map<String, Object> params = new HashMap<>(2);
        params.put("orderId", order.getOrderId());
        params.put("workOrderId", workOrder.getWoId());
        //查询订单对应的除本次以外的工单
        List<WorkOrder> orderLinkWorkOrders = workOrderDao.queryLink(params);
        if (orderLinkWorkOrders != null) {
            orderLinkWorkOrders = orderLinkWorkOrders.stream().filter(workOrder1 -> filterCheckingAndDone(workOrder1)).collect(toList());
        } else {
            orderLinkWorkOrders = new ArrayList<>();
        }
        List<RefundCommodity> allOrderRefundCommodities = new ArrayList<>();
        for (WorkOrder wo : orderLinkWorkOrders) {
            allOrderRefundCommodities.addAll(refundCommodityDao.queryByWorkOrderId(wo.getWoId()));
        }
        //加入本次工单，不需要验证进行中或完成的状态
        orderLinkWorkOrders.add(workOrder);
        //加入本次退款商品
        allOrderRefundCommodities.addAll(workOrderRefundCommodities);
        //所有关联订单的工单中存在已计算退款运费，则本次不计算运费
        for (WorkOrder linkWorkOrder : orderLinkWorkOrders) {
            if (1 == linkWorkOrder.getIsFreight()&&!linkWorkOrder.getWoId().equalsIgnoreCase(workOrder.getWoId())) {
                isTotalCommodities = false;
                break;
            }
        }
        //若所有工单未计算运费，则需遍历工单，计算总的各个商品退货数是否等于订单购买数
        if (isTotalCommodities) {
            for (CommoditySnapshoot orderCommodity : snapshoots) {
                Integer snapTotalNums = allOrderRefundCommodities.stream()
                        .filter(workOrderDetail -> workOrderDetail.getSnapshootId().equals(orderCommodity.getSnapshootId()))
                        .mapToInt(RefundCommodity::getNum).sum();
                if (!orderCommodity.getNum().equals(snapTotalNums)) {
                    isTotalCommodities = false;
                    break;
                }
            }
        }
        return isTotalCommodities ? order.getFreightPrice() : 0;
    }


    /**
     * 更新订单退货信息
     *
     * @param order
     */
    private void orderRefundUpdate(Order order) {
        orderRefundUpdate(order, false);
    }

    /**
     * 更新订单退货及订单状态
     *
     * @param order
     */
    private void orderRefundUpdate(Order order, boolean finish) {
        Map<String, Integer> refundDetail = new HashMap<>();
        Map<String, Object> simpleParams = new HashMap<>(1);
        simpleParams.put("orderId", order.getOrderId());
        List<WorkOrder> linkWorkOrders = workOrderDao.query(simpleParams);
        if (linkWorkOrders != null && !linkWorkOrders.isEmpty()) {
            linkWorkOrders = linkWorkOrders.stream().filter(workOrder -> !WorkOrderStatusEnum.CLOSE.judge(workOrder.getStatus())).collect(toList());
            for (WorkOrder workOrder : linkWorkOrders) {
                List<RefundCommodity> details = refundCommodityDao.queryByWorkOrderId(workOrder.getWoId());
                details.forEach(detail -> {
                    Integer nums = refundDetail.get(detail.getSnapshootId() + "");
                    if (null == nums) {
                        nums = 0;
                    }
                    nums += detail.getNum();
                    refundDetail.put(detail.getSnapshootId() + "", nums);
                });
            }
            String refundDetailStr = refundStr(refundDetail);
            List<CommoditySnapshoot> snapshoots = commoditySnapshootDao.queryByOrderId(order.getOrderId());
            Integer refundStatus = 0;
            int allfinish = 0;
            for (CommoditySnapshoot snapshoot : snapshoots) {
                Integer num = refundDetail.get(snapshoot.getSnapshootId() + "");
                if (snapshoot.getNum().equals(num)) {
                    allfinish++;
                    continue;
                }
                if (null != num && num != 0 && num < snapshoot.getNum()) {
                    refundStatus = 1;
                    break;
                }
            }
            Order updOrder = new Order();
            if (allfinish == snapshoots.size()) {
                refundStatus = 2;
                if (finish) {
                    updOrder.setStatus(OrderStatusEnum.SUCCESS.getValue());
                }
            }
            updOrder.setOrderId(order.getOrderId());
            updOrder.setRefundDetail(refundDetailStr);
            updOrder.setRefundStatus(refundStatus);
            orderDao.update(updOrder);
        }

    }

    private String refundStr(Map<String, Integer> refundMap) {
        String refundStr = "";
        if (refundMap != null && !refundMap.isEmpty()) {
            StringBuilder str = new StringBuilder();
            refundMap.entrySet().forEach(entry -> {
                str.append(entry.getKey());
                str.append("-");
                str.append(entry.getValue());
                str.append(",");
            });
            refundStr = str.toString().substring(0, str.toString().length() - 1);
        }
        return refundStr;
    }

    /**
     * Method:记录工单操作日志
     * Created by chenchuan
     */
    private void tagingWorkOrderLog(User operator, int i, WorkOrder workOrder, String logMsg, boolean isLogistics) {
        if (i > 0) {
            WorkOrderLog log = OrderFactory.getWorkOrderLogInstance(workOrder);
            log.setContent(logMsg);
            log.setStatus(workOrder.getStatus());
            log.setOperator(operator.getOperator());
            log.setOperatorId(operator.getOperatorId());
            log.setOperatorType(OperatorTypeEnum.USER.getValue());
            if (isLogistics) {
                log.setLogisticsId(workOrder.getLogisticsId());
            }
            workOrderLogDao.insert(log);
        }
    }


    /**
     * 过滤出关联工单里面进行中和已完成退款的工单
     *
     * @param workOrder
     * @return
     */
    private boolean filterCheckingAndDone(WorkOrder workOrder) {
        return WorkOrderStatusEnum.APPLY.judge(workOrder.getStatus())
                || WorkOrderStatusEnum.WAITDELIVERY.judge(workOrder.getStatus())
                || WorkOrderStatusEnum.WAITREFUND.judge(workOrder.getStatus())
                || WorkOrderStatusEnum.SUCCESS.judge(workOrder.getStatus());
    }

    /**
     * 过滤出关联工单里面进行中的工单
     *
     * @param workOrder
     * @return
     */
    private boolean filterChecking(WorkOrder workOrder) {
        return WorkOrderStatusEnum.APPLY.judge(workOrder.getStatus())
                || WorkOrderStatusEnum.WAITDELIVERY.judge(workOrder.getStatus())
                || WorkOrderStatusEnum.WAITREFUND.judge(workOrder.getStatus());
    }

    /**
     * 退款支付
     *
     * @param workOrder
     * @param operator
     * @return
     */
    private ResponseResult refundPayback(WorkOrder workOrder, Integer amount, User operator) {
        workOrder.setAmount(amount);
        ResponseResult payBakResult = workOrderPayService.refundTrade(workOrder, operator);
        if (0 == payBakResult.getCode()) {
            JSONObject bakInfo = JSON.parseObject(payBakResult.getData().toString());
            threadPoolTaskExecutor.execute(() -> {
                TradeDetail tradeDetail = FinanceFactory.getTradeDetailFromWorkOrder(workOrder);
                tradeDetail.setTradeNo(bakInfo.getString("trade_no"));
                tradeDetail.setName("退款订单号-" + workOrder.getOrderId());
                tradeDetail.setPayerId(Constant.appID); //付款人ID
                tradeDetail.setPayer(Constant.appName); //付款人名称
                tradeDetail.setType(TradeTypeEnum.REFUND.getValue());
                tradeDetail.setAmount(-1 * amount);
                tradeDetail.setMerchantId(workOrder.getMerchantId());
                tradeDetail.setTradeStatus("TRADE_SUCCESS");
                tradeDetail.setStatus(TradeStatusEnum.SUCCESS.getValue());
                tradeDetail.setPayeeId(bakInfo.getString("open_id"));//收款人ID
                tradeDetail.setPayee(bakInfo.getString("buyer_logon_id"));//收款人名称
                tradeDetailDao.insert(tradeDetail);
            });
            return ResponseResult.successInstance();
        } else {
            return payBakResult;
        }
    }

    /**
     * 过滤出关联工单里面已完成退款的工单
     *
     * @param workOrder
     * @return
     */
    private boolean filterDone(WorkOrder workOrder) {
        return WorkOrderStatusEnum.SUCCESS.judge(workOrder.getStatus());
    }

    private List<RefundCommodity> getAllRefundCommodities(List<WorkOrder> linksDoneWorkOrder) {
        List<RefundCommodity> refundCommodities = new ArrayList<>();
        if (null != linksDoneWorkOrder) {
            for (WorkOrder wo : linksDoneWorkOrder) {
                refundCommodities.addAll(getRefundCommodities(wo.getWoId()));
            }
        }
        return refundCommodities;
    }



	@Override
	public ResponsePageResult selectBypa(Map<String, Object> params, Integer pageNo, Integer pageSize) {
		int startIndex = 0;
        int fetchSize = 0;
        if ( pageNo != null && pageSize != null ) {
            startIndex = (pageNo-1) * pageSize;
            fetchSize = pageSize;
        }
        ResponsePageResult rp = ResponsePageResult.empty(pageNo==null?0:pageNo, pageSize==null?0:pageSize);
        int i = workOrderDao.selectCountByPa(params);
        rp.setTotalCount(i);
        if( i > 0 ) {
            rp.setData(workOrderDao.selectByPa(params,startIndex,fetchSize));
        }
        return rp;
	}

}
