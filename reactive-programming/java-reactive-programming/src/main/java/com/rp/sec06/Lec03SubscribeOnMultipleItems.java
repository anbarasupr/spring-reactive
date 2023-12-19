package com.rp.sec06;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Lec03SubscribeOnMultipleItems {

    public static void main(String[] args) {

        Flux<Object> flux = Flux.create(fluxSink -> {
            printThreadName("create");
            for (int i = 0; i < 4; i++) {
                fluxSink.next(i);
                Util.sleepSeconds(1);
            }
            fluxSink.complete();
        })
        .doOnNext(i -> printThreadName("next " + i));

 		
 		// Single subscriber that runs in a single thread from boundedElastic thread pool
        flux
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe(v -> printThreadName("sub " + v));
     
        
       
        
/*       Runnable runnable = ()->  flux
            .subscribeOn(Schedulers.boundedElastic())
            .subscribe(v -> printThreadName("sub " + v));
       // Multiple subscribers that runs in a separate thread from boundedElastic thread pool.
       // Each subscribers that runs in a single thread from boundedElastic thread pool
       for (int i = 0; i < 2; i++) {
           new Thread(runnable).start();
       }
*/
        
/*      
		flux
            .subscribeOn(Schedulers.parallel())
            .subscribe(v -> printThreadName("sub " + v));
*/
        Util.sleepSeconds(5);

    }

    private static void printThreadName(String msg){
        System.out.println(msg + "\t\t: Thread : " + Thread.currentThread().getName());
    }


}
