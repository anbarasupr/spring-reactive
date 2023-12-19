package com.rp.sec04;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Lec06OnError {

    public static void main(String[] args) {
    	 errorReturn();
    	// errorResume();
//    	errorContinue();
    }

    private static Mono<Integer> fallback(){
        return Mono.fromSupplier(() -> Util.faker().random().nextInt(100, 200));
    }

    
    public static void errorReturn() {
        Flux.range(1, 10)
                .log()
                .map(i -> 10 / (5 - i))
                .onErrorReturn(-1) 
                .log()
                .subscribe(Util.subscriber());
    }
    
    public static void errorResume() {
        Flux.range(1, 10)
                .log()
                .map(i -> 10 / (5 - i))
                .onErrorResume(e -> fallback()) 
                .subscribe(Util.subscriber());
    }    
    
    public static void errorContinue() {
        Flux.range(1, 10)
                .log()
                .map(i -> 10 / (5 - i))
                .onErrorContinue((err, obj) -> {
                	throw new RuntimeException(err.getMessage()+", Rethrown from onErrorContinue");
                })
                .subscribe(Util.subscriber());
    }
}
