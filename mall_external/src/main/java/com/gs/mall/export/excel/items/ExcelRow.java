package com.gs.mall.export.excel.items;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchuan on 2017/9/1.
 */
public class ExcelRow {
    /**
     * 纵向位置
     **/
    private int offsetY = 0;
    /**
     * 纵向合并
     **/
    int rowspan = -1;
    /**
     * 本行元素
     **/
    List<ExcelCell> cells;

    public ExcelRow(int rowIndex, List<ExcelCell> cells) {
        index(rowIndex++);
        setCells(cells);
    }

    public ExcelRow(int rowIndex) {
        index(rowIndex);
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public int getRowspan() {
        return rowspan;
    }

    public void setRowspan(int rowspan) {
        this.rowspan = rowspan;
    }

    public List<ExcelCell> getCells() {
        return cells;
    }

    public void setCells(List<ExcelCell> cells) {
        List<ExcelCell> newCells = new ArrayList<>();
        for (ExcelCell cell:cells) {
            newCells.add(cell);
        }
        this.cells = newCells;
    }
    /**
     *返回行的位置
     *Author:cdaic
     *Create:2017/5/16
     */
    public int index() {
        return getOffsetY();
    }
    /**
     *设置行的位置
     *Author:cdaic
     *Create:2017/5/16
     */
    public void index(int index) {
        setOffsetY(index);
    }
    /**
     *返回一行占的总格数
     *Author:cdaic
     *Create:2017/5/16
     */
    public int totalCellSpan() {
        int total = -1;
        List<ExcelCell> cells = getCells();
        if (null != cells && !cells.isEmpty()) {
            for (ExcelCell cell : cells) {
                total += cell.getColspan();
            }
        }
        return total;
    }
}
