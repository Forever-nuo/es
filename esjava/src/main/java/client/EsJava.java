package client;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * @author: Forever丶诺
 * @date: 2018/6/6 17:44
 */
public class EsJava {

    public static void main(String[] args) {

        Settings settings = Settings.builder().put("cluster.name", "elasticsearch")
                .put("client.transport.sniff",true)
                .build();
        try {
            TransportClient client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
            GetResponse response = client.prepareGet("es1","type","1").execute().actionGet();
            String sourceAsString = response.getSourceAsString();
            Map<String, Object> sourceAsMap = response.getSourceAsMap();
            System.out.println(sourceAsMap);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
