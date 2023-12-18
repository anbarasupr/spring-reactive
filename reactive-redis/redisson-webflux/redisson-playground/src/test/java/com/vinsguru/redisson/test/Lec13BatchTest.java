package com.vinsguru.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.BatchOptions;
import org.redisson.api.RBatchReactive;
import org.redisson.api.RListReactive;
import org.redisson.api.RSetReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec13BatchTest extends BaseTest {

    @Test // 7.6 seconds for 50 + 50 K = 1 M calls and 1.5 second for 20 + 20 K = 40K parallel calls using batch operation
    public void batchTest(){
        RBatchReactive batch = this.client.createBatch(BatchOptions.defaults());
        RListReactive<Long> list = batch.getList("numbers-list", LongCodec.INSTANCE);
        RSetReactive<Long> set = batch.getSet("numbers-set", LongCodec.INSTANCE);
        for (long i = 0; i < 500_000; i++) {
            list.add(i);
            set.add(i);
        }
        StepVerifier.create(batch.execute().then())
                .verifyComplete();
    }

    @Test // 26.9 seconds for 50 + 50 K = 1 M calls without batch operation
    public void regularTest(){
        RListReactive<Long> list = this.client.getList("numbers-list", LongCodec.INSTANCE);
        RSetReactive<Long> set = this.client.getSet("numbers-set", LongCodec.INSTANCE);
        Mono<Void> mono = Flux.range(1, 500_000)
                .map(Long::valueOf)
                .flatMap(i -> list.add(i).then(set.add(i)))
                .then();
        StepVerifier.create(mono)
                .verifyComplete();
    }

}
