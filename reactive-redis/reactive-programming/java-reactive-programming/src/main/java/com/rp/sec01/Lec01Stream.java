package com.rp.sec01;

import java.util.stream.Stream;

public class Lec01Stream {

    public static void main(String[] args) {

        Stream<Integer> stream = Stream.of(1)
                                        .map(i -> {
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            return i * 2;
                                        });

        //System.out.println(stream);
        stream.forEach(System.out::println);
        
        // Streams are lazy unless a terminal operator is connected
        // The stream pipelines are executed only if a terminal operator is executed.

    }

    
}
