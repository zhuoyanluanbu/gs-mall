package com.gs.mall.common.util;

/**
 * Created by huangyp on 2017/8/7.
 */
public class PageUtil {

    /**
     * 每页大小
     */
    private int pageSize;

    /**
     * 查询起始位置
     */
    private int startIndex;

    /**
     * 当前第页数
     */
    private int pageNo;

    /**
     * 总行数
     */
    private int totalRows;

    public PageUtil(){
        this(1,20);
    }

    public PageUtil(int pageNo, int pageSize) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStartIndex() {
        if ( pageSize == 0 ||  pageNo == 0 ) {
            return 0;
        }
        int si = (pageNo-1) * pageSize;
        return si;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }
}
