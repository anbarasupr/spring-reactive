package com.rp.sec09;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Lec02OverlapAndDrop {

    public static void main(String[] args) {

        eventStream()
//        		.buffer(3)
//                 .buffer(3, 1) //Overlap and Drop
//                .buffer(Duration.ofSeconds(2))
                .bufferTimeout(5, Duration.ofSeconds(2))
                .subscribe(Util.subscriber());

        Util.sleepSeconds(60);

    }


    private static Flux<String> eventStream(){
        return Flux.interval(Duration.ofMillis(300))
                .map(i -> "event"+i);
    }


}
