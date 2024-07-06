package org.triBhaskar.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Collections;

public class HttpUtils {
    @Value("${api.host}")
    private static String apiHost;
    @Value("${api.key}")
    private static String apiKey;

    public static HttpEntity<String> getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("accept", "application/json");
        headers.set("x-rapidapi-host", apiHost);
        headers.set("x-rapidapi-key", apiKey);
        return new HttpEntity<>(headers);
    }
}
