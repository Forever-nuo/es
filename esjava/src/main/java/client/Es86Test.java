package client;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;

/**
 * scroll 实现批量下载
 *
 * @author: Forever丶诺
 * @date: 2018/6/9 16:20
 */
@Slf4j
public class Es86Test {

    @Test
    public void testScroll() {

        TransportClient client = EsClient.getEsClient();

        //第一次请求  得到返回对象
        SearchResponse response = client.prepareSearch("car_shop").setTypes("sales").setQuery(QueryBuilders.termQuery
                ("brand.keyword", "宝马1")).setScroll(TimeValue.timeValueMinutes(1)).setSize(1).get();

        do {

            SearchHits hits = response.getHits();
            for (SearchHit hit : hits) {
                String sourceAsString = hit.getSourceAsString();
                log.info(sourceAsString);
            }

            // 获取 scrollId 进行下次的查询
            response = client.prepareSearchScroll(response.getScrollId()).setScroll(TimeValue.timeValueMinutes(1))
                    .execute().actionGet();

        } while (response.getHits().getHits().length != 0);

    }

}
