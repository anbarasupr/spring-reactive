package com.rp.sec01;

import com.rp.courseutil.Util;
import reactor.core.publisher.Mono;

public class Lec04MonoEmptyOrError {

	public static void main(String[] args) {
		System.out.println("----first name------");
		userRepository(1).subscribe(Util.onNext(), Util.onError(), Util.onComplete());

		System.out.println("----mono empty------");
		userRepository(2).subscribe(Util.onNext(), Util.onError(), Util.onComplete());

		System.out.println("----mono error------");
		userRepository(3).subscribe(Util.onNext(), Util.onError(), Util.onComplete());

	}

	private static Mono<String> userRepository(int userId) {
		// 1
		if (userId == 1) {
			return Mono.just(Util.faker().name().firstName());
		} else if (userId == 2) {
			return Mono.empty(); // null
		} else
			return Mono.error(new RuntimeException("Not in the allowed range"));
	}

}
