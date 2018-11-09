package com.gs.mall.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class OSSFileTemp {
    private Logger logger = LoggerFactory.getLogger(OSSFileTemp.class);

    @Value("${oss.gs.mall.key}")
    private  String mall_key;
    @Value("${oss.access.id}")
    private String ACCESS_ID;
    @Value("${oss.access.key}")
    private String ACCESS_KEY;
    @Value("${oss.bucketname.temp}")
    private String bucketName;
    @Value("${oss.ossurl.temp}")
    private String OSSURL;//下载地址
    @Value("${oss.endpoint}")
    private String ENDPOINT;//外网上传
    @Value("${oss.file.path}")
    private String filePath;//文件路径

    public OssResult uploadFileTemp(File fileTemp, String key) {
        if(null==fileTemp||null==key||""==key){
            throw new RuntimeException("上传文件和Key不能为空");
        }
        logger.info("********文件上传Temp开始*************");
        OSSClient client = new OSSClient(ENDPOINT, ACCESS_ID, ACCESS_KEY);
        key +="/"+new SimpleDateFormat("yyyyMMdd").format(new Date())+"/"+fileTemp.getName();
        long start = System.currentTimeMillis();
        OssResult result = new OssResult();
        InputStream input = null;
        try {
            input = new FileInputStream(fileTemp);
            client.putObject(bucketName, key, input);

            result.setSuccess(true);
            result.setKey(key);
            result.setUrl(OSSURL + key);

        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            return result;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (client != null) {
                client.shutdown();
            }
        }

        logger.info("********文件上传Temp结束*************");
        long end = System.currentTimeMillis();

        logger.info("一共耗时:" + (end - start) + "毫秒");
        return result;
    }


    /**
     * OSS文件下载
     *
     * @param key
     * @return
     */
    public File downloadFile(String key) {
        OSSClient client = new OSSClient(ENDPOINT, ACCESS_ID, ACCESS_KEY);
        if (null!=key&&""!=key) {
            return null;
        }
        //file/20170816/201708169agl8u.xlsx
        logger.info("************文件下载开始*****************");
        File targetFile = new File(filePath + "/" + key);
        try {
            long start = System.currentTimeMillis();

            if (!targetFile.exists()) {
                targetFile.getParentFile().mkdirs();
            }
            //OSS下载
            client.getObject(new GetObjectRequest(bucketName, key), targetFile);
            long end = System.currentTimeMillis();
            logger.info("一共耗时:" + (end - start) + "毫秒");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.shutdown();
            }
        }

        return targetFile;
    }


    /**
     * 删除OSS存放的图片
     *
     * @param key
     * @return
     */
    public boolean deleteOSSFile(String key) {
        OSSClient client = new OSSClient(ENDPOINT, ACCESS_ID, ACCESS_KEY);
        if (null!=key&&""!=key) {
            return false;
        }
        try {
            client.deleteObject(bucketName, key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.shutdown();
            }
        }
        // 关闭client
        logger.info("--------删除图片成功:" + key);
        return true;

    }

    public String mall_key() {
        return mall_key;
    }
}
