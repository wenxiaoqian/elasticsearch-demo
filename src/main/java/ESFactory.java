import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.client.http.JestHttpClient;
import io.searchbox.indices.CreateIndex;
import org.elasticsearch.common.settings.Settings;

import java.io.IOException;

/**
 * @desc elasticsarch demo
 * @author xiaoqian
 */
public class ESFactory {

    private static JestClient client;

    private ESFactory() {

    }

    public synchronized static JestClient getClient() {
        if (client == null) {
            JestClientFactory factory = new JestClientFactory();
            factory.setHttpClientConfig(new HttpClientConfig
                    .Builder("http://localhost:9200")
                    .multiThreaded(true)
                    .build());
            client = factory.getObject();
        }
        return client;
    }

    public static void main(String[] args) throws IOException {
        JestClient client = ESFactory.getClient();

        client.execute(new CreateIndex.Builder("articles").build());

        String settings = "{\"settings\":{\"number_of_shards\":5,\"number_of_replicas\":1}}";

        //client.execute(new CreateIndex.Builder("articles")
        //        .settings(Settings.builder().loadFromSource(settings).build().getAsMap()).build());

        client.shutdownClient();
    }
}