package com.gs.mall.params.adapter;

import com.gs.mall.enums.TradeTypeEnum;
import com.gs.mall.export.common.ExportFile;
import com.gs.mall.export.excel.ExcelFileAdapter;
import com.gs.mall.export.excel.items.ExcelCell;
import com.gs.mall.export.excel.items.ExcelData;
import com.gs.mall.export.excel.items.ExcelRow;
import com.gs.mall.export.factory.ExcelStyleFactory;
import com.gs.mall.export.impl.ExcelEngine;
import com.gs.mall.finance.po.TradeDetail;
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
public class TradeDetailDataAdapter implements ExcelFileAdapter {

    @Value("${oss.test.env}")
    Boolean testEnv;

    @Value("${oss.test.file.path}")
    String testFilePath;
    
    @Value("${export.file.path}")
    String exportFilePath;

    @Override
    public ExportFile excelFileAdapter(Object data) {
        List<TradeDetail> tradeDetails = (List<TradeDetail>) data;
        ExportFile exportFile = new ExportFile();
        exportFile.setType(ExcelEngine.FILE_TYPE);
        String filName = System.currentTimeMillis()+"";
        if( true ){
            File dir = new File(exportFilePath);
            if ( ! dir.exists() ) {
                dir.mkdirs();
            }
            filName = exportFilePath+"/"+filName;
        }
        exportFile.setName(filName);
        ExcelData exportData = new ExcelData();
        List<ExcelRow> rows = new ArrayList<>();
        ExcelRow row;
        int rowIndex = 0;
        row = new ExcelRow(rowIndex++);
        List<ExcelCell> cells = new ArrayList<>();
        int colIndex = 0;
        for (String title :
                Arrays.asList("创建时间", "名称", "流水号", "对方", "金额", "类型")) {
            cells.add(new ExcelCell(colIndex++, title, ExcelStyleFactory.tableTh()));
        }
        row.setCells(cells);
        rows.add(row);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (TradeDetail tradDetail:tradeDetails) {
            row = new ExcelRow(rowIndex++);
            colIndex =0;
            cells = new ArrayList<>(6);
            //创建时间
            cells.add(new ExcelCell(colIndex++, sdf.format(tradDetail.getCreateTime()), ExcelStyleFactory.defStyle()));
             //名称
            cells.add(new ExcelCell(colIndex++, tradDetail.getName(), ExcelStyleFactory.defStyle()));
             //流水
            cells.add(new ExcelCell(colIndex++, tradDetail.getTradeNo(), ExcelStyleFactory.defStyle()));
             //对方
            cells.add(new ExcelCell(colIndex++, TradeTypeEnum.INCOME.judge(tradDetail.getType())?tradDetail.getPayer():tradDetail.getPayee(), ExcelStyleFactory.defStyle()));
             //金额
            cells.add(new ExcelCell(colIndex++, tradDetail.getAmount()/100.0+"", ExcelStyleFactory.defStyle()));
             //类型
            cells.add(new ExcelCell(colIndex++, TradeTypeEnum.get(tradDetail.getType()), ExcelStyleFactory.defStyle()));
            row.setCells(cells);
            rows.add(row);
        }
        exportData.setRows(rows);
        exportFile.setData(exportData);
        return exportFile;
    }

}
