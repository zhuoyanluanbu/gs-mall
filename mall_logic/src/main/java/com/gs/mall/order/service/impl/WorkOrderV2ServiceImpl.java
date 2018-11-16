package com.gs.mall.order.service.impl;

import com.gs.mall.common.util.StringUtil;
import com.gs.mall.order.dao.RefundCommodityDao;
import com.gs.mall.order.dao.WorkOrderFlowDao;
import com.gs.mall.order.dao.WorkOrderFlowRecDao;
import com.gs.mall.order.dao.WorkOrderV2Dao;
import com.gs.mall.order.dto.WorkOrderTableDisplayDto;
import com.gs.mall.order.po.*;
import com.gs.mall.order.service.WorkOrderV2Service;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by huyoucheng on 2018/10/31.
 */
@Service
public class WorkOrderV2ServiceImpl implements WorkOrderV2Service {

    Logger logger = Logger.getLogger(WorkOrderV2ServiceImpl.class);

    static Map<String, Object> workOrderFlows;

    /*
    * 初始化流程操作，分别以列表，键值对，链表的形式储存在一个map里，方便使用
    * */
    @PostConstruct
    void initWorkOrderFlows() {
        workOrderFlows = new HashMap<>(3);
        List<WorkOrderFlow> workOrderFlowList = workOrderFlowDao.getAllWordOrderFlows();
        workOrderFlows.put("list", workOrderFlowList);
        Map<String, WorkOrderFlow> workOrderFlowMap = workOrderFlowList.stream().collect(Collectors.toMap(WorkOrderFlow::getOperation, w -> w));
        workOrderFlows.put("map", workOrderFlowMap);
        WorkOrderFlowLink workOrderFlowLink = new WorkOrderFlowLink(workOrderFlowList.get(0));
        for (int i = 1; i < workOrderFlowList.size(); i++) {
            if (workOrderFlowList.get(i).getOperation() != WorkOrderFlow.Close)
                workOrderFlowLink.add(new WorkOrderFlowLink(workOrderFlowList.get(i)));
        }
        workOrderFlows.put("link", workOrderFlowLink);
    }
    public List<WorkOrderFlow> getAllWorkOrderFlowsList() {
        return (List<WorkOrderFlow>) this.workOrderFlows.get("list");
    }
    public Map<String, WorkOrderFlow> getAllWorkOrderFlowsMap() {
        return (Map<String, WorkOrderFlow>) this.workOrderFlows.get("map");
    }
    public WorkOrderFlowLink getAllWorkOrderFlowLink() {
        return (WorkOrderFlowLink) this.workOrderFlows.get("link");
    }




    @Autowired
    WorkOrderFlowDao workOrderFlowDao;

    @Autowired
    WorkOrderV2Dao workOrderV2Dao;

    @Autowired
    WorkOrderFlowRecDao workOrderFlowRecDao;

    @Autowired
    RefundCommodityDao refundCommodityDao;


    /*
    * 提交一个工单
    * */
    @Override
    @Transactional
    public Boolean submitWorkOrder(WorkOrderV2 workOrderV2, RefundCommodity refundCommodity,String other) throws WorkOrderV2Exception{
        if (workOrderV2Dao.insert(workOrderV2) > 0) {
            Map<String,WorkOrderFlow> workOrderFlowMap = this.getAllWorkOrderFlowsMap();
            WorkOrderFlowRec workOrderFlowRec;
            WorkOrderFlow workOrderFlow;
            if (workOrderV2.getFromwhere() == 1) {//如果提交工单直接由后台发起
                if (!StringUtil.isNotEmpty(other))
                    throw new WorkOrderV2Exception(WorkOrderV2Exception.Item.LogisticsError);
                workOrderFlow = workOrderFlowMap.get(WorkOrderFlow.NoticeBackMail);
                workOrderFlowRec = new WorkOrderFlowRec(0, workOrderV2.getWo_id(), workOrderV2.getFromid(), workOrderV2.getFromname(),
                        workOrderFlow.getOperation(), new Date(),
                        1, other, null, this.getPreAndNextFlowByName(workOrderFlow.getOperation())[1].getOperation(),
                        0, workOrderV2.getWo_reason(),
                        workOrderV2.getRemark(),new Date());
            }else {
                workOrderFlow = workOrderFlowMap.get(WorkOrderFlow.View);
                workOrderFlowRec = new WorkOrderFlowRec(0, workOrderV2.getWo_id(), null, null, workOrderFlow.getOperation(), new Date(),
                        workOrderV2.getFromwhere(), null, null, this.getPreAndNextFlowByName(workOrderFlow.getOperation())[1].getOperation(),
                        0, workOrderV2.getWo_reason(),
                        workOrderV2.getRemark(),new Date());
            }
            if (workOrderFlowRecDao.insert(workOrderFlowRec) > 0 &&
                    refundCommodityDao.insert(refundCommodity) > 0) {
                return true;
            }
        }
        return false;
    }

    /*
    * 当前工单的的当前步骤
    * */
    @Override
    public WorkOrderFlowRec currentWorkOrderByOrderIdOrWoId(String order_idOrWo_id) {
        WorkOrderFlowRec workOrderFlowRec = null;
        if (order_idOrWo_id.contains("G")) {
            Map<String, Object> map = new HashMap<>(1);
            map.put("order_id", order_idOrWo_id);
            if(workOrderV2Dao.selectByParam(map, 0, 1).size()<1) return null;
            WorkOrderV2 workOrderV2 = workOrderV2Dao.selectByParam(map, 0, 1).get(0);
            order_idOrWo_id = workOrderV2.getWo_id();
        }
        workOrderFlowRec = workOrderFlowRecDao.currentWorkOrderByWoId(order_idOrWo_id);
        return workOrderFlowRec;
    }

    /*
    * 该工单的所有记录的步骤
    * */
    public List<WorkOrderFlowRec> allExistWorkOrderFlowRec(String order_idOrWo_id) {
        Map<String, Object> map = new HashMap<>(1);
        if (order_idOrWo_id.contains("R")) {
            map.put("wo_id", order_idOrWo_id);
        } else {
            map.put("order_id", order_idOrWo_id);

        }
        return workOrderFlowRecDao.selectByParam(map, 0, 999);
    }

    /*
    * 当前工单是否已经关闭
    * */
    public boolean workOrderIsClosed(String order_idOrWo_id){
        if (this.eqOperation(this.currentWorkOrderByOrderIdOrWoId(order_idOrWo_id).getOperation(),WorkOrderFlow.Close))
            return true;
        return false;
    }
    public boolean workOrderIsClosed(WorkOrderFlowRec workOrderFlowRec){
        if (this.eqOperation(workOrderFlowRec.getOperation(), WorkOrderFlow.Close)) return true;
        return false;
    }
    /*
    * 当前工单是否已经完成
    * */
    public boolean workOrderIsFinished(String order_idOrWo_id){
        if (this.eqOperation(this.currentWorkOrderByOrderIdOrWoId(order_idOrWo_id).getOperation(),WorkOrderFlow.Finish))
            return true;
        return false;
    }
    public boolean workOrderIsFinished(WorkOrderFlowRec workOrderFlowRec){
        if (this.eqOperation(workOrderFlowRec.getOperation(),WorkOrderFlow.Finish)) return true;
        return false;
    }

    /*
    * 确认或者驳回当前步骤，生成下一步骤
    * */
    @Override
    @Transactional
    public WorkOrderFlowRec confirmOrRollBackCurrentFlowAndCreateNextFlow(WorkOrderFlowRec clientCurFlowRec) throws WorkOrderV2Exception {
        //当前正在进行的操作
        WorkOrderFlowRec currentWorkOrderFlowRec = this.currentWorkOrderByOrderIdOrWoId(clientCurFlowRec.getWo_id());

        if(currentWorkOrderFlowRec==null)
            throw new WorkOrderV2Exception(WorkOrderV2Exception.Item.WorkOrderNotExist);

        if(this.workOrderIsClosed(currentWorkOrderFlowRec))
            throw new WorkOrderV2Exception(WorkOrderV2Exception.Item.WorkOrderClosed);

        if(this.workOrderIsFinished(currentWorkOrderFlowRec))
            throw new WorkOrderV2Exception(WorkOrderV2Exception.Item.WorkOrderFinished);

        if (!StringUtil.isNotEmpty(clientCurFlowRec.getOperator_id()) || !StringUtil.isNotEmpty(clientCurFlowRec.getOperator())) {
            //这里必须填写操作者
            throw new WorkOrderV2Exception(WorkOrderV2Exception.Item.OperatorNull);
        }

        if (this.eqOperation(currentWorkOrderFlowRec.getOperation(),WorkOrderFlow.NoticeBackMail) ||
                this.eqOperation(currentWorkOrderFlowRec.getOperation(), WorkOrderFlow.BackMail)) {
            if (!StringUtil.isNotEmpty(clientCurFlowRec.getLogistics())) { //  为空
                //这里应该抛异常，未填写物流号
                throw new WorkOrderV2Exception(WorkOrderV2Exception.Item.LogisticsError);
            }
        }

        //未通过，需要填写原因
        if (this.eqOpState(clientCurFlowRec.getStatus(),WorkOrderFlowRec.NotPass) && !StringUtil.isNotEmpty(clientCurFlowRec.getReason()))
            throw new WorkOrderV2Exception(WorkOrderV2Exception.Item.ReasonNull);

        this.setReadyUpdateData(currentWorkOrderFlowRec, clientCurFlowRec);
        currentWorkOrderFlowRec = this.updateFlowRec(currentWorkOrderFlowRec);
        //触发工单表里面状态的更改
        this.triggerWorkOrderV2StateUpdate(currentWorkOrderFlowRec);
        return this.bornNextWorkFlowRec(currentWorkOrderFlowRec);
    }

    /*
    * 生成下一个操作
    * */
    private WorkOrderFlowRec bornNextWorkFlowRec(WorkOrderFlowRec current) {
        if (this.eqOpState(current.getStatus(),WorkOrderFlowRec.Pass)) {
            if (current.getOperation().contains(WorkOrderFlow.FinanceView)){//如果财务通过，直接这里退款
                //这里应该执行退款操作
                new Thread(() -> {
                    System.out.println("退款啦！！！！");
                }).start();
            }
            //下一步应该做的操作
            WorkOrderFlow nextWorkOrderFlow = this.getPreAndNextFlowByName(current.getOperation())[1];
            //下下一步应该做的操作
            WorkOrderFlow nextNextWorkOrderFlow = this.getPreAndNextFlowByName(nextWorkOrderFlow.getOperation())[1];
            WorkOrderFlowRec nextWorkOrderFlowRec = this.insertFlowRec(new WorkOrderFlowRec(0, current.getWo_id(),
                    null, null, nextWorkOrderFlow.getOperation(), new Date(), -1, null,
                    current.getOperation(),
                    nextNextWorkOrderFlow==null? null:nextNextWorkOrderFlow.getOperation(),
                    this.eqOperation(nextWorkOrderFlow.getOperation(), WorkOrderFlow.Finish)?1:0,
                    null, null,null,new Date(),current.getOperator(),current.getOperator_id()));
            return nextWorkOrderFlowRec;
        } else if ( this.eqOpState(current.getStatus(),WorkOrderFlowRec.NotPass) ){
            if (!this.getAllWorkOrderFlowsMap().get(current.getOperation())
                    .getOwn().contains(WorkOrderFlow.WorkOrderFlowOwner.Finance)) {//如果当前步骤没有财务参与,客服不通过，直接关闭工单
                WorkOrderFlowRec closeWorkOrderFlowRec = this.insertFlowRec(new WorkOrderFlowRec(0, current.getWo_id(),
                        current.getOperator_id(), current.getOperator(),
                        WorkOrderFlow.Finish, new Date(), 1, null, current.getOperation(),
                        null, 1, current.getReason(), current.getRemark(),null,new Date(),current.getFrom_operator(),current.getFrom_operator_id()));
                return closeWorkOrderFlowRec;
            } else {
                //回到上一步骤
                WorkOrderFlowRec rollBackWorkOrderFlowRec = this.insertFlowRec(new WorkOrderFlowRec(0, current.getWo_id(),
                        null, null, current.getPre_operation(), new Date(), 1, null,
                        this.getPreAndNextFlowByName(current.getPre_operation())[0]==null?
                                null:this.getPreAndNextFlowByName(current.getPre_operation())[0].getOperation(),
                        current.getOperation(), 1, null, null,null,new Date(),current.getOperator(), current.getOperator_id()));
                return rollBackWorkOrderFlowRec;
            }
        }
        return current;
    }

    /*
    * 工单关闭
    * */
    @Transactional
    public WorkOrderFlowRec closeWorkOrder(WorkOrderFlowRec workOrderFlowRec) throws WorkOrderV2Exception{
        if(this.workOrderIsClosed(workOrderFlowRec.getWo_id()))
            throw new WorkOrderV2Exception(WorkOrderV2Exception.Item.WorkOrderClosed);
        if(!StringUtil.isNotEmpty(workOrderFlowRec.getReason()))
            throw new WorkOrderV2Exception(WorkOrderV2Exception.Item.ReasonNull);
        workOrderFlowRec.setOperation(WorkOrderFlow.Close);
        workOrderFlowRec.setStatus(1);
        workOrderFlowRec.setFrom_operator(workOrderFlowRec.getOperator());
        workOrderFlowRec.setFrom_operator_id(workOrderFlowRec.getOperator_id());
        workOrderFlowRec.setCreate_time(new Date());
        workOrderFlowRec.setOperation_time(new Date());
        //WorkOrderV2更新状态
        workOrderV2Dao.update(new WorkOrderV2(workOrderFlowRec.getWo_id(),WorkOrderV2.HaveClosed));
        return this.insertFlowRec(workOrderFlowRec);
    }

    /*
    * 增加一个操作
    * */
    public WorkOrderFlowRec insertFlowRec(WorkOrderFlowRec workOrderFlowRec) {
        workOrderFlowRecDao.insert(workOrderFlowRec);
        return workOrderFlowRec;
    }

    /*
    * 修改一个操作
    * */
    public WorkOrderFlowRec updateFlowRec(WorkOrderFlowRec workOrderFlowRec) {
        workOrderFlowRec.setOperation_time(new Date());
        workOrderFlowRecDao.update(workOrderFlowRec);
        return workOrderFlowRec;
    }


    /*
    * 查找上一步和下一步的操作
    * */
    private WorkOrderFlow[] getPreAndNextFlowByName(String currentFlow) {
        WorkOrderFlowLink workOrderFlowLink = this.getAllWorkOrderFlowLink();
        while (workOrderFlowLink.next != null) {
            if (this.eqOperation(workOrderFlowLink.value.getOperation(), currentFlow)) {
                WorkOrderFlowLink pre = workOrderFlowLink.pre;
                WorkOrderFlowLink next = workOrderFlowLink.next;
                return new WorkOrderFlow[]{pre==null?null:pre.value , next==null?null:next.value} ;
            }
            workOrderFlowLink = workOrderFlowLink.next;
        }
        WorkOrderFlowLink pre = workOrderFlowLink.pre;
        WorkOrderFlowLink next = workOrderFlowLink.next;
        return new WorkOrderFlow[]{pre==null?null:pre.value , next==null?null:next.value} ;
    }

    /*
    * 设置准备update的数据
    * */
    private void setReadyUpdateData(WorkOrderFlowRec currentWorkOrderFlowRec, WorkOrderFlowRec clientCurFlowRec) {
        currentWorkOrderFlowRec.setStatus(clientCurFlowRec.getStatus());
        currentWorkOrderFlowRec.setLogistics(clientCurFlowRec.getLogistics());
        currentWorkOrderFlowRec.setOperation_time(new Date());
        currentWorkOrderFlowRec.setOperation_from(clientCurFlowRec.getOperation_from());
        currentWorkOrderFlowRec.setOperator(clientCurFlowRec.getOperator());
        currentWorkOrderFlowRec.setOperator_id(clientCurFlowRec.getOperator_id());
        currentWorkOrderFlowRec.setReason(clientCurFlowRec.getReason());
        currentWorkOrderFlowRec.setRemark(clientCurFlowRec.getRemark());
    }

    /*
    * 操作比较
    * */
    private boolean eqOperation(String $1,String $2){
        return $1.equalsIgnoreCase($2);
    }
    /*
    * 状态比较
    * */
    private boolean eqOpState(int $1,int $2){
        return $1 == $2;
    }

    /*
    * 触发更改工单表的state字段 (workOrderV2 state)
    * */
    private void triggerWorkOrderV2StateUpdate(WorkOrderFlowRec wc){
        int workOrderState = -1;
        String op = wc.getOperation();
        if (this.eqOpState(wc.getStatus(),WorkOrderFlowRec.Pass)){
            if(this.eqOperation(op,WorkOrderFlow.View)){
                workOrderState = WorkOrderV2.WaitViewPass;
            }else if(this.eqOperation(op,WorkOrderFlow.BackMail)){
                workOrderState = WorkOrderV2.WaitRefund;
            }else if(this.eqOperation(op,WorkOrderFlow.FinanceView)){
                workOrderState = WorkOrderV2.HaveRefunded;
            }else if(this.eqOperation(op,WorkOrderFlow.Close)){
                workOrderState = WorkOrderV2.HaveClosed;
            }
        }
        if(workOrderState > 0){
            workOrderV2Dao.update(new WorkOrderV2(wc.getWo_id(),workOrderState));
        }
    }

    @Override
    public WorkOrderFlow saveOrUpdate(WorkOrderFlow workOrderFlow) {
        int i = 0;
        if (workOrderFlow.getId() <= 0) {
            i = workOrderFlowDao.insert(workOrderFlow);
        } else {
            i = workOrderFlowDao.update(workOrderFlow);
        }
        return i > 0 ? workOrderFlow : null;
    }

    @Override
    public Boolean deleteWorkOrderFlow(int id) {
        return workOrderFlowDao.deleteById(id) > 0;
    }


    /*
    * 查询工单(各个工单的最新状态)
    * */
    @Override
    public List<WorkOrderTableDisplayData> getWorkOrderTableDisplayDataNewestState(WorkOrderTableDisplayDto workOrderTableDisplayDto) {
        //查询出来的数据可能是下面这种形式,需要通过order_id分理处各个order_id的最新状态
        //order_id:1 -- [o1,o2]
        //order_id:2 -- [o1,o2]
        //order_id:3 -- [o1,o2,o3]
        List<WorkOrderTableDisplayData> workOrderTableDisplayDataListAllOrderIds = workOrderFlowRecDao.getWorkOrderTableDisplayData(workOrderTableDisplayDto);
        List<WorkOrderTableDisplayData> workOrderTableDisplayDatas = new ArrayList<>();
        Map<String,WorkOrderTableDisplayData> map = new HashMap<>();
        for (WorkOrderTableDisplayData wotdd:workOrderTableDisplayDataListAllOrderIds){
            if(!StringUtil.isNotEmpty(wotdd.getOrder_id()) || !StringUtil.isNotEmpty(wotdd.getWo_id()))
                continue;
            WorkOrderFlowRecDescription workOrderFlowRecDescription =
                    WorkOrderFlowRecDescription.instanceForManager(wotdd.getOperation(),wotdd.getStatus(),wotdd.getLogistics(),wotdd.getReason());
            wotdd.setDesc(workOrderFlowRecDescription.getDescription());
            //如果map里面含有这个order_id的实体,比较map里面的实体的operation和wotdd的operation两者的顺序，保留顺序靠后的，顺序靠后的是最新的步骤
            if (map.containsKey(wotdd.getOrder_id())){
                String operation = map.get(wotdd.getOrder_id()).getOperation();
                int increaseIdInMap = this.getAllWorkOrderFlowsMap().get(operation).getId();
                int increaseIdInWotdd = this.getAllWorkOrderFlowsMap().get(wotdd.getOperation()).getId();
                if (increaseIdInWotdd > increaseIdInMap){
                    map.put(wotdd.getOrder_id(),wotdd);
                }
            }else {//如果map里面不含有这个order_id的实体，直接添加到map
                map.put(wotdd.getOrder_id(),wotdd);
            }
        }
        for (Map.Entry<String,WorkOrderTableDisplayData> entry:map.entrySet()){
            //因为map里面的值只会有一个元素，所以直接添加map的值
            workOrderTableDisplayDatas.add(entry.getValue());
        }
        return workOrderTableDisplayDatas;
    }

    /*
    * 查询工单(各个工单的各个状态)
    * */
    @Override
    public List<WorkOrderTableDisplayData> getWorkOrderTableDisplayData(WorkOrderTableDisplayDto workOrderTableDisplayDto) {
        List<WorkOrderTableDisplayData> workOrderTableDisplayDataListAllOrderIds = workOrderFlowRecDao.getWorkOrderTableDisplayData(workOrderTableDisplayDto);
        Iterator<WorkOrderTableDisplayData> it = workOrderTableDisplayDataListAllOrderIds.iterator();
        while (it.hasNext()){
            WorkOrderTableDisplayData wotdd = it.next();
            if(!StringUtil.isNotEmpty(wotdd.getWo_id()) || !StringUtil.isNotEmpty(wotdd.getOrder_id()))
                it.remove();
            WorkOrderFlowRecDescription workOrderFlowRecDescription =
                    WorkOrderFlowRecDescription.instanceForManager(wotdd.getOperation(),wotdd.getStatus(),wotdd.getLogistics(), wotdd.getReason());
            wotdd.setDesc(workOrderFlowRecDescription.getDescription());
        }
        return workOrderTableDisplayDataListAllOrderIds;
    }

}
