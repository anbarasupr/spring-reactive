package com.rp.sec09;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class Lec04Window {

    private static AtomicInteger atomicInteger = new AtomicInteger(1);

    public static void main(String[] args) {

        eventStream()
//        		.window(Duration.ofSeconds(1))
                 .window(3) //as and when something comes, it is given to next pipeline until window time instead of collecting a list and giving after like buffer
                //.flatMap(flux -> saveEvents(flux))
                .flatMap(flux -> saveEventsFluxReturn(flux))
                .subscribe(Util.subscriber());

        Util.sleepSeconds(60);

    }

    private static Flux<String> eventStream(){
        return Flux.interval(Duration.ofMillis(500))
                .map(i -> "event"+i);
    }

    private static Mono<Integer> saveEvents(Flux<String> flux){
        return flux
                    .doOnNext(e -> System.out.println("saving " + e))
                    .doOnComplete(() -> {
                        System.out.println("saved this batch");
                        System.out.println("-------------------");
                    })
                    .then(Mono.just(atomicInteger.getAndIncrement())); // calculate no of batches
    }
    
    private static Flux<String> saveEventsFluxReturn(Flux<String> flux){
        return flux
                    .doOnNext(e -> System.out.println("saving " + e))
                    .doOnComplete(() -> {
                        System.out.println("saved this batch");
                        System.out.println("-------------------");
                    });
     }

}
