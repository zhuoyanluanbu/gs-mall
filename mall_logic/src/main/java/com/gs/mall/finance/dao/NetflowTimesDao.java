package com.gs.mall.finance.dao;

import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.finance.po.NetflowTimes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.util.List;

/**
 * NetflowTimes dao interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:19
 */
@Mapper
public interface NetflowTimesDao extends BaseDao<NetflowTimes,java.lang.Long> {

    /**
     * 查询最新日期
     * @return
     */
    Date querylatestDate();

    /**
     * 查询指定日期的访问次数
     * @param date
     * @return
     */
    Integer queryTimesByDate(Date date);

    /**
     * 查询一个范围的访问次数
     * @param startDate
     * @param endDate
     * @return
     */
    Integer queryTimesByScope(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

	int batchInsert(List<NetflowTimes> list);

}
