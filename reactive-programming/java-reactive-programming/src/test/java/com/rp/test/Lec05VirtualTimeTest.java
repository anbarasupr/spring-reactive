package com.rp.test;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class Lec05VirtualTimeTest {

    @Test
    public void test1(){
        StepVerifier.withVirtualTime(() -> timeConsumingFlux())
                .thenAwait(Duration.ofSeconds(30)) // with Virtual time, it will simulate the clock that the time has passed for 30 secs to avoid the waiting
                .expectNext("1a", "2a", "3a", "4a")
                .verifyComplete();
    }

    @Test
    public void test2(){
        StepVerifier.withVirtualTime(() -> timeConsumingFlux())
                .expectSubscription() // sub is an event. expect onSubscribe event and then no event happened give below
                .expectNoEvent(Duration.ofSeconds(4)) //First 4 secs, to verify no event has hpapened
                .thenAwait(Duration.ofSeconds(20)) // simulate it iswaited for 20 secs and got everything
                .expectNext("1a", "2a", "3a", "4a")
                .verifyComplete();
    }


    private Flux<String> timeConsumingFlux(){
        return Flux.range(1, 4)
                    .delayElements(Duration.ofSeconds(5))
                    .map((i -> i + "a"));
    }


}
