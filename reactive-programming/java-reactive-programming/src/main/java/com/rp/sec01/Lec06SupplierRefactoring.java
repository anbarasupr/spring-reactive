package com.rp.sec01;

import com.rp.courseutil.Util;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class Lec06SupplierRefactoring {

	public static void main(String[] args) {

//		executeNone();
		executeBlocking();
//		executeNonBlocking();
//		executeBlockingV1();
	}

	public static void executeNone() {
		// the reactive pipelines are not executed due to no subscription not happened
		getName();
		getName();
		getName();
	}

	public static void executeBlocking() {
		getName();
		getName().subscribe(Util.onNext());
		getName();
	}

	public static void executeNonBlocking() {
		getName();
		// makes async and main thread will not wait until this is completing
		getName().subscribeOn(Schedulers.boundedElastic()) // makes async
				.subscribe(Util.onNext());
		getName();
		Util.sleepSeconds(4); // make main thread to wait until the completion
	}

	public static void executeBlockingV1() {
		getName();
		// makes async but blocks again due to block()
		String name = getName().subscribeOn(Schedulers.boundedElastic()).block();
		System.out.println(name);
		getName();
	}

	private static Mono<String> getName() {
//		System.out.println("entered getName method");
		System.out.println("entered getName method Thread:"+Thread.currentThread().getName());
		return Mono.fromSupplier(() -> {
			System.out.println("Generating name..");
			Util.sleepSeconds(3);
			return Util.faker().name().fullName();
		}).map(String::toUpperCase);
	}

}
