package com.rp.sec08.helper;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

public class NameGenerator {

    private List<String> list = new ArrayList<>();

    public Flux<String> generateNames(){
        return Flux.generate(stringSynchronousSink -> {
            System.out.println("generated fresh");
            Util.sleepSeconds(1);
            String name = Util.faker().name().firstName();
            list.add(name);
            stringSynchronousSink.next(name);
        })
        .cast(String.class)
        .startWith(getFromCache()); //starts with this publisher for the requested items and emits them, if more required generate using above sink and cache for next consumers
    } 

    private Flux<String> getFromCache(){
        return Flux.fromIterable(list);
    }

}
