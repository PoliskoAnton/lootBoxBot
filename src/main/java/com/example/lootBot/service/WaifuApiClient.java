package com.example.lootBot.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class WaifuApiClient {

    private final RestTemplate restTemplate;
    private final String apiUrl;

    public WaifuApiClient(@Value("${waifu.api}") String apiUrl) {
        this.restTemplate = new RestTemplate();
        this.apiUrl = apiUrl;
    }

    public String getRandomImage() {
        Map<String, String> response = restTemplate.getForObject(apiUrl, Map.class);
        return response.get("url");
    }
}
