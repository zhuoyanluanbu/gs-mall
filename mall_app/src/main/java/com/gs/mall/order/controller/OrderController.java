package com.gs.mall.order.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gs.common.cache.JedisCache;
import com.gs.common.openapi.model.UserInfo;
import com.gs.common.result.ResponsePageResult;
import com.gs.common.result.ResponseResult;
import com.gs.common.util.IdentityUtil;
import com.gs.common.util.StringUtils;
import com.gs.mall.base.controller.BaseController;
import com.gs.mall.commodity.dto.CommoditySkuDto;
import com.gs.mall.commodity.service.CommodityService;
import com.gs.mall.common.constant.Constant;
import com.gs.mall.common.util.PageUtil;
import com.gs.mall.enums.OrderPayStatusEnum;
import com.gs.mall.enums.TradeStatusEnum;
import com.gs.mall.enums.TradeTypeEnum;
import com.gs.mall.finance.FinanceFactory;
import com.gs.mall.finance.po.TradeDetail;
import com.gs.mall.finance.service.TradeDetailService;
import com.gs.mall.order.dto.OrderDto;
import com.gs.mall.order.po.OrderPayData;
import com.gs.mall.order.service.OrderPayService;
import com.gs.mall.order.service.OrderService;
import com.gs.mall.order.service.ShoppingCartService;
import com.gs.mall.user.po.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * app订单接口
 * Created by huangyp on 2017/8/21.
 */
@RestController("appOrderController")
@RequestMapping("/app/order")
public class OrderController extends BaseController {

    private static final Logger logger = Logger.getLogger(OrderController.class);

    @Value("${mall.freight.limit}")
    private int freight_limit;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private OrderPayService orderPayService;

    @Autowired
    private TradeDetailService tradeDetailService;
 
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 获得订单提交token
     *
     * @return
     */
    @RequestMapping(value = "/getSumitToken", method = RequestMethod.POST)
    public ResponseResult getOrderSumitToken() {
        String token = IdentityUtil.getUUID();
        JedisCache.setRedisObjectExpired(token, "1", Constant.APP_USER_CACHE_EXPIRE);
        return ResponseResult.successInstance().setData(token);
    }

    /**
     * 确认订单
     * openId 用户OPENID,
     * commodities:[{id:商品ID,skuId:商品SKUID,num:商品数量),type:商品类别},
     * {id:商品ID2,skuId:商品SKUID2,num:商品数量2),type:商品类别}]
     *
     * @return
     */
    @RequestMapping(value = "/orderComfirm", method = RequestMethod.POST)
    public ResponseResult orderComfirm(String commodities) {
        if (StringUtils.isEmpty(commodities)) {
            JSON.toJSONString(ResponseResult.instance(102011));
        }
        Map<String, Object> dto = new HashMap<>();
        JSONArray jsonArray = JSONArray.parseArray(commodities);
        List<CommoditySkuDto> commoditySkus = new ArrayList<>();
        CommoditySkuDto commoditySkuItem = null;
        JSONObject subInfo = null;
        int totalNum = 0, totalPrice = 0, num = 0, price = 0;
        int freight = 0, indepFreight = 0;
        Map<Long, Integer> commodityFreight = new HashMap<>();
        Map<Long, Integer> commodityIndepFreight = new HashMap<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            subInfo = jsonArray.getJSONObject(i);
            commoditySkuItem = commodityService.queryByIdAndSkuId(subInfo.getLong("id"), subInfo.getString("skuId"));
            if (null == commoditySkuItem) {
                return ResponseResult.failInstance(
                        String.format("商品Id[%d]和规格Id[%s]有误，查询不到商品售价", subInfo.getLong("id"), subInfo.getString("skuId")));
            }
            num = subInfo.getInteger("num");
            price = num * commoditySkuItem.getSalePrice();
            totalNum += num;
            totalPrice += price;
            if (null != commoditySkuItem.getFreightType()) {
                if (1 == commoditySkuItem.getFreightType()) {
                    commodityFreight.put(commoditySkuItem.getId(), commoditySkuItem.getFreight());
                } else if (2 == commoditySkuItem.getFreightType()) {
                    commodityIndepFreight.put(commoditySkuItem.getId(), commoditySkuItem.getFreight());
                }
            }
            commoditySkuItem.setNum(num);
            commoditySkuItem.setPrice(price);
            commoditySkus.add(commoditySkuItem);
        }
        freight = commodityFreight.entrySet().stream().mapToInt(entry -> entry.getValue()).sum();
        indepFreight = commodityIndepFreight.entrySet().stream().mapToInt(entry -> entry.getValue()).sum();
        freight = freight > freight_limit ? freight_limit : freight;
        freight += indepFreight;
        dto.put("items", commoditySkus);
        dto.put("totalNums", totalNum);
        dto.put("totalCommodityPrice", totalPrice);
        dto.put("freight", freight);
        dto.put("totalPrice", totalPrice + freight);
        return ResponseResult.successInstance().setData(dto);
    }

    /**
     * 订单保存
     *
     * @param params
     * @return 参数：
     * openId 用户OPENID,
     * totalPrice 订单总金额,
     * freightPrice 运费,
     * discountPrice 优惠金额,
     * discountCode 优惠码,
     * discountName 优惠券名称,
     * removeShopCarIds 移除购物车Ids [1,2]
     * buyer:{userName:名称,mobile:电话,provinceId:省ID,province:重庆,cityId:市ID,city:重庆市,areaId:区ID,area:南岸区,address:互联网产业园}
     * commodities:[{id:商品ID,skuId:商品SKUID,num:商品数量,price:商品价格(数量X单价),type:商品类别},
     * {id:商品ID2,skuId:商品SKUID2,num:商品数量2,price:商品价格(数量X单价),type:商品类别}]
     */
    @RequestMapping(value = "/submitOrder", method = RequestMethod.POST)
    public ResponseResult submitOrder(@RequestParam Map<String, String> params,
                              @RequestParam String submitToken, HttpServletRequest request) {
        if ( JedisCache.deleteObject(submitToken) ) {
            if (StringUtils.isEmpty(params.get("buyer"))) {
                return ResponseResult.instance(102001);
            } else if (StringUtils.isEmpty(params.get("commodities"))) {
                return ResponseResult.instance(102002);
            }
            UserInfo userInfo = (UserInfo) request.getAttribute(Constant.APP_USER_SESSION_KEY);
            params.put("openId", userInfo.getOpenId());
            params.put("userName", userInfo.getUserName());
            //保存订单
            ResponseResult result = orderService.save(params);
            //下单成功移除对应购物车项
            if (0 == result.getCode() && null != params.get("removeShopCarIds")) {
                threadPoolTaskExecutor.execute(() -> {
                    Map<String, Object> updateJson = new HashMap<>();
                    updateJson.put("removeIds", params.get("removeShopCarIds"));
                    shoppingCartService.update(JSON.toJSONString(updateJson), userInfo.getOpenId());
                });
            }
            return result;
        } else {
            return ResponseResult.instance(101003);
        }
    }

    /**
     * APP订单查询
     *
     * @param requset
     * @return
     */
    @RequestMapping("/list")
    public ResponsePageResult list(@RequestParam Map<String, Object> params, PageUtil pageUtil, HttpServletRequest requset) {
        params.put("ownerOpenId", getUserInfo(requset).getOpenId());
        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status) && status.indexOf(",") > 0) {
            String[] statusArray = status.split(",");
            Integer[] sa = new Integer[statusArray.length];
            for (int i = 0; i < statusArray.length; i++) {
                sa[i] = Integer.valueOf(statusArray[i]);
            }
            params.put("statusArray", sa);
            params.remove("status");
        }
        String payStatus = (String) params.get("payStatus");
        if (!StringUtils.isEmpty(payStatus)) {
            params.put("appPayStatus", Integer.valueOf(payStatus));
            params.remove("payStatus");
        }
        // params.put("payStatus", OrderStatusEnum.WAITPAYMENT.getValue());
        return orderService.getListByPage(params, pageUtil.getPageNo(),pageUtil.getPageSize());
    }

    /**
     * 查看订单详情
     *
     * @param orderId
     * @return
     */
    @RequestMapping("/detail")
    public ResponseResult detail(@RequestParam String orderId, HttpServletRequest request) {
        //ResponseResult result = orderService.getByPage(orderId);
        OrderDto orderDto = orderService.getOrderDtoByOrderId(orderId, getUserInfo(request).getOpenId());
        if (orderDto == null) {
            return ResponseResult.instance(101006);
        }
        JSONObject orderDtoJson = JSON.parseObject(JSON.toJSONString(orderDto));
        //未支付显示支付数据
        if (OrderPayStatusEnum.NONPAYMENT.getValue().shortValue() == orderDto.getPayStatus()) {
            OrderPayData payData = orderService.getPayDataByOrderId(orderDto.getOrderId());
            JSONObject json = new JSONObject();
            json.put("out_trade_no", payData.getOrderId());
            json.put("trade_no", payData.getTradeNo());
            json.put("payData", payData.getPayData());
            orderDtoJson.put("payment", json);
        }
        return ResponseResult.successInstance().setData(orderDtoJson);
    }

    /**
     * 取消订单
     *
     * @param orderId
     * @param request
     * @return
     */
    @RequestMapping(value = "/cancelOrder", method = RequestMethod.POST)
    public ResponseResult cancelOrder(@RequestParam String orderId, HttpServletRequest request) {
        ResponseResult rst = orderService.updateClose(orderId, this.getOperator(request));
        /*if (0 == rst.getCode()) {
            threadPoolTaskExecutor.execute(() -> {
                orderCloseJob.removeFormCache(orderId);
            });
        }*/
        return rst;
    }


    /**
     * 完成订单
     *
     * @param orderId
     * @param request
     * @return
     */
    @RequestMapping(value = "/finishOrder", method = RequestMethod.POST)
    public ResponseResult finishOrder(@RequestParam String orderId, HttpServletRequest request) {
        ResponseResult rst = orderService.updateFinish(orderId, new Date(), this.getOperator(request));
       /* if (0 == rst.getCode()) {
            threadPoolTaskExecutor.execute(() -> {
                orderFinishJob.removeFormCache(orderId);
            });
        }*/
        return rst;
    }

    /**
     * 查询交易状态
     *
     * @param orderId
     * @param tradeNo
     * @return
     */
    @RequestMapping(value = "/queryOrder", method = RequestMethod.POST)
    public ResponseResult queryOrder(@RequestParam String orderId, @RequestParam String tradeNo) {
        ResponseResult result = orderPayService.queryTrade(orderId, tradeNo);
        if (result.getCode() == 0 && result.getData() != null) {
            JSONObject json = (JSONObject) result.getData();
            String trade_tsatus = json.getString("trade_status");
            if ("TRADE_FINISHED".equalsIgnoreCase(trade_tsatus)) {
                return ResponseResult.successInstance();
            }
            String out_trade_no_resp = json.getString("out_trade_no");
            String trade_no_resp = json.getString("trade_no");
            Map<String, String> params = new HashMap<String, String>(2);
            params.put("out_trade_no", out_trade_no_resp);
            params.put("trade_no", trade_no_resp);
            TradeDetail tradeDetail = null;
            tradeDetail = getTradeDetail(out_trade_no_resp,trade_no_resp);
            if (tradeDetail == null) {
            	tradeDetail = FinanceFactory.getTradeDetailInstance(params);
            	tradeDetail.setTdId(null);
            }
            tradeDetail.setName("交易订单号-" + out_trade_no_resp);
            tradeDetail.setPayerId(json.getString("open_id"));
            if (StringUtils.isEmpty(tradeDetail.getPayer())) {
            	tradeDetail.setPayer(json.getString("buyer_logon_id"));
			}
            tradeDetail.setAmount(json.getInteger("buyer_pay_amount"));//NumberUtil.stringToLongToAmount(json.getString("buyer_pay_amount")));
            tradeDetail.setTradeStatus(trade_tsatus);
            if ("WAIT_BUYER_PAY".equalsIgnoreCase(trade_tsatus)) {
                tradeDetail.setStatus(TradeStatusEnum.CREATE.getValue());
                result.setCode(-1);
                result.setMessage("订单等待支付");
            } else if ("TRADE_CLOSED".equalsIgnoreCase(trade_tsatus)) {
                tradeDetail.setStatus(TradeStatusEnum.CLOSE.getValue());
                result.setCode(-1);
                result.setMessage("交易已关闭");
            } else if ("TRADE_SUCCESS".equalsIgnoreCase(trade_tsatus)) {
                tradeDetail.setStatus(TradeStatusEnum.SUCCESS.getValue());
            }
            tradeDetail.setPayeeId(Constant.appID);
            tradeDetail.setPayee(Constant.appName);
            Boolean rs = tradeDetailService.saveOrUpdate(tradeDetail);
            //修改订单状态
            if ( rs && tradeDetail.getStatus().equals(TradeStatusEnum.SUCCESS.getValue())) {
                Date da = null;
                Long val = json.getLong("gmt_payment");
                if (val != null) {
                    val = val * 1000;
                    da = new Date(val);
                }
                orderService.updatePaySuccess(orderId, da, getUser(tradeDetail.getPayerId(), tradeDetail.getPayer()));
            }
        }
        return result;
    }
    
    private TradeDetail getTradeDetail(String formId,String tradeNo) {
    	ResponseResult res = tradeDetailService.getByIdNo(formId, tradeNo);
    	return res.getData() == null ? null : 
    		JSON.parseObject(JSON.toJSONString(res.getData()), TradeDetail.class);
    }

    /**
     * 支付回调
     *
     * @return
     */
    @RequestMapping(value = "/callback/paymentCallback", method = RequestMethod.POST)
    public String paymentCallback(@RequestParam Map<String, String> params) {
        logger.info("支付回调参数：" + params.toString());
        try {
            ResponseResult result = orderPayService.tradeCallbackCheckSign(params);
            if (result.getCode() == 0) {
                String tradeStatus = params.get("trade_status");
                if ("TRADE_FINISHED".equalsIgnoreCase(tradeStatus)) {
                    return "success";
                }
                String orderId = params.get("out_trade_no");
                String tradeNo = params.get("trade_no");
                //String tradeNo = params.get("trade_no");
                
                TradeDetail tradeDetail = null;
                tradeDetail = getTradeDetail(orderId,tradeNo);
                if (tradeDetail == null) {
                	tradeDetail = FinanceFactory.getTradeDetailInstance(params);
                	tradeDetail.setTdId(null);
                }
                
                tradeDetail.setName("交易订单号-" + orderId);
                //tradeDetail.setPayerId(params.get("open_id")); //付款人ID
                //UserInfo userInfo = UserInfoUtil.getUserInfo(params.get("open_id"));
                tradeDetail.setPayerId(params.get("open_id"));
                if (StringUtils.isEmpty(tradeDetail.getPayer())) {
                	tradeDetail.setPayer(params.get("buyer_logon_id"));
				}
                tradeDetail.setType(TradeTypeEnum.INCOME.getValue());
                tradeDetail.setAmount(Integer.valueOf(params.get("buyer_pay_amount")));//NumberUtil.stringToLongToAmount(params.get("buyer_pay_amount")));
                tradeDetail.setTradeStatus(tradeStatus);
                if ("WAIT_BUYER_PAY".equalsIgnoreCase(tradeStatus)) {
                    tradeDetail.setStatus(TradeStatusEnum.CREATE.getValue());
                } else if ("TRADE_CLOSED".equalsIgnoreCase(tradeStatus)) {
                    tradeDetail.setStatus(TradeStatusEnum.CLOSE.getValue());
                } else if ("TRADE_SUCCESS".equalsIgnoreCase(tradeStatus)) {
                    tradeDetail.setStatus(TradeStatusEnum.SUCCESS.getValue());
                }
                tradeDetail.setPayeeId(Constant.appID);
                tradeDetail.setPayee(Constant.appName);
                Boolean rs = tradeDetailService.saveOrUpdate(tradeDetail);
                //修改订单状态
                if ( rs && tradeDetail.getStatus().equals(TradeStatusEnum.SUCCESS.getValue()) ) {
                    Date da = null;
                    if (!StringUtils.isEmpty(params.get("gmt_payment"))) {
                        Long val = Long.valueOf(params.get("gmt_payment")) * 1000;
                        da = new Date(val);
                    }
                    orderService.updatePaySuccess(orderId, da, getUser(tradeDetail.getPayerId(), tradeDetail.getPayer()));
                }
                return "success";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "fail";
    }

    /**
     * 支付关闭
     *
     * @return
     */
    @RequestMapping(value = "/callback/closeCallback", method = RequestMethod.POST)
    public String closeCallback(@RequestParam Map<String, String> params) {
        logger.info("关闭回调参数：" + params.toString());
        try {
            ResponseResult result = orderPayService.tradeCallbackCheckSign(params);
            if (result.getCode() == 0) {
                String tradeStatus = params.get("trade_status");
                if (!"TRADE_CLOSED".equalsIgnoreCase(tradeStatus)) {
                    return "success";
                }
                String orderId = params.get("out_trade_no");
                //String tradeNo = params.get("trade_no");
                TradeDetail tradeDetail = FinanceFactory.getTradeDetailInstance(params);
                tradeDetail.setName("交易订单号-" + orderId);
                tradeDetail.setPayerId(params.get("open_id")); //付款人ID
                tradeDetail.setPayer(params.get("buyer_logon_id")); //付款人名称
                tradeDetail.setType(TradeTypeEnum.INCOME.getValue());
                tradeDetail.setAmount(Integer.valueOf(params.get("buyer_pay_amount")));//new BigDecimal(params.get("buyer_pay_amount")));
                tradeDetail.setTradeStatus(tradeStatus);
                tradeDetail.setStatus(TradeStatusEnum.CLOSE.getValue());
                tradeDetail.setPayeeId(Constant.appID);
                tradeDetail.setPayee(Constant.appName);
                tradeDetailService.saveOrUpdate(tradeDetail);
                //修改订单状态
                if (tradeDetail.getStatus().equals(TradeStatusEnum.SUCCESS.getValue())) {
                    orderService.updateClose(orderId, getUser(tradeDetail.getPayerId(), tradeDetail.getPayer()));
                }
                return "success";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "fail";
    }


    private User getUser(String userId, String userName) {
        User user = new User();
        user.setOperatorId(userId);
        user.setOperator(userName);
        return user;
    }
}
