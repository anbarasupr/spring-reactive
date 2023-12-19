package com.rp.sec10;

import com.rp.courseutil.Util;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

public class Lec04RetryWhenAdvanced {

    public static void main(String[] args) {

//    	executeRetry();
    	executeAdvancedRetry();
        Util.sleepSeconds(60);
    }

	public static void executeRetry() {
		// retry for all errors both 500 and 404
		 orderService(Util.faker().business().creditCardNumber())
		 .doOnError(err->System.out.println(err.getMessage()))
		 .retry(5)
		 .subscribe(Util.subscriber());
	}
    
	public static void executeAdvancedRetry() {
		// retry only for 500 errors not for 404
		 orderService(Util.faker().business().creditCardNumber())
         .retryWhen(Retry.from(
              flux -> flux
                         .doOnNext(rs -> {
                             System.out.println("totalRetries: "+rs.totalRetries());
                             System.out.println("failure: "+rs.failure());
                         })
                         .handle((rs, synchronousSink) -> { // filter + map
                             if(rs.failure().getMessage().equals("500"))
                                 synchronousSink.next(1); // whenever it emits, it consider that as a signal to retry
                             else
                                 synchronousSink.error(rs.failure());
                         })
                         .delayElements(Duration.ofSeconds(1))
         ))
         .subscribe(Util.subscriber());
	}
	
    // order service
    private static Mono<String> orderService(String ccNumber){
        return Mono.fromSupplier(() -> {
            processPayment(ccNumber);
            return Util.faker().idNumber().valid(); // generating an order no
        });
    }

    // payment service
    private static void processPayment(String ccNumber){
        int random = Util.faker().random().nextInt(1, 10);
        if(random < 8)
            throw new RuntimeException("500");
        else if(random < 10)
            throw new RuntimeException("404");
    }

}
