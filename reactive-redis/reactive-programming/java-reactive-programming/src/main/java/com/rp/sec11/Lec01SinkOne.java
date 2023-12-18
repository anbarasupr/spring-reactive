package com.rp.sec11;

import com.rp.courseutil.Util;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

public class Lec01SinkOne {

    public static void main(String[] args) {

//    	tryEmitValue();
    	emitFailureHandler();

    }

    public static void tryEmitValue() {
        // mono 1 value / empty / error
        Sinks.One<Object> sink = Sinks.one(); // capable of emitting one value / empty / error

        Mono<Object> mono = sink.asMono();

        mono.subscribe(Util.subscriber("sam"));
        mono.subscribe(Util.subscriber("mike"));

        sink.tryEmitValue("Hello");
    }
    
    public static void emitFailureHandler() {
        // mono 1 value / empty / error
        Sinks.One<Object> sink = Sinks.one(); // capable of emitting one value / empty / error
        
        Mono<Object> mono = sink.asMono();

        mono.subscribe(Util.subscriber("sam"));
        mono.subscribe(Util.subscriber("mike"));
        
        sink.emitValue("hi", (signalType, emitResult) -> {
	        System.out.println(signalType.name());
	        System.out.println(emitResult.name());
	        return false; // required retry or not
	    });

        // we should not emit multiple values like below because it is a Mono which should 0 or 1 item which is already emitted above                                                    
//	    sink.emitValue("hello", (signalType, emitResult) -> {
//	        System.out.println(signalType.name());
//	        System.out.println(emitResult.name());
//	        return false;
//	    }); 
       
       
    }
}
