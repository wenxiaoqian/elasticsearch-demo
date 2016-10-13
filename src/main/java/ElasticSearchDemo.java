import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by WXQ on 2016/10/13.
 */
public class ElasticSearchDemo {

    public static void main(String[] args) throws IOException {
        String json = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("user","kimchy");
        jsonMap.put("postDate",new Date());
        jsonMap.put("message","trying out Elasticsearch");

        // instance a json mapper
        ObjectMapper mapper = new ObjectMapper(); // create once, reuse

        // generate json
        byte[] jsonByte = mapper.writeValueAsBytes(jsonMap);

        XContentBuilder builder = jsonBuilder().startObject()
                .field("user", "kimchy")
                .field("postDate", new Date())
                .field("message", "trying out Elasticsearch")
                .endObject();

        String str = builder.string();

        Client client = TransportClient.builder().build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        IndexResponse response = client.prepareIndex("twitter", "tweet", "1")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("user", "kimchy")
                        .field("postDate", new Date())
                        .field("message", "trying out Elasticsearch")
                        .endObject()
                ).get();
    }

}
