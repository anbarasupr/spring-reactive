package com.rp.sec09;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Lec05Group {

    public static void main(String[] args) {

        Flux.range(1, 30)
                .delayElements(Duration.ofSeconds(1))
                .groupBy(i -> i % 2)  // key 0, 1 based on whatever it returns, it is considered as a key to group and type return is GroupedFlux
                .subscribe(gf -> process(gf, gf.key())); // get GroupedFlux and do subscribe in process for every group


        Util.sleepSeconds(60);

    }

    private static void process(Flux<Integer> flux , int key){
        System.out.println("Called GroupedFlux:"+key);
        flux.subscribe(i -> System.out.println("Key : " + key + ", Item : " + i));
    }


}
