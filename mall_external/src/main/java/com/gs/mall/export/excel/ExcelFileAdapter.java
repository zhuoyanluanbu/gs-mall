package com.gs.mall.export.excel;

import com.gs.mall.export.common.ExportFile;

/**
 * Created by chenchuan on 2017/9/1.
 */
public interface ExcelFileAdapter {
    ExportFile excelFileAdapter(Object data);
}
