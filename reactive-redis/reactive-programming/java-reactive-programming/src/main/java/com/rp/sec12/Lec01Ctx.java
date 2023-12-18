package com.rp.sec12;

import com.rp.courseutil.Util;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

public class Lec01Ctx {

    public static void main(String[] args) {

        getWelcomeMessage()
                .contextWrite(ctx -> ctx.put("user", ctx.get("user").toString().toUpperCase())) // context update
//                .contextWrite(Context.of("users", "jake")) // add multiple keys
//                .contextWrite(Context.of("user", "jake")) // replace the user key with jake
                .contextWrite(Context.of("user", "sam")) // Context created by downstream
                .subscribe(Util.subscriber());

    }


    private static Mono<String> getWelcomeMessage(){
        return Mono.deferContextual(ctx -> {
            if(ctx.hasKey("user")){
                return Mono.just("Welcome  " + ctx.get("user"));
            }else{
                return Mono.error(new RuntimeException("unauthenticated"));
            }
        });
    }


}
