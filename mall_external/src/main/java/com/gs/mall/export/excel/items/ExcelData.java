package com.gs.mall.export.excel.items;


import com.gs.mall.export.common.ExportData;

import java.util.List;

/**
 * Created by chenchuan on 2017/9/1.
 */
public class ExcelData extends ExportData {
    int totalRows;
    int totalCols;

    List<ExcelRow> rows;

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getTotalCols() {
        return totalCols;
    }

    public void setTotalCols(int totalCols) {
        this.totalCols = totalCols;
    }

    public List<ExcelRow> getRows() {
        return rows;
    }

    public void setRows(List<ExcelRow> rows) {
        this.rows = rows;
    }
}
