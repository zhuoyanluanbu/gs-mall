package com.gs.mall.export.impl;


import com.gs.mall.export.ExportEngine;
import com.gs.mall.export.common.CommonEngine;
import com.gs.mall.export.common.ExportFile;
import com.gs.mall.export.common.ExportType;
import com.gs.mall.export.excel.ExcelFileAdapter;
import com.gs.mall.export.excel.items.ExcelCell;
import com.gs.mall.export.excel.items.ExcelData;
import com.gs.mall.export.excel.items.ExcelRow;
import com.gs.mall.export.factory.ExcelStyleFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Excel文件操作引擎
 * Created by chenchuan on 2017/9/1.
 */
@Component("excelEngine")
public class ExcelEngine extends CommonEngine implements ExportEngine {
    /**
     * 指定文件类型
     **/
    public static final ExportType FILE_TYPE = ExportType.EXCEL;

    @Override
    public File createExportFile(ExportFile exportFile) throws Exception {
        File file = new File(exportFile.fileName());
        if (file.exists())
            file.delete();
        file.createNewFile();
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("sheet1");
        sheet.setDefaultColumnWidth(16);
        ExcelData data = (ExcelData) exportFile.getData();
        List<ExcelRow> dataRows = data.getRows();
        List<ExcelCell> dataCells = null;
        //Excel 对象元素
        Row row = null;
        CellRangeAddress cra = null;
        Cell cell = null;
        try {
            for (ExcelRow dataRow : dataRows) {
                //合并单元格
                if (-1 != dataRow.getRowspan()) {
                    cra = new CellRangeAddress(dataRow.index(),
                            dataRow.index() + dataRow.getRowspan(),
                            1, 1);
                    sheet.addMergedRegion(cra);
                }
                row = sheet.createRow(dataRow.getOffsetY());
                dataCells = dataRow.getCells();
                for (ExcelCell dataCell : dataCells) {
                    try {
                        cell = row.createCell(dataCell.index());
                        if (-1 != dataCell.getColspan()) {
                            cra = new CellRangeAddress(dataRow.index(), dataRow.index(),
                                    dataCell.index(), dataCell.index() + dataCell.getColspan() - 1);
                            sheet.addMergedRegion(cra);


                        }
                        cell.setCellStyle(ExcelStyleFactory.adapterPoiExcelStyle(wb, dataCell));
                        cell.setCellValue(dataCell.getContent());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                ExcelStyleFactory.refreshStyle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int col = 2; col < data.getTotalCols(); col++) {
            sheet.autoSizeColumn((short) col);
        }

        FileOutputStream os = new FileOutputStream(file);
        wb.write(os);
        os.close();
        return file;
    }

    @Override
    public ExportFile genFile(Object data, ExcelFileAdapter fileAdapter) {
        ExportFile exportFile = fileAdapter.excelFileAdapter(data);
        return exportFile;
    }

    @Override
    public ExportType exportType() {
        return FILE_TYPE;
    }

    @Override
    public ExportFile exchange(ExportFile fromFile) {
        return null;
    }
}
