package client;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import joptsimple.internal.Strings;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: Forever丶诺
 * @date: 2018/6/9 14:39
 */
@Slf4j
public class Es84Test {

    /**
     * mGet 获取数据
     */
    @Test
    public void testMGet() {
        TransportClient esClient = EsClient.getEsClient();
        MultiGetResponse multiGetItemResponses = esClient.prepareMultiGet().add("car_shop", "cars",  Lists.newArrayList("1", "2")).get();
        for (MultiGetItemResponse multiResponses : multiGetItemResponses) {
            GetResponse response = multiResponses.getResponse();
            log.info(response.getSourceAsString());
        }
    }
}
