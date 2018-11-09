package com.gs.mall.export.excel.items;


import com.gs.mall.export.common.ExportStyle;

/**
 * Created by chenchuan on 2017/9/1.
 */
public class ExcelStyle extends ExportStyle {
    public ExcelStyle() {
        font="Microsoft YaHei";
    }

    public ExcelStyle(String textAlign) {
        this.textAlign = textAlign;
    }

    public ExcelStyle(String textAlign, int fontSize) {
        this.textAlign = textAlign;
        this.fontSize = fontSize;
    }

    public ExcelStyle(String textAlign, int fontSize, String font) {
        this.textAlign = textAlign;
        this.fontSize = fontSize;
        this.font = font;
    }

    public ExcelStyle(String textAlign, int fontSize, String font, boolean border) {
        this.textAlign = textAlign;
        this.fontSize = fontSize;
        this.font = font;
        this.border = border;
    }
}
