package com.gs.mall.export.common;

/**
 * Created by chenchuan on 2017/9/1.
 */
public class ExportFile {
    String name;

    ExportData data;

    ExportType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExportData getData() {
        return data;
    }

    public void setData(ExportData data) {
        this.data = data;
    }

    public ExportType getType() {
        return type;
    }

    public void setType(ExportType type) {
        this.type = type;
    }

    public String  fileName(){
       return name + type.msg();
    }
}
