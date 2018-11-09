package com.gs.mall.export.factory;

import com.gs.mall.export.excel.items.ExcelCell;
import com.gs.mall.export.excel.items.ExcelStyle;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by chenchuan on 2017/9/1.
 */
@Component
public class ExcelStyleFactory {
    public static final String DEF = "def";
    public static final String BIG_T = "bigTitle";
    public static final String TH_T = "tableTh";

    private static Map<String, ExcelStyle> styleStore;

    private static Map<String, CellStyle> cellStyleMap;
    @PostConstruct
    public void init(){
        styleStore = new HashMap<>();
        cellStyleMap = new HashMap<>();
        styleStore.put(DEF, new ExcelStyle("center", 12));
        styleStore.put(BIG_T, new ExcelStyle("center", 16));
        styleStore.put(TH_T, new ExcelStyle("center", 14));
    }

    /**
     * 默认样式
     */
    public static ExcelStyle defStyle() {
        return styleStore.get(DEF);
    }

    /**
     * 大标题
     */
    public static ExcelStyle bigTitle() {
        return styleStore.get(BIG_T);
    }

    /**
     * 表格每列的标题
     */
    public static ExcelStyle tableTh() {
        return styleStore.get(TH_T);
    }

    /**
     * 获取样式
     */
    public static ExcelStyle styleStore(String key) {
        return styleStore.get(key);
    }

    public static CellStyle adapterPoiExcelStyle(Workbook wb, ExcelCell dataCell) {
        ExcelStyle excelStyle = dataCell.getStyle();

        CellStyle cellStyle = getCellStyle(excelStyle);
        if(null==cellStyle){
            cellStyle = wb.createCellStyle();
        }else {
            return cellStyle;
        }
        //边框
        if (excelStyle.isBorder()) {
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
        }
        //字体
        Font font = wb.createFont();
        font.setFontName(excelStyle.getFont());
        font.setFontHeightInPoints((byte)excelStyle.getFontSize());
        cellStyle.setFont(font);
        //对齐
        if("center".equalsIgnoreCase(excelStyle.getTextAlign())){
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
        }else if("left".equalsIgnoreCase(excelStyle.getTextAlign())){
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
        }else if("right".equalsIgnoreCase(excelStyle.getTextAlign())){
            cellStyle.setAlignment(HorizontalAlignment.RIGHT);
        }
        //自动换行
        cellStyle.setWrapText(true);
        setCellStyle(excelStyle,cellStyle);
        return cellStyle;
    }

    private static void setCellStyle(ExcelStyle excelStyle, CellStyle cellStyle) {
        cellStyleMap.put(genStyleKey(excelStyle),cellStyle);
    }

    private static CellStyle getCellStyle(ExcelStyle excelStyle) {
        return cellStyleMap.get(genStyleKey(excelStyle));
    }

    private static String genStyleKey(ExcelStyle excelStyle) {
        return String.format("%s#%s#%s#%s#%s",excelStyle.getFont(),excelStyle.getFontSize()+"",excelStyle.getTextAlign(),excelStyle.isBold()?"1":"0",excelStyle.isBorder()?"1":"0");
    }
    public static void refreshStyle() {
        cellStyleMap = new HashMap<>();
    }
}
