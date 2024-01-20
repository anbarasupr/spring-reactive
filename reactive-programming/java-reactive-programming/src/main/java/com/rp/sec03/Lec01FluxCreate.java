package com.rp.sec03;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;

public class Lec01FluxCreate {

    public static void main(String[] args) {
        
    	Flux.create(fluxSink -> {
    		fluxSink.next(1);
    		fluxSink.next(2);
    		fluxSink.next(3);
            fluxSink.complete();
        })
        .subscribe(Util.subscriber());
    	
    	
    	// keep emiting countries until we get canada
        Flux.create(fluxSink -> {
            String country;
            do{
                country = Util.faker().country().name();
                System.out.println("fluxSink emitting country "+country);
                fluxSink.next(country);
            }while (!country.toLowerCase().equals("canada"));
            fluxSink.complete();
        })
        // If downstream system (subscriber) is  cancelled, then flux sink should not emit data and it has to be handled by developer.
        .take(3)
        .subscribe(Util.subscriber());



    }

}
