package com.rp.sec04;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;

public class Lec03DoCallbacks {

    public static void main(String[] args) {

        Flux.create(fluxSink -> {
            System.out.println("inside fluxSink create");
            for (int i = 0; i < 5; i++) {
                fluxSink.next(i);
            }
            fluxSink.complete();
            // fluxSink.error(new RuntimeException("oops"));
             System.out.println("--fluxSink completed--");
        })
        .doOnTerminate(() -> System.out.println("doOnTerminate 1"))
        .doOnComplete(() -> System.out.println("doOnComplete 1:"))
        .doFirst(() -> System.out.println("doFirst:"))
        .doFirst(() -> System.out.println("doAnotherFirst:"))
        .doOnNext(o -> System.out.println("doOnNext : " + o))
        .doOnRequest(l -> System.out.println("doOnRequest 1: " + l))
        .doOnSubscribe(s -> System.out.println("doOnSubscribe 1: " + s))
        .doOnRequest(l -> System.out.println("doOnRequest 2: " + l))
        .doOnError(err -> System.out.println("doOnError : " + err.getMessage()))
        .doOnTerminate(() -> System.out.println("doOnTerminate 2"))
        .doOnSubscribe(s -> System.out.println("doOnSubscribe 2: " + s))
        .doOnCancel(() -> System.out.println("doOnCancel"))
        .doFinally(signal -> System.out.println("doFinally 1 : " + signal))
        .doFinally(signal -> System.out.println("doFinally 2 : " + signal))
        .doOnDiscard(Object.class, o -> System.out.println("doOnDiscard : " + o))
        .doOnTerminate(() -> System.out.println("doOnTerminate 3"))
        // .take(2)
        .doOnComplete(() -> System.out.println("doOnComplete 2:"))
        .doOnTerminate(() -> System.out.println("doOnTerminate 4"))
        .doFinally(signal -> System.out.println("doFinally 3 : " + signal))
        .subscribe(Util.subscriber("Subscriber"));


    }

}
