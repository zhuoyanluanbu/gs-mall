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
import com.gs.mall.order.po.WorkOrderFlowRec;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by huyoucheng on 2018/11/12.
 */
public class WorkOrderFlowRecAdapter implements ExcelFileAdapter {

    @Value("${oss.test.env}")
    Boolean testEnv;

    @Value("${oss.test.file.path}")
    String testFilePath;

    @Value("${export.file.path}")
    String exportFilePath;

    @Override
    public ExportFile excelFileAdapter(Object data) {
        List<WorkOrderFlowRec>  wofr = (List<WorkOrderFlowRec>) data;
        ExportFile exportFile = new ExportFile();
        exportFile.setType(ExcelEngine.FILE_TYPE);
        String filName = "工单"+System.currentTimeMillis();
        File dir = new File(exportFilePath);
        if ( ! dir.exists() ) {
            dir.mkdirs();
        }
        filName = exportFilePath+"/"+filName;
        exportFile.setName(filName);
        ExcelData exportData = new ExcelData();
        List<ExcelRow> rows = new ArrayList<>();
        ExcelRow row;
        int rowIndex = 0;
        row = new ExcelRow(rowIndex++);
        List<ExcelCell> cells = new ArrayList<>();
        int colIndex = 0;
        for (String title :
                Arrays.asList("状态", "申请时间", "商品订单号", "退款金额", "退款申请说明", "申请发起人","退款操作人")) {
            cells.add(new ExcelCell(colIndex++, title, ExcelStyleFactory.tableTh()));
        }
        row.setCells(cells);
        rows.add(row);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (WorkOrderFlowRec w:wofr) {
            row = new ExcelRow(rowIndex++);
            colIndex =0;
            cells = new ArrayList<>(7);
            //状态
            cells.add(new ExcelCell(colIndex++, sdf.format(tradDetail.getCreateTime()), ExcelStyleFactory.defStyle()));
            //申请时间
            cells.add(new ExcelCell(colIndex++, null, ExcelStyleFactory.defStyle()));
            //商品订单编号
            cells.add(new ExcelCell(colIndex++, null, ExcelStyleFactory.defStyle()));
            //退款金额
            cells.add(new ExcelCell(colIndex++, null, ExcelStyleFactory.defStyle()));
            //退款申请说明
            cells.add(new ExcelCell(colIndex++, null, ExcelStyleFactory.defStyle()));
            //申请发起人
            cells.add(new ExcelCell(colIndex++, null, ExcelStyleFactory.defStyle()));
            //操作人
            cells.add(new ExcelCell(colIndex++,null,ExcelStyleFactory.defStyle()));
            row.setCells(cells);
            rows.add(row);
        }
        exportData.setRows(rows);
        exportFile.setData(exportData);
        return exportFile;
        return null;
    }
}
