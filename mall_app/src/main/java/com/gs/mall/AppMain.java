package com.gs.mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 * app入口
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:12
 */
@SpringBootApplication
//@PropertySource(ignoreResourceNotFound = false,value = {
//	      "${out.properties}",
//	      "${out.db.properties}"
//	      })
@PropertySource(ignoreResourceNotFound = false,value = {
        "classpath:application-test.properties",
        "classpath:application-test-db.properties"
        })
public class AppMain {

    public static void main(String[] args)
    {
        SpringApplication.run(AppMain.class, args);
    }
}
