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


@RestController
public class MeterVTController {
	@Autowired
    private WebClient.Builder webClientBuilder; 

    @GetMapping(value = "/start")
    public void upload() {
        final WebClient webClient = webClientBuilder.build();
        webClient.patch()
                .uri("http://127.0.0.1:8082/api/engagements")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(fromFile(new File("/home/ilya/Downloads/test_1.xlsx"))))
                .retrieve()
                .bodyToMono(String.class)   
                .block();   
    }

    public MultiValueMap<String, HttpEntity<?>> fromFile(File file) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", new FileSystemResource(file));
        return builder.build();
    }
}