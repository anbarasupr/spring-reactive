package com.rp.test;

import com.rp.sec09.helper.BookOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class Lec04AssertTest {

    @Test
    public void test1(){

        Mono<BookOrder> mono = Mono.fromSupplier(() -> new BookOrder());

        StepVerifier.create(mono)
                .assertNext(b -> Assertions.assertNotNull(b.getAuthor()))
                .verifyComplete();
    }

    
    @Test
    public void test2_0(){

        Mono<BookOrder> mono = Mono.fromSupplier(() -> new BookOrder())
                                    .delayElement(Duration.ofSeconds(3));

        StepVerifier.create(mono)
                .assertNext(b -> Assertions.assertNotNull(b.getAuthor()))
                .verifyComplete(); // this will wait for 3 secs to complete
    }
    
    @Test
    public void test2_1(){

        Mono<BookOrder> mono = Mono.fromSupplier(() -> new BookOrder())
                                    .delayElement(Duration.ofSeconds(3));

        StepVerifier.create(mono)
                .assertNext(b -> Assertions.assertNotNull(b.getAuthor()))
                .expectComplete()
                .verify(Duration.ofSeconds(4)); //assume it is waited for secs. give more than or equal to original delay 3 secs
        		// this will not wait for 3 secs and assume it has waited to complete
        
        /*
         * Consider the assertion is having 3 sec delayElement and the StepVerifier doesnot requiredto wait for 3 sec to complete
         * To avoid waiting in test cases, we can set the test case to assume that it has waited for some duration and execute
         * To do the waiting assumption, use verify(Duration.ofSeconds(4))
         * */
    }

}
