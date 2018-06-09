package client;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author: Forever丶诺
 * @date: 2018/6/9 14:00
 */
public class EsClient {

    public static TransportClient getEsClient(){
        Settings settings = Settings.builder().put("client.transport.sniff", true).build();
        TransportClient client =null;
        try {
             client = new PreBuiltTransportClient(settings).addTransportAddress(new
                    InetSocketTransportAddress(InetAddress.getByName("localhost"),9300));

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }

}
