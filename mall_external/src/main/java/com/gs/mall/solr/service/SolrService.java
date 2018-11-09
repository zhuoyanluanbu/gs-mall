package com.gs.mall.solr.service;

import com.gs.mall.enums.SolrEnum;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * solr服务
 * Created by huangyp on 2017/12/27.
 */
public class SolrService {

    private static final Logger logger = LoggerFactory.getLogger(SolrService.class);

    private String uniqueKey;
    private SolrClient client = null;
    private String server;

    public SolrService(String server, SolrEnum e) {
        String coreName = null;
        switch (e.getValue()) {
            case 1:
                coreName = "commodity";
                this.uniqueKey = "commodityId";
                break;
            default:
                break;
        }
        this.server = server + coreName;
        this.client = new HttpSolrClient.Builder(this.server).build();
        HttpSolrClient clientSolr = null;


    }

    public String getUniqueKey(){
        return this.uniqueKey;
    }

    /**
     * solor分词
     * @param rid
     * @param doc
     * @return
     */
    public int segment(String rid, SolrInputDocument doc) {
        int status = -1;
        try {
            client.add(doc);
            UpdateResponse updateResponse = client.commit();
            status = updateResponse.getStatus();
            logger.debug("push>doc=" + doc + " status=" + status);
            //resp = new Resp(UuidUtils.uuid32(),updateResponse.getStatus(),now,"");
        } catch (Exception e) {
            logger.error("solr-添加分词异常", e);
            //resp = new Resp(UuidUtils.uuid32(),-1,now,"");
        }
        return status;
    }

    /**
     * 批量更新solr
     *
     * @param doc
     * @param uniqueKeys
     * @return
     */
    public int update(SolrInputDocument doc, Object[] uniqueKeys) {
        if (uniqueKeys == null && uniqueKeys.length == 0) {
            logger.debug("push>list.size=0");
            return 0;
        }
        int status = -1;
        //long now = System.currentTimeMillis();
        try {
            List<SolrInputDocument> list = new LinkedList<SolrInputDocument>();
            Set<String> keys = doc.keySet();
            for (Object uniqueKey : uniqueKeys) {
                SolrInputDocument d = new SolrInputDocument();
                d.setField(this.uniqueKey, uniqueKey);
                for (String key : keys) {
                    Map<String, String> operation = new HashMap<String, String>();
                    operation.put("set", doc.getFieldValue(key).toString());
                    d.setField(key, operation);
                }
                list.add(d);
            }

            client.add(list);
            UpdateResponse updateResponse = client.commit();
            status = updateResponse.getStatus();

            logger.debug("push>list=" + list + " status=" + status);
            //resp = new Resp(UuidUtils.uuid32(),updateResponse.getStatus(),now,"");
        } catch (Exception e) {
            logger.error("solr-更新分词异常", e);
        }
        return status;
    }

    public SolrDocumentList query(int pageIndex, int pageSize, String querySql, Map<String, String> otherFields)  {
        int start = pageIndex == 0 ? 0 : ((pageIndex * pageSize) + 1);
        try {
            SolrQuery query = new SolrQuery();
            query.setStart(start);
            query.setRows(pageSize);
            logger.info("solr query querySql=" + querySql);
            query.setQuery(querySql);

            if (otherFields != null) {
                Set<Map.Entry<String, String>> set = otherFields.entrySet();
                Iterator<Map.Entry<String, String>> iter = set.iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, String> me = iter.next();
                    query.set(me.getKey(), me.getValue());
                }
            }
            logger.info("solr query toQueryString" + query.toQueryString());
            logger.info("solr query ###queryStr=" + query.toQueryString());
            QueryResponse QueryResponse = client.query(query);
            logger.info("solr query QueryResponse-->" + QueryResponse);
            if (QueryResponse.getStatus() == 0) {
                SolrDocumentList docList = QueryResponse.getResults();
                return docList;
            }
        } catch (Exception e) {
            logger.error("solr-查询异常", e);
        }
        return null;
    }
}
