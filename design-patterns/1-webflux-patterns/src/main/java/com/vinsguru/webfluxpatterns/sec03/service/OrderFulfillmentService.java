package com.vinsguru.webfluxpatterns.sec03.service;

import com.vinsguru.webfluxpatterns.sec03.dto.OrchestrationRequestContext;
import com.vinsguru.webfluxpatterns.sec03.dto.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderFulfillmentService {

    @Autowired
    private List<Orchestrator> orchestrators;
    
    public Mono<OrchestrationRequestContext> placeOrder(OrchestrationRequestContext ctx){
        var list = orchestrators.stream() // List<Mono<OrchestrationRequestContext>>
	                   .map(o -> o.create(ctx)) // gives publishers to deduct payment, shipping & inventory
	                   .collect(Collectors.toList());
        return Mono.zip(list, a -> { 
        			System.out.println("all zip response: "+a.length);
	        		return a[0]; 
        		}) // subscribes to all deduct publishers above and get the response
                .cast(OrchestrationRequestContext.class)
                .doOnNext(this::updateStatus);
    }

    private void updateStatus(OrchestrationRequestContext ctx){
        var allSuccess = this.orchestrators.stream().allMatch(o -> o.isSuccess().test(ctx));
        var status = allSuccess ? Status.SUCCESS : Status.FAILED;
        ctx.setStatus(status);
    }

}
