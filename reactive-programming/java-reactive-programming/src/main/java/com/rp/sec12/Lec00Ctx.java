package com.rp.sec12;

import com.rp.courseutil.Util;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

public class Lec00Ctx {

    public static void main(String[] args) {

        getWelcomeMessage()
                 .subscribe(Util.subscriber());

    }


    private static Mono<String> getWelcomeMessage(){
    	return Mono.just("Welcome"); 
    	// Whoever calls this endpoint or pipeline, it need to know the username to authenticate and without user name, throw error
    }

}
