package com.gs.mall.export.excel.items;

/**
 * Created by chenchuan on 2017/9/1.
 */
public class ExcelCell {
    /**  横向位置 **/
    private int offsetX=0;
    /**  Cells 内容 **/
    private String content;
    /**  合并单元格 **/
    private int colspan=-1;

    private ExcelStyle style;


    public ExcelCell(String content,int colspan) {
        setContent(content);
        setColspan(colspan);
    }

    public ExcelCell(String content, ExcelStyle style) {
        this.content = content;
        this.style = style;
    }
    public ExcelCell(int offsetX, String content,ExcelStyle style) {
        this.offsetX = offsetX;
        this.content = content;
        this.style = style;
    }
    public ExcelCell(int offsetX, String content, int colspan, ExcelStyle style) {
        this.offsetX = offsetX;
        this.content = content;
        this.colspan = colspan;
        this.style = style;
    }

    public ExcelStyle getStyle() {
        return style;
    }

    public void setStyle(ExcelStyle style) {
        this.style = style;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getColspan() {
        return colspan;
    }

    public void setColspan(int colspan) {
        this.colspan = colspan;
    }

    public int index(){
        return getOffsetX();
    }
    public  void index(int index){
        setOffsetX(index);
    }
}
