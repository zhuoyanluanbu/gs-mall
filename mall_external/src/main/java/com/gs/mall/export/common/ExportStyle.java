package com.gs.mall.export.common;

/**
 * Created by chenchuan on 2017/9/1.
 */
public class ExportStyle {

    protected String textAlign ="left";//center;left;right

    protected int fontSize=12;//字体大小

    protected String  font="Microsoft YaHei";//字体

    protected boolean bold=false;//加粗

    protected boolean border=false;//有无边框



    public String getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(String texrAlign) {
        this.textAlign = texrAlign;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public boolean isBorder() {
        return border;
    }

    public void setBorder(boolean border) {
        this.border = border;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }
}
