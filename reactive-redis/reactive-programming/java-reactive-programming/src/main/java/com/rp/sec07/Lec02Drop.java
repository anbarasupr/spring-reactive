package com.rp.sec07;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;


public class Lec02Drop {

    public static void main(String[] args) {
        // 75% of 16 items is 12. When 75% items(12) in the queue is drained, the queue will accept another 12 items, Once the queue is full, new items will be dropped
        System.setProperty("reactor.bufferSize.small", "16"); //Default value is 256 in Queue interface

        List<Object> list = new ArrayList<>();

        Flux.create(fluxSink -> {
            for (int i = 1; i < 201; i++) {
                fluxSink.next(i);
                System.out.println("Pushed : " + i);
                Util.sleepMillis(1);
            }
            fluxSink.complete();
        })
        // .onBackpressureDrop(list::add) // we can push the dropping data to another publisher or any
        .onBackpressureDrop(list::add) //Once the queue is full, new items will be dropped
        .publishOn(Schedulers.boundedElastic())
        .doOnNext(i -> {
            Util.sleepMillis(10);
        })
        .subscribe(Util.subscriber());


        Util.sleepSeconds(10);
        System.out.println(list); //list contains dropped data by back pressure

    }


}
