package org.triBhaskar;


import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
@SpringBootApplication
public class Main {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(Main.class, args);
//        String apiKey = "9dbda438d7mshcf49e93b573759ap1c05edjsna6b77678b309";
//        String apiHost = "coinranking1.p.rapidapi.com";
//        String apiUrl = "https://coinranking1.p.rapidapi.com/coins?referenceCurrencyUuid=yhjMzLPhuIDl&timePeriod=24h&orderBy=marketCap&orderDirection=desc&limit=50&offset=0";
//
//        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//            HttpGet request = new HttpGet(apiUrl);
//            request.addHeader("x-rapidapi-key", apiKey);
//            request.addHeader("x-rapidapi-host", apiHost);
//
//            try (CloseableHttpResponse response = httpClient.execute(request)) {
//                HttpEntity entity = response.getEntity();
//                if (entity != null) {
//                    String result = EntityUtils.toString(entity);
//                    System.out.println("Response status: " + response.getStatusLine().getStatusCode());
//                    System.out.println("Response body: " + result);
//                }
//            }
//        }
    }
}