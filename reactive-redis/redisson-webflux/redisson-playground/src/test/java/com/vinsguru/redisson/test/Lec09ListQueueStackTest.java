package com.vinsguru.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.RDequeReactive;
import org.redisson.api.RListReactive;
import org.redisson.api.RQueueReactive;
import org.redisson.client.codec.LongCodec;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Lec09ListQueueStackTest extends BaseTest {

	@Test
    public void listTest_(){
        // lrange number-input 0 -1
        RListReactive<Long> list = this.client.getList("number-input", LongCodec.INSTANCE);

        Mono<Void> listAdd = Flux.range(1, 10)
                .map(Long::valueOf)
                .flatMap(list::add)
                .then();

        /*
         *  for every range value, it will do add operation to list in flatmap and it makes a network call to redis to save. 
         *  so the list will not maintain order here due to network calls
         *  to avoid this, do the add in one shot like in next test case
         * */
        StepVerifier.create(listAdd)
                 .verifyComplete();
        StepVerifier.create(list.size())
                .expectNext(10)
                .verifyComplete();
    }
	
    @Test
    public void listTest(){
        // lrange number-input 0 -1
        RListReactive<Long> list = this.client.getList("number-input", LongCodec.INSTANCE);

        List<Long> longList = LongStream.rangeClosed(1, 10)
                .boxed()
                .collect(Collectors.toList());

        StepVerifier.create(list.addAll(longList).then())
                .verifyComplete();
        StepVerifier.create(list.size())
                .expectNext(10)
                .verifyComplete();
    }

    @Test
    public void queueTest(){
        RQueueReactive<Long> queue = this.client.getQueue("number-input", LongCodec.INSTANCE);
        Mono<Void> queuePoll = queue.poll() // poll - polls from start position since it is a queue
                .repeat(3) // above poll executes one time and same repeats 3 more time so totally 4 times. polls - 1 2 3 4, remains 6 itmes
                .doOnNext(System.out::println)
                .then();
        StepVerifier.create(queuePoll)
                .verifyComplete();
        StepVerifier.create(queue.size())
                .expectNext(6)
                .verifyComplete();
    }

    @Test
    public void stackTest(){ // Deque for stack data structure - same "number-input" datastructure is used for both queue and stack 
        RDequeReactive<Long> deque = this.client.getDeque("number-input", LongCodec.INSTANCE);
        Mono<Void> stackPoll = deque.pollLast() // pollLast - polls from last position since it is a stack
                .repeat(3) // polls 10, 9 8 7. already 1, 2, 3, 4 are polled from above test case and now remaining is 2
                .doOnNext(System.out::println)
                .then();
        StepVerifier.create(stackPoll)
                .verifyComplete();
        StepVerifier.create(deque.size())
                .expectNext(2)
                .verifyComplete();
    }


}
