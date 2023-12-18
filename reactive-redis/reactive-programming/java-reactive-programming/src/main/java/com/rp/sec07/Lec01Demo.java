package com.rp.sec07;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Lec01Demo {

	//backpressure test
    public static void main(String[] args) {


        Flux.create(fluxSink -> {
            for (int i = 1; i < 501; i++) {
                fluxSink.next(i);
                System.out.println("Pushed : " + i);
            }
            fluxSink.complete();
        })
        .publishOn(Schedulers.boundedElastic())
        .doOnNext(i -> {
        	// consumer waiting for 10 ms, meanwhile publisher produced all 500 items which is retained in the memory
        	// until subscriber started consuming which is not good if the objects are huge 
            Util.sleepMillis(10); 
        })
        .subscribe(Util.subscriber());


        Util.sleepSeconds(60);

    }

}
