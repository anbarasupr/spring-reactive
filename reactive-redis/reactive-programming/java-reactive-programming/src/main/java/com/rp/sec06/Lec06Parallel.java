package com.rp.sec06;

import java.util.ArrayList;
import java.util.List;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Lec06Parallel {

    public static void main(String[] args) {

    	parallelism();
//    	notThreadSafeParallelism();
//    	parallelismPublishOnSubscribeOn();

    }

    private static void printThreadName(String msg){
        System.out.println(msg + "\t\t: Thread : " + Thread.currentThread().getName());
    }
    
    public static void parallelism() {

         Flux.range(1, 10)
                .parallel(2) // to adjust the parallel thread counts
                .runOn(Schedulers.boundedElastic())
                .doOnNext(i -> printThreadName("next " + i))
                //.sequential()
                .subscribe(v -> printThreadName("sub " + v));
        
        Util.sleepSeconds(5); 
    }
    
    public static void notThreadSafeParallelism() {

    	List<Integer> list = new ArrayList<>();
        Flux.range(1, 1000)
                .parallel(10)
                .runOn(Schedulers.boundedElastic())
                .doOnNext(i -> printThreadName("next " + i))
                //.sequential()
                //.subscribe(v -> printThreadName("sub " + v));
                .subscribe(v->list.add(v));
       
        Util.sleepSeconds(5);
        System.out.println("list:"+list.size()); // list is not thread safe and the output here is not consistent all the time
        // its upto developer to choose the right data type in parallelism
    }
    
    public static void parallelismPublishOnSubscribeOn() {

        Flux.range(1, 10)
               .parallel() // to adjust the parallel thread counts
               .runOn(Schedulers.boundedElastic()) // ParallelFlux type
               .doOnNext(i -> printThreadName("next " + i))
               // publishOn, subscribeOn cannnot be used ParallelFlux. To achieve use sequential Flux
               .sequential().publishOn(Schedulers.parallel()) 
               .subscribe(v -> printThreadName("sub " + v));
       
       Util.sleepSeconds(5); 
   }
}
