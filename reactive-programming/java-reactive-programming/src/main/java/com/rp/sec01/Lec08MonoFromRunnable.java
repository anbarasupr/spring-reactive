package com.rp.sec01;

import com.rp.courseutil.Util;
import reactor.core.publisher.Mono;

public class Lec08MonoFromRunnable {

    public static void main(String[] args) {

    	// Create a Mono that completes empty once the provided Runnable has been executed. 
        Mono.fromRunnable(timeConsumingProcess())
                .subscribe(Util.onNext(),
                        Util.onError(),
                        () -> {
                            System.out.println("process is done. Sending emails...");
                        });
        System.out.println("--done--");

    }

    private static Runnable timeConsumingProcess(){
        return () -> {
            Util.sleepSeconds(3);
            System.out.println("Operation completed");
        };
    }


}
