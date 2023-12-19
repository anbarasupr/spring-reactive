package com.rp.sec04;

import com.rp.courseutil.Util;
import com.rp.sec04.helper.OrderService;
import com.rp.sec04.helper.UserService;

import java.io.BufferedReader;

public class Lec13ConcatMap {

    public static void main(String[] args) {

        BufferedReader reader;


        UserService.getUsers()
                .concatMap(user -> OrderService.getOrders(user.getUserId())) // mono / flux
               // .filter(p -> p > 10)
                .log()
                .subscribe(Util.subscriber());


        Util.sleepSeconds(60);


    }


}
