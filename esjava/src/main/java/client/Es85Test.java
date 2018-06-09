package client;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;

import java.util.*;

/**
 * Bulk java Api 批量下载
 * @author: Forever丶诺
 * @date: 2018/6/9 15:17
 */
@Slf4j
public class Es85Test {

    @Test
    public void testBulk() {

        TransportClient client = EsClient.getEsClient();

        //创建bulkRequest
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();

        //分别准备 index 更新  创建 删除的 request
        Map  indexMap = new HashMap();
        indexMap.put("brand","奔驰");
        indexMap.put("name","奔驰C200");
        indexMap.put("produce_date","2017-01-05");
        indexMap.put("sale_price",340000);
        indexMap.put("sale_date","2017-02-03");
        IndexRequestBuilder indexRequestBuilder = client.prepareIndex("car_shop","sales").setSource(indexMap);
        bulkRequestBuilder.add(indexRequestBuilder);

        //index delete update  和 bulk 进行绑定
        bulkRequestBuilder.add(client.prepareDelete("car_shop","sales","1"));

        Map  updateMap = new HashMap();
        updateMap.put("sale_price",3500000);
        bulkRequestBuilder.add(client.prepareUpdate("car_shop","sales","2").setDoc(updateMap));


        //获取数据
        BulkResponse bulkItemResponses = bulkRequestBuilder.get();
        for (BulkItemResponse bulkItemRespons : bulkItemResponses) {
            DocWriteResponse response = bulkItemRespons.getResponse();
            log.info(response.getResult().getLowercase());
        }
    }

}
