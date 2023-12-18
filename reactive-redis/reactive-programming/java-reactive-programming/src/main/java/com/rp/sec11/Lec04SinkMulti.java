package com.rp.sec11;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class Lec04SinkMulti {
	// many-multicast			Flux					1:N	(one publisher, multiple subscribers)
    public static void main(String[] args) {

//    	onBackpressureBuffer();
    	directAllOrNothing();
    }
    
    public static void onBackpressureBuffer( ) {

        // handle through which we would push items
        Sinks.Many<Object> sink = Sinks.many().multicast().onBackpressureBuffer();
       
        // handle through which subscribers will receive items
        Flux<Object> flux = sink.asFlux();

        sink.tryEmitNext("hi");
        sink.tryEmitNext("how are you");

        flux.subscribe(Util.subscriber("sam")); // When a first subscriber connects, the messages in the queue played to him
        flux.subscribe(Util.subscriber("mike")); // mike joins after sam, the messages in the queue already played sam, so mike will only new messages
        sink.tryEmitNext("?");
        flux.subscribe(Util.subscriber("jake")); // same like mike

        sink.tryEmitNext("new msg");

    }

    public static void directAllOrNothing( ) {

        // handle through which we would push items       
        
        // to disable the buffering history
        Sinks.Many<Object> sink = Sinks.many().multicast().directAllOrNothing();
        
        // handle through which subscribers will receive items
        Flux<Object> flux = sink.asFlux();


        // To disable above initial messages in the queue,
    	// Whoever joined after that, whatever the messages emitted, they see only those so hi, how are you are not seen by any subscriber
    
        sink.tryEmitNext("hi");
        sink.tryEmitNext("how are you");

        flux.subscribe(Util.subscriber("sam"));  
        flux.subscribe(Util.subscriber("mike"));  
        sink.tryEmitNext("?");
        flux.subscribe(Util.subscriber("jake")); 

        sink.tryEmitNext("new msg");

    }

}
