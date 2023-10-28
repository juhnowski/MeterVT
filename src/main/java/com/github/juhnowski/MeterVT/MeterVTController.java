package com.github.juhnowski.MeterVT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.stream.IntStream;


@RestController
public class MeterVTController {
	@Autowired
    private WebClient.Builder webClientBuilder; 

    @Value("${idx.from}")
    private Integer idxFrom;

    @Value("${idx.to}")
    private Integer idxTo;

    @Value("${app.url}")
    private String testUri;


    @GetMapping(value = "/start")
    public void upload() {
        IntStream.range(idxFrom, idxTo).parallel().forEach(i -> {
            try {
                Thread.ofVirtual().name(""+i).start(new WebClientRunnable(webClientBuilder, ""+i)).join();;    
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}