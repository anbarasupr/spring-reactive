package com.rp.sec04;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;

public class Lec04LimitRate {

    public static void main(String[] args) {

        Flux.range(1, 20) // 175
                .log()
                .limitRate(10) // 75%
                .log()
                .subscribe(Util.subscriber());



    }


}
