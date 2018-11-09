package com.gs.mall.common.constant;

/**
 * 系统常量
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:12
 */
public interface Constant {

    /**
     * 商户在缓存中的key
     */
    String MERCHANT_SESSION_KEY = "Merchant_Info_Session";

    /**
     * 商户缓存时间(秒)
     */
    int MERCHANT_CACHE_TIME =  8 * 60 * 60;
    //private static final int MERCHANT_CACHE_TIME  = 8 * 60 * 60;

    /**
     * 操作人缓存中的key
     */
    String OPERATOR_SESSION_KEY = "Operator_Info_Session";

    /**
     * APP user 缓存中的key
     */
    String APP_USER_SESSION_KEY = "APP_USER_Info_Session";

    /**
     * APP用户前缀
     */
    String APP_USER_CACHE_PREFIX = "GS_Mall_AppUser_";

    /**
     * APP用户缓存时间(秒)
     */
    int APP_USER_CACHE_EXPIRE = 2 * 60 * 60;


    /**
     * 无规格商品sku
     */
    String SKU_NO_ATTRIBUTE = "-";

    /**
     * 财务数据缓存
     */
    String FINANCE_PANEL_CACHE_KEY = "Finance_Panel_Data_Cache";

    /**
     * 搜索配置key
     */
    String SEARCH_WORD ="Search_Word";
    /**
     * 默认分类Id
     */
    Long DEFAULT_CATEGORY = -1l;
    /**
     * 默认分类名称
     */
    String DEFAULT_CATEGORY_NAME= "默认分类";
    /**
     * 未处理工单数缓存
     */
    String GS_MALL_WORKORDER_NUM="GS_MALL_WORKORDER_NUM";
    /**
     * 待发货订单数缓存
     */
    String GS_MALL_WAITDELIVERY_NUM="GS_MALL_WAITDELIVERY_NUM";


    ///** 自营商城库存处理topic*/
    //String TOPIC_JFMALL_INVENTORY_UPDATE_TOPIC = "topic_jfmall_";
    /**
     * 自营商城订单关闭topic
     */
    String TOPIC_MALL_ORDER_CLOSE = "topic_mall_order_close";
    /**
     * 自营商城订单完成topic
     */
    String TOPIC_MALL_ORDER_FINISH = "topic_mall_order_finish";
    
    /**
     * 自营商城每日销售统计TOPIC
     */
    String TOPIC_OPERATIONAL_REPORT="topic_operational_report";
    // ===========================API Config============================

    /**
     * ： APPID
     */
    String appID = "B20748010B5E4F808E36AB9FD518C443";//"F41D24A807064C0DB3A46C16B16EA252";

    //默认 商家名称
    String appName = "固守供应链管理有限公司";

    //默认 商家ID
    long merchantId = 1;
    /**
     * appSecretKey
     */
    String appSecretKey = "GS20170823135216202APYYZyrY";//"GS20170712160936594qETaFbt8";
    /**
     *  APP私钥
     */
    //String appPrivateSecret = "MIICXAIBAAKBgQDXx6/4Qt7S1lJspybD0xIlPVZc/ZQVMdHsPYC1Dy1f6SmYcytO4DYe29qEix7QAw6Ye6Nj7+CamF7fl6YvIoFu9inskIAl5Sk/u3QRreglATJ4piV+7KhNMc+2e2DlLFbrgo3sNqRaRIpH4XUrQdzL96H1V9YOigODjNMTzQ5GCwIDAQABAoGAB9vzEdTFruI1gFWaONmwOIECa0KlKRx2a+dVj8KswWiJpk7Tjfzmm7sgO2Ghzqu91VnzeolbIeZY1kX8vIzCMApOqU9yuTS8HSvTuZrFJniCTYb4jc0RAHz/esi3+Y3KTM9NmQdhu9jcr3VEsA+g6OmncJKpqAuLvjnnXRf3GOkCQQD0xsXWthlfpPX8BVHhxxxTd1KgcjAtUkM6Qe6SKN/N4JeCSs2N4zEvoJGWFBX8nt+EGHnKk1Df27xqXEL3OYulAkEA4ayMtNosUy7t18n4em7DN/9apscQzIx2EpVrOlLpMCWdFEYWS8vcVfSCf7NOgbuPTMhpoWYOgfXTLynP8Sib7wJASR5rkNT6xWholq0cJZ6t5nz5cLZLj+5H6MozMvQEzuItQB2KQ70/PyPu+W7slnb6++G31/yjpwtjwD3pPyKwhQJAF7YvmpkJ6zuVFgVMrGCS9oFIHxnvc+6WQWGdwKURh1OMa6LpmNE0t3tXct9+Mjdo2SJ9ps24XINdyyjThn1D1QJBAKMisfe8NY3T4cNpmE3QvkGXSTml4ufZuOOff2uCQmY+QGJ7YoH9j/15h9aD2Cniyt3A87fOuT/sdZzVZspaveg=";
    String appPrivateSecret = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANfHr/hC3tLWUmynJsPTEiU9Vlz9lBUx0ew9gLUPLV/pKZhzK07gNh7b2oSLHtADDph7o2Pv4JqYXt+Xpi8igW72KeyQgCXlKT+7dBGt6CUBMnimJX7sqE0xz7Z7YOUsVuuCjew2pFpEikfhdStB3Mv3ofVX1g6KA4OM0xPNDkYLAgMBAAECgYAH2/MR1MWu4jWAVZo42bA4gQJrQqUpHHZr51WPwqzBaImmTtON/OabuyA7YaHOq73VWfN6iVsh5ljWRfy8jMIwCk6pT3K5NLwdK9O5msUmeIJNhviNzREAfP96yLf5jcpMz02ZB2G72NyvdUSwD6Do6adwkqmoC4u+OeddF/cY6QJBAPTGxda2GV+k9fwFUeHHHFN3UqByMC1SQzpB7pIo383gl4JKzY3jMS+gkZYUFfye34QYecqTUN/bvGpcQvc5i6UCQQDhrIy02ixTLu3Xyfh6bsM3/1qmxxDMjHYSlWs6UukwJZ0URhZLy9xV9IJ/s06Bu49MyGmhZg6B9dMvKc/xKJvvAkBJHmuQ1PrFaGiWrRwlnq3mfPlwtkuP7kfoyjMy9ATO4i1AHYpDvT8/I+75buyWdvr74bfX/KOnC2PAPek/IrCFAkAXti+amQnrO5UWBUysYJL2gUgfGe9z7pZBYZ3ApRGHU4xroumY0TS3e1dy334yN2jZIn2mzbhcg13LKNOGfUPVAkEAoyKx97w1jdPhw2mYTdC+QZdJOaXi59m4459/a4JCZj5AYntigf2P/XmH1oPYKeLK3cDzt865P+x1nNVmylq96A==";

    /**
     *  APP公钥
     */
    String appPublicSecret = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDXx6/4Qt7S1lJspybD0xIlPVZc/ZQVMdHsPYC1Dy1f6SmYcytO4DYe29qEix7QAw6Ye6Nj7+CamF7fl6YvIoFu9inskIAl5Sk/u3QRreglATJ4piV+7KhNMc+2e2DlLFbrgo3sNqRaRIpH4XUrQdzL96H1V9YOigODjNMTzQ5GCwIDAQAB";
    //"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDXx6/4Qt7S1lJspybD0xIlPVZc/ZQVMdHsPYC1Dy1f6SmYcytO4DYe29qEix7QAw6Ye6Nj7+CamF7fl6YvIoFu9inskIAl5Sk/u3QRreglATJ4piV+7KhNMc+2e2DlLFbrgo3sNqRaRIpH4XUrQdzL96H1V9YOigODjNMTzQ5GCwIDAQAB";

    /**
     *   平台公钥
     */
    String openPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCMfUp+MDaEmOQOGKOUWgRXs/2ryS9Q/YQyH+CIjBO1EIz1P3C9jOMA5Q9x5P0xHB/NOl+GGuUtcQaYIkEsk4h3ZkZ/uBXpQmS3DLpdNsdWb0G64PLf33GhAvPKc+yRkp8av8pz+X2WbfjtVZfavw5k8TPRMXKgWCPdgznm7jbVewIDAQAB";

}
