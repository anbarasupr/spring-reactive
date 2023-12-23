package com.rp.courseutil;

import com.github.javafaker.Faker;
import org.reactivestreams.Subscriber;

import java.util.function.Consumer;

public class Util {

	private static final Faker FAKER = Faker.instance();

	public static Consumer<Object> onNext() {
		return o -> System.out.println("Received onNext: " + o + ", Thread:" + Thread.currentThread().getName());
	}

	public static Consumer<Throwable> onError() {
		return e -> System.out
				.println("ERROR onError: " + e.getMessage() + ", Thread:" + Thread.currentThread().getName());
	}

	public static Runnable onComplete() {
		return () -> System.out.println("Completed onComplete" + ", Thread:" + Thread.currentThread().getName());
	}

	public static Faker faker() {
		return FAKER;
	}

	public static void sleepSeconds(int seconds) {
		sleepMillis(seconds * 1000);
	}

	public static void sleepMillis(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static Subscriber<Object> subscriber() {
		return new DefaultSubscriber();
	}

	public static Subscriber<Object> subscriber(String name) {
		return new DefaultSubscriber(name);
	}

}
