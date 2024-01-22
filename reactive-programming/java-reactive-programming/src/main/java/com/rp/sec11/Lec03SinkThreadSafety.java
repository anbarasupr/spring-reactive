package com.rp.sec11;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.EmitResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;

public class Lec03SinkThreadSafety {

    public static void main(String[] args) {


        // handle through which we would push items
        Sinks.Many<Object> sink = Sinks.many().unicast().onBackpressureBuffer();

        // handle through which subscribers will receive items
        Flux<Object> flux = sink.asFlux();
        List<Object> list = new ArrayList<>();
        Vector<Object> vector = new Vector<>();
//        flux.subscribe(list::add);
        flux.subscribe(vector::add);
//        for (int i = 0; i < 1000; i++) {
//            final int j = i;
//            CompletableFuture.runAsync(() -> {
//               EmitResult emitResult= sink.tryEmitNext(j); 
//            });
//        }

        
        //below is thread safe and handle callback with retry if any failure
        for (int i = 0; i < 1000; i++) {
            final int j = i;
            CompletableFuture.runAsync(() -> {
                sink.emitNext(j, (signalType, emitResult) -> true);
            });
        }

        Util.sleepSeconds(3);
        System.out.println(list.size());
        System.out.println(list);
        
        System.out.println(vector.size());
        System.out.println(vector);


    }

}
