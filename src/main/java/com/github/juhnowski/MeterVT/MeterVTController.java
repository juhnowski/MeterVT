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


@RestController
public class MeterVTController {
	@Autowired
    private WebClient.Builder webClientBuilder; 

    @GetMapping(value = "/start")
    public void upload() {
        IntStream.range(1, 1001).parallel().forEach(i -> {
            try {
                Thread.ofVirtual().name(""+i).start(new WebClientRunnable(webClientBuilder, ""+i)).join();;    
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}