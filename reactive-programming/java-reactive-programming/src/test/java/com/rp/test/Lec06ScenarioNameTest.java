package com.rp.test;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;

public class Lec06ScenarioNameTest {

    @Test
    public void test1(){

        Flux<String> flux = Flux.just("a", "b", "c");

        StepVerifierOptions scenarioName = StepVerifierOptions.create().scenarioName("alphabets-test"); 
        // give the scenario name to identify the test name helpful in failing in which scenario it isfailed

        StepVerifier.create(flux, scenarioName)
                .expectNextCount(12)
                .verifyComplete();


    }

    @Test
    public void test2(){

        Flux<String> flux = Flux.just("a", "b1", "c");

        StepVerifier.create(flux)
                .expectNext("a")
                .as("a-test") // Set a description for the previous verification step.
                // Choosing a unique and descriptive name can make assertion errors easier to resolve. 
                .expectNext("b")
                .as("b-test")
                .expectNext("c")
                .as("c-test")
                .verifyComplete();


    }

}
