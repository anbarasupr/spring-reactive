package com.rp.sec06;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Lec02SubscribeOnDemo {

    public static void main(String[] args) {

    	
//    	  Flux.create(fluxSink -> {
//             printThreadName("create");				// boundedElastic thread executes
//             fluxSink.next(1);
//         })
//		 .doOnNext(i -> printThreadName("next " + i)) 	// boundedElastic thread executes
//    	 .doFirst(() -> printThreadName("first2"))		// boundedElastic thread executes
//    	 .subscribeOn(Schedulers.boundedElastic()) 		// boundedElastic thread starts from here
//    	 .doFirst(() -> printThreadName("first1"))		// main thread executes and set the pipeline
//    	 .subscribe(v -> printThreadName("sub " + v)); 
    	 
    	 
    	//	Multiple subscribeOn with newParallel and boundedElastic, closest thread  to the publisher takes precedence
    	
        Flux<Object> flux = Flux.create(fluxSink -> {
            printThreadName("create");
            fluxSink.next(1);
        })
		// this is the closes thread (vins) in the pipeline and from here, it starts executing and take precedence over others Schedulers
         .subscribeOn(Schedulers.newParallel("vins")) 
        .doOnNext(i -> printThreadName("next " + i));


      //runnable
      Runnable runnable = () ->  flux
               .doFirst(() -> printThreadName("first2"))
               .subscribeOn(Schedulers.boundedElastic())		// boundedElastic thread starts from here and executes
               .doFirst(() -> printThreadName("first1"))		// current thread executes and set the pipeline
               .subscribe(v -> printThreadName("sub " + v));  

        for (int i = 0; i < 2; i++) {
            new Thread(runnable).start();
        }

        Util.sleepSeconds(5);

    }

    private static void printThreadName(String msg){
        System.out.println(msg + "\t\t: Thread : " + Thread.currentThread().getName());
    }


}
