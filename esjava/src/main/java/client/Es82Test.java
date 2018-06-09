package client;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * @author: Forever丶诺
 * @date: 2018/6/9 11:37
 */
@Slf4j
public class Es82Test {

    @Test
    public void test() throws Exception {
        Settings settings = Settings.builder().build();
        TransportClient client = new PreBuiltTransportClient(settings).addTransportAddress(new
                InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        GetResponse response = client.prepareGet("es1", "type", "1").execute().actionGet();
        Map<String, Object> sourceAsMap = response.getSourceAsMap();
        client.close();
        log.info(sourceAsMap.toString());
    }

    /**
     * 探查节点
     */
    @Test
    public void testSniffNode() throws UnknownHostException {
        //设置的时候 自动探查节点
        Settings settings = Settings.builder().put("client.transport.sniff", true).build();

        //创建连接
        TransportClient client = new PreBuiltTransportClient(settings).addTransportAddress(new
                InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        GetResponse response = client.prepareGet("es1", "type", "1").execute().actionGet();
        Map<String, Object> source = response.getSourceAsMap();
        client.close();
        log.info(source.toString());
    }

}
