package com.rp.sec09;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Lec01Buffer {

    public static void main(String[] args) {

        // eventStream()
        takeEventStream()
        		// .buffer(5) //collect 5 items and give back
//        		.buffer(Duration.ofSeconds(2)) // collect the items for the 2 seconds window time and give back
                 .bufferTimeout(5, Duration.ofSeconds(2)) 
        		// Either collect 5 items or whatever the items that it got in the window time and give back. whichever finish first, it will take precedence either window time or no of items
                .subscribe(Util.subscriber());

        Util.sleepSeconds(60);

    }


    private static Flux<String> eventStream(){
        return Flux.interval(Duration.ofMillis(800))
                    .map(i -> "event"+i);
    }


    private static Flux<String> takeEventStream(){
        return Flux.interval(Duration.ofMillis(800))
                    .map(i -> "event"+i)
                    .take(3);
    }
}
