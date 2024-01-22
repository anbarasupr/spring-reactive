package com.vinsguru.redisson.test.assignment;

import org.redisson.api.RScoredSortedSetReactive;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PriorityQueue {
    
    private RScoredSortedSetReactive<UserOrder> queue;

    public PriorityQueue(RScoredSortedSetReactive<UserOrder> queue) {
        this.queue = queue;
    }

    public Mono<Void> add(UserOrder userOrder){
        return this.queue.add(
//        		userOrder.getCategory().ordinal(),
                getScore(userOrder.getCategory()),
                userOrder
        ).then();
    }

    public Flux<UserOrder> takeItems(){
        return this.queue.takeFirstElements() // takeFirstElements - take the lowest value - here PRIME is having 0th value which is lowest
                         .limitRate(1); // take one by one instead of unbounded request
    }

    private double getScore(Category category){
        return category.ordinal() + Double.parseDouble("0." + System.nanoTime());
    }

}
