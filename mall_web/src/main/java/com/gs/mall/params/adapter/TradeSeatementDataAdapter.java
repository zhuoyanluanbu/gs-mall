package com.gs.mall.params.adapter;


import com.gs.common.util.IdentityUtil;
import com.gs.mall.export.common.ExportFile;
import com.gs.mall.export.excel.ExcelFileAdapter;
import com.gs.mall.export.excel.items.ExcelCell;
import com.gs.mall.export.excel.items.ExcelData;
import com.gs.mall.export.excel.items.ExcelRow;
import com.gs.mall.export.factory.ExcelStyleFactory;
import com.gs.mall.export.impl.ExcelEngine;
import com.gs.mall.finance.po.TradeStatement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @Filename com.gs.mall.params.adapter.TradeDetailDataAdapter
 * @Description
 * @Version 1.0
 * @Author cdaic
 * @Email cdaic@qq.com
 * @History <li>Author: chenchuan</li>
 * <li>Date: 2017/9/1</li>
 * <li>Version: 1.0</li>
 * <li>Content: create</li>
 */
@Component
public class TradeSeatementDataAdapter implements ExcelFileAdapter {
    @Value("${oss.test.env}")
    Boolean testEnv;

    @Value("${oss.test.file.path}")
    String testFilePath;
    
    @Value("${export.file.path}")
    String exportFilePath;

    @Override
    public ExportFile excelFileAdapter(Object data) {
        List<TradeStatement> tradeDetails = (List<TradeStatement>) data;
        ExportFile exportFile = new ExportFile();
        exportFile.setType(ExcelEngine.FILE_TYPE);
        if( true ) {
            File f = new File(exportFilePath);
            if ( ! f.exists() )  {
                f.mkdirs();
            }
        }
        String filName = exportFilePath + "/" + IdentityUtil.getIdentity("TS");
        exportFile.setName(filName);
        ExcelData exportData = new ExcelData();
        List<ExcelRow> rows = new ArrayList<>();
        ExcelRow row;
        int rowIndex = 0;
        row = new ExcelRow(rowIndex++);
        List<ExcelCell> cells = new ArrayList<>();
        int colIndex = 0;
        for (String title :
                Arrays.asList("时间", "单号", "金额")) {
            cells.add(new ExcelCell(colIndex++, title, ExcelStyleFactory.tableTh()));
        }
        row.setCells(cells);
        rows.add(row);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        for (TradeStatement tradDetail : tradeDetails) {
            row = new ExcelRow(rowIndex++);
            colIndex = 0;
            cells = new ArrayList<>(6);
            //时间
            cells.add(new ExcelCell(colIndex++, sdf.format(tradDetail.getCreateTime()), ExcelStyleFactory.defStyle()));
            //单号
            cells.add(new ExcelCell(colIndex++, tradDetail.getOrderId(), ExcelStyleFactory.defStyle()));
            //金额
            cells.add(new ExcelCell(colIndex++, tradDetail.getAmount()/100.0+"", ExcelStyleFactory.defStyle()));
            row.setCells(cells);
            rows.add(row);
        }
        exportData.setRows(rows);
        exportFile.setData(exportData);
        return exportFile;
    }

}
