package com.github.juhnowski.MeterVT;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import java.io.File;

public class WebClientRunnable implements Runnable {
    private WebClient.Builder webClientBuilder;
    private String filename;
    private String appUrl;

    public WebClientRunnable(WebClient.Builder webClientBuilder, String filename, String appUrl) {
        this.webClientBuilder = webClientBuilder;
        this.filename = filename;
        this.appUrl = appUrl;
    }

    public void run() {
        try {
        final WebClient webClient = webClientBuilder.build();
        webClient.patch()
                .uri(appUrl)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(fromFile(new File(filename))))
                .retrieve()
                .bodyToMono(String.class)   
                .block();               
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MultiValueMap<String, HttpEntity<?>> fromFile(File file) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", new FileSystemResource(file));
        return builder.build();
    }
}
