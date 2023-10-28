package com.github.juhnowski.MeterVT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.List;
import java.util.stream.IntStream;

public class WebClientRunnable implements Runnable {
    private WebClient.Builder webClientBuilder;
    private String filename;

    public WebClientRunnable(WebClient.Builder webClientBuilder, String idx) {
        this.webClientBuilder = webClientBuilder;
        this.filename = "/home/ilya/Downloads/test_"+idx+".xlsx";
    }

    public void run() {
        try {
        final WebClient webClient = webClientBuilder.build();
        webClient.patch()
                .uri("http://127.0.0.1:8082/api/engagements")
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
