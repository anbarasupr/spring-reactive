package com.rp.sec06;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Lec07FluxInterval {

    public static void main(String[] args) {

//    	range();
    	interval();
    }

    private static void printThreadName(String msg){
        System.out.println(msg + "\t\t: Thread : " + Thread.currentThread().getName());
    }
    
    public static void range() {

        Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1))
                .log()
                .subscribe(v -> printThreadName("sub " + v));


        Util.sleepSeconds(60);
    }
    
    public static void interval() {

        Flux.interval(Duration.ofSeconds(1))
                .log()
                .subscribe(v -> printThreadName("sub " + v));


        Util.sleepSeconds(60);
    }
}
