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
import com.gs.mall.order.po.WorkOrderTableDisplayData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by huyoucheng on 2018/11/12.
 */
@Component
public class WorkOrderFlowRecAdapter implements ExcelFileAdapter {

    @Value("${oss.test.env}")
    Boolean testEnv;

    @Value("${oss.test.file.path}")
    String testFilePath;

    @Value("${export.file.path}")
    String exportFilePath;

    @Override
    public ExportFile excelFileAdapter(Object data) {
        List<WorkOrderTableDisplayData>  wofr = (List<WorkOrderTableDisplayData>) data;
        ExportFile exportFile = new ExportFile();
        exportFile.setType(ExcelEngine.FILE_TYPE);
        String filName = "WorkOrder_"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
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
                Arrays.asList("工单编号", "客户信息", "日志内容", "日志时间", "操作人")) {
            cells.add(new ExcelCell(colIndex++, title, ExcelStyleFactory.tableTh()));
        }
        row.setCells(cells);
        rows.add(row);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (WorkOrderTableDisplayData w:wofr) {
            row = new ExcelRow(rowIndex++);
            colIndex =0;
            cells = new ArrayList<>(5);
            cells.add(new ExcelCell(colIndex++,w.getWo_id(),ExcelStyleFactory.defStyle()));
            cells.add(new ExcelCell(colIndex++,
                    (w.getCustomerName()+"（"+w.getCustomerTel()+"）").equals("（）")?"":(w.getCustomerName()+"（"+w.getCustomerTel()+"）"),
                    ExcelStyleFactory.defStyle()));
            cells.add(new ExcelCell(colIndex++,w.getDesc(),ExcelStyleFactory.defStyle()));
            cells.add(new ExcelCell(colIndex++,w.getCreate_time()!=null?sdf.format(w.getCreate_time()):"",ExcelStyleFactory.defStyle()));
            cells.add(new ExcelCell(colIndex++,w.getOperator()+"（"+w.getOperator_id()+"）",ExcelStyleFactory.defStyle()));
            row.setCells(cells);
            rows.add(row);
        }
        exportData.setRows(rows);
        exportFile.setData(exportData);
        return exportFile;
    }
}
