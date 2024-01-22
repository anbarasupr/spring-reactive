package com.rp.sec12;

import com.rp.courseutil.Util;
import com.rp.sec12.helper.BookService;
import com.rp.sec12.helper.UserService;
import reactor.util.context.Context;

public class Lec02CtxRateLimiterDemo {

    public static void main(String[] args) {

    	// Based on the user category (std/prime), allow the purchase of book based on rate limiter
    	// std user can do 2 purchase, prime user can do 3 purchase
        BookService.getBook()
                .repeat(3) // first time and  3 repeats so totally 4 execution
                .contextWrite(UserService.userCategoryContext())
                .contextWrite(Context.of("user", "mike"))
                .subscribe(Util.subscriber());
    }


}
