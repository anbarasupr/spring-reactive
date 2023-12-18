package com.rp.sec08;

import com.rp.courseutil.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Lec06Assignment {

	/*
	 * Consider a car bought for 10 K dollar, the price is reducing for every month by 100 dollar starts from 0th Month, 
	 * there is demand due to inflation which is around 0.8 - 1.2 every quarter
	 * */
    public static void main(String[] args) {

        final int carPrice = 10000;
        Flux.combineLatest(monthStream(), demandStream(), (month, demand) -> {
            return (carPrice - (month * 100)) * demand;
        })
        .subscribe(Util.subscriber());


        Util.sleepSeconds(20);

    }


    private static Flux<Long> monthStream(){
        return Flux.interval(Duration.ZERO, Duration.ofSeconds(1));
    }

    private static Flux<Double> demandStream(){
        return Flux.interval(Duration.ofSeconds(3)) // every quarter
                    .map(i -> Util.faker().random().nextInt(80, 120) / 100d)
                    .startWith(1d); 
        // the demand factor for first month is 1d (1 double value) and it startswith 1 for the first month, then for every 3sec which is like every quarter, it generates demand value b/w 0.8-1.2
    }

}
