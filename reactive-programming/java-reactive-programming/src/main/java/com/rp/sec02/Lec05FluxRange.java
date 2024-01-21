package com.rp.sec02;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;

public class Lec05FluxRange {

    public static void main(String[] args) {

//    	Parameters:	start the first integer to be emit
//		count the total number of incrementing values to emit, including the first value
        Flux.range(3, 10)
                .log()
                .map(i -> Util.faker().name().fullName())
//                .log()
                .subscribe(
                        Util.onNext()
                );


    }

}
