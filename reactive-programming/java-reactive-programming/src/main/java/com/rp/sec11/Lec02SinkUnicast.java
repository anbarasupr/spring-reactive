package com.rp.sec11;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class Lec02SinkUnicast {

    public static void main(String[] args) {
    	// many-unicast			Flux					1:1	(one publisher, one subscriber)
        // handle through which we would push items
        Sinks.Many<Object> sink = Sinks.many().unicast().onBackpressureBuffer();

        // handle through which subscribers will receive items
        Flux<Object> flux = sink.asFlux();

        flux.subscribe(Util.subscriber("sam"));
        // flux.subscribe(Util.subscriber("mike")); // ERROR : UnicastProcessor allows only a single Subscriber

        sink.tryEmitNext("hi");
        sink.tryEmitNext("how are you");
        sink.tryEmitNext("?");
//        sink.tryEmitComplete();
        sink.tryEmitError(new RuntimeException("failure"));


    }

}
