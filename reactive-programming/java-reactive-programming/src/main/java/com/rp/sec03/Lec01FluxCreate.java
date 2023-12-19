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
                fluxSink.next(country);
            }while (!country.toLowerCase().equals("canada"));
            fluxSink.complete();
        })
        .subscribe(Util.subscriber());



    }

}
