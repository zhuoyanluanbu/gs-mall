package com.gs.mall.export.common;

import java.util.Arrays;
import java.util.List;

/**
 * Created by chenchuan on 2017/9/1.
 */
public enum ExportType {
    /**
     * CSV
     */
    CSV("csv", ".csv"),
    /**
     * Excel
     */
    EXCEL("excel", ".xls"),
    /**
     * PDF
     */
    PDF("pdf", ".pdf"),
    /**
     * Word
     */
    WORD("word", ".docx"),
    /**
     *Text
     */
    TXT("txt", ".txt");

    private String code;
    private String msg;

    ExportType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String code() {
        return code;
    }

    public String msg() {
        return msg;
    }

    public List<ExportType> valueList() {
        return Arrays.asList(ExportType.values());
    }
}
