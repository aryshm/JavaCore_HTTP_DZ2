import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static final String REMOTE_SERVICE_URI =
            "https://api.nasa.gov/planetary/apod?api_key=yt3gbfeiuDbJHNI151M5GWXkuEkwBYNDwshebom3";
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet(REMOTE_SERVICE_URI);
        CloseableHttpResponse response = httpClient.execute(request);
        Nasa nasa = mapper.readValue(response.getEntity().getContent(), Nasa.class);
        String image = nasa.getUrl();
        request = new HttpGet(image);
        response = httpClient.execute(request);
        String[] arr = nasa.getUrl().split("/");
        String fileName = arr[6];
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            FileOutputStream fos = new FileOutputStream(fileName);
            entity.writeTo(fos);
            fos.close();
        }
        response.close();
        httpClient.close();
    }
}
