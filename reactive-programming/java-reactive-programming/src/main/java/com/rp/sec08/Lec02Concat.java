package com.rp.sec08;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;

public class Lec02Concat {

    public static void main(String[] args) {

        Flux<String> flux1 = Flux.just("a", "b");
        Flux<String> flux2 = Flux.error(new RuntimeException("oops"));
        Flux<String> flux3 = Flux.just("c", "d", "e");

//        Flux<String> flux0 = flux1.concatWith(flux3);
//        flux0.subscribe(Util.subscriber());
        

        Flux<String> flx = Flux.concat(flux1, flux2, flux3); // if any error signal is produced by any of flux, then concat operation there
        flx.subscribe(Util.subscriber());
        
        Flux<String> flux = Flux.concatDelayError(flux1, flux2, flux3);
        flux.subscribe(Util.subscriber());


    }

}
