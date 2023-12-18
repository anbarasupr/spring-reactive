package com.rp.sec03;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;

public class Lec07FluxGenerateCounter {

	// Flux Generate - to have a counter or state supplier to maintain the state
    public static void main(String[] args) {

    	// emit until canada country met
    	// max items to emit is 10 times -- need to maintain a state for 10 times
    	// if subscriber cancels - exit the process -- use take
        Flux.generate(
                () -> 1,
                (counter, sink) -> {
                   String country = Util.faker().country().name();
                   sink.next(country);
                   if(counter >= 10 || country.toLowerCase().equals("canada") )
                       sink.complete();
                  return counter + 1;
                }
        )
        .take(4)
        .subscribe(Util.subscriber());


    }

}
