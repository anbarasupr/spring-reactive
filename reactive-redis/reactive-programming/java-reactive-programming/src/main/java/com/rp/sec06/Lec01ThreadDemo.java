package com.rp.sec06;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;

public class Lec01ThreadDemo {

	// By default, all the tasks in a pipeline are executed by current thread (main/any) which is executing the pipeline
    public static void main(String[] args) {

        Flux<Object> flux = Flux.create(fluxSink -> {
            printThreadName("create");
            fluxSink.next(1);
        })
        .doOnNext(i -> printThreadName("next " + i));

       flux.subscribe(v -> printThreadName("sub " + v)); // Everything executed by current thread which is main thread
        
       flux.subscribe(v -> printThreadName("sub__ " + v));	// Everything executed by current thread which is main thread
       
       Runnable runnable = () -> flux.subscribe(v -> printThreadName("sub " + v));
       for (int i = 0; i < 2; i++) {
            new Thread(runnable).start(); // Multiple subscribers executed in various threads
       }

        Util.sleepSeconds(5);

    }

    private static void printThreadName(String msg){
        System.out.println(msg + "\t\t: Thread : " + Thread.currentThread().getName());
    }


}
