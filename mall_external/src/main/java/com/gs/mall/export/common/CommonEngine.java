package com.gs.mall.export.common;


import com.gs.mall.export.ExportEngine;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by chenchuan on 2017/9/1.
 */
public abstract class CommonEngine implements ExportEngine {
    @Override
    public void download(File file, HttpServletResponse response) throws IOException {
        response.reset();
        response.addHeader("Content-Disposition",
                "attachment;filename=" + new String((file.getName()).getBytes("UTF-8"), "ISO8859-1"));
        response.addHeader("Content-Length", "" + file.length());
        response.setContentType("application/octet-stream");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods","*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        OutputStream bo = new BufferedOutputStream(response.getOutputStream());
        InputStream fis = new BufferedInputStream(new FileInputStream(file));
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        bo.write(buffer);
        bo.flush();
        bo.close();
        file.delete();
    }
}
