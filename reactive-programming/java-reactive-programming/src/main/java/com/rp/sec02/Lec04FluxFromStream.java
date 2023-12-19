package com.rp.sec02;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Stream;

public class Lec04FluxFromStream {

    public static void main(String[] args) {

        List<Integer> list = List.of(1, 2, 3, 4, 5);
        Stream<Integer> stream = list.stream();

       // stream.forEach(System.out::println); // closed 
       // Streams are one time use. Once the pipelines are executed and it will closed and cannot reuse
       // stream.forEach(System.out::println);

        Flux<Integer> integerFlux = Flux.fromStream(() -> list.stream());

        integerFlux
                .subscribe(
                        Util.onNext(),
                        Util.onError(),
                        Util.onComplete()
                );

        // We can connect multiple subscribers to a Reactive publisher streams
        integerFlux
                .subscribe(
                        Util.onNext(),
                        Util.onError(),
                        Util.onComplete()
                );


    }

}
