package com.rp.sec10;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;

public class Lec01Repeat {

    private static AtomicInteger atomicInteger = new AtomicInteger(1);

    public static void main(String[] args) {

        getIntegers()
        		//.repeat(2)
        		//.repeat()
                .repeat(() -> atomicInteger.get() < 14) 
                // when first time completes, the signal not even went to below subscriber and repeat will consume it 
                //and executes the pipe again 2 times. after executing 3 times only, the complete signal reaches the subscriber
                .subscribe(Util.subscriber());


    }


    private static Flux<Integer> getIntegers(){
        return Flux.range(1, 3)
                    .doOnSubscribe(s -> System.out.println("Subscribed"))
                    .doOnComplete(() -> System.out.println("--Completed"))
                    .map(i -> atomicInteger.getAndIncrement());
    }

}
