package com.rp.sec08;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;

public class Lec04Zip {

    public static void main(String[] args) {
        Flux.zip(getBody(), getEngine(), getTires())
                .subscribe(Util.subscriber());

    }

    private static Flux<String> getBody(){
        return Flux.range(1, 5)
                .map(i -> "body:"+i);
    }

    private static Flux<String> getEngine(){
        return Flux.range(1, 3)
                .map(i -> "engine:"+i);
    }

    private static Flux<String> getTires(){
        return Flux.range(1, 6)
                .map(i -> "tires:"+i);
    }


}
