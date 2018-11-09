package com.gs.mall.export;


import com.gs.mall.export.common.ExportFile;
import com.gs.mall.export.common.ExportType;
import com.gs.mall.export.excel.ExcelFileAdapter;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * Created by chenchuan on 2017/9/1.
 */
public interface ExportEngine {
    /**
     * 创建要导出的文件
     */
    File createExportFile(ExportFile exportFile) throws Exception;

    /**
     * 组装要输出的数据
     */
    ExportFile genFile(Object data, ExcelFileAdapter fileAdapter);

    /**
     * 流写出文件
     */
    void download(File file, HttpServletResponse response) throws IOException;

    /**
     * 引擎类型
     */
    ExportType exportType();

    /**
     * 文件类型转换
     */
    ExportFile exchange(ExportFile fromFile);

}
