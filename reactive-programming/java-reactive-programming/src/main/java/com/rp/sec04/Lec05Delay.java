package com.rp.sec04;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;
import reactor.util.concurrent.Queues;

import java.time.Duration;

public class Lec05Delay {

    public static void main(String[] args) {

        System.setProperty("reactor.bufferSize.x", "10");

        // reactor.util.concurrent.Queues - Class defines Min 8 and Max 32 items as  default to emit when using delayElements
        // and its Max always which is 32. To change the behaviour,  System.setProperty("reactor.bufferSize.x", "9");
        Flux.range(1, 20)  // 32
                .log()
                .delayElements(Duration.ofSeconds(1))
                .subscribe(Util.subscriber());



        Util.sleepSeconds(60);
    }


}
