package client;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author: Forever丶诺
 * @date: 2018/6/9 14:04
 */
@Slf4j
public class Es83Test {


    @Test
    public void testUpsert() throws IOException, ExecutionException, InterruptedException {
        TransportClient esClient = EsClient.getEsClient();

        IndexRequest indexRequest = new IndexRequest("car_shop","cars","1").source(XContentFactory.jsonBuilder()
                .startObject().field("brand","宝马").field("name","宝马320").field("price",320000)
                .field("produce_date","2017-02-03").endObject());

        UpdateRequest updateRequest = new UpdateRequest("car_shop","cars","1").doc(XContentFactory.jsonBuilder()
                .startObject().field("price",310000).endObject()).upsert(indexRequest);

        UpdateResponse updateResponse = esClient.update(updateRequest).get();
        log.info(updateResponse.toString());
    }
}
