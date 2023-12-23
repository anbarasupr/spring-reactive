package com.rp.sec02;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Lec09FluxFromMono {

    public static void main(String[] args) {

        Mono<String> mono = Mono.just("a");
        Flux<String> flux = Flux.from(mono);
        flux.subscribe(Util.onNext(), Util.onError(), Util.onComplete());

        System.out.println("-----");

        Flux.range(1, 10)
                .next() // 1 (Emit only the first item emitted by this Flux, into a new Mono)
                .log()
                .filter(i -> i > 3)
                .subscribe(Util.onNext(), Util.onError(), Util.onComplete());

        System.out.println("-----");
        Util.sleepSeconds(3);

    }

    private static void doSomething(Flux<String> flux){

    }

}
