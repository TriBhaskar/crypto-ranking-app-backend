package org.triBhaskar.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Collections;

public class HttpUtils {
    @Value("${coin.api.host}")
    private static String apiHost;
    @Value("${coin.api.key}")
    private static String apiKey;

    public static HttpEntity<String> getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("accept", "application/json");
        headers.set("x-rapidapi-host", apiHost);
        headers.set("x-rapidapi-key", "9dbda438d7mshcf49e93b573759ap1c05edjsna6b77678b309");
        return new HttpEntity<>(headers);
    }
}
