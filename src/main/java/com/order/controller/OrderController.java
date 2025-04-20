package com.order.controller;

import com.order.model.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private WebClient.Builder webClientBuilder;

    private final RestTemplate restTemplate =  new RestTemplate();

    @GetMapping("/")
    public String homeApi(){
        return "Order Service Working";
    }

    @PostMapping("/changeStatus")
    public String changeOrderStatus(@RequestBody OrderStatus status){
        System.out.println(status.toString());
        String message = restTemplate.postForObject("http://localhost:8082/notify/send", status, String.class);
        System.out.println(message);
        return "Order Status Change Received";
    }

    @PostMapping("/modifyStatus")
    public String changeOrderStatusWithWebClient(@RequestBody OrderStatus status){
        System.out.println(status.toString());
        webClientBuilder.build().post()
                .uri("http://localhost:8082/notify/send")
                .bodyValue(status)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(message -> System.out.println("Message:"+message));
        return "Order Status Change Received";
    }

}
