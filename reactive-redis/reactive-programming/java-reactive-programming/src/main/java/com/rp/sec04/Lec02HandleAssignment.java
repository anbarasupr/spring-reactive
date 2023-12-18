package com.rp.sec04;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;

public class Lec02HandleAssignment {

    public static void main(String[] args) {

        Flux.generate(synchronousSink -> synchronousSink.next(Util.faker().country().name())).log()
                .map(Object::toString)
                .handle((s, synchronousSink) -> {
                    synchronousSink.next(s);
                    if(s.toLowerCase().equals("canada"))
                        synchronousSink.complete();
                }).log()
                .take(2)
                .subscribe(Util.subscriber());

    }

}
