package com.rp.sec04;

import com.rp.courseutil.Util;
import com.rp.sec04.helper.Person;
import reactor.core.publisher.Flux;

import java.util.function.Function;

public class Lec11SwitchOnFirst {

    public static void main(String[] args) {

        getPerson()
                .switchOnFirst((signal, personFlux) -> {
                    System.out.println("inside switch-on-first:"+signal.isOnNext());
                    return signal.isOnNext() && signal.get().getAge() > 10 ? personFlux : applyFilterMap().apply(personFlux);
                })
                // .transform(applyFilterMap())
                .doOnDiscard(Person.class, p -> System.out.println("Not allowing Existing Pipeline: " + p))
                .subscribe(Util.subscriber());
    }

    public static Flux<Person> getPerson(){
        return Flux.range(1, 10)
                .map(i -> new Person());
    }

    public static Function<Flux<Person>, Flux<Person>> applyFilterMap(){
        return flux -> flux
                .filter(p -> p.getAge() > 5)
                .doOnNext(p -> p.setName(p.getName().toUpperCase()+"_TEST"))
                .doOnDiscard(Person.class, p -> System.out.println("Not allowing Usable Pipeline: " + p));
    }




}
