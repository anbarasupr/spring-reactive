package com.vinsguru.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.RPatternTopicReactive;
import org.redisson.api.RTopicReactive;
import org.redisson.api.listener.PatternMessageListener;
import org.redisson.client.codec.StringCodec;

import reactor.core.publisher.Mono;

public class Lec12PubSubTest extends BaseTest {

    @Test
    public void subscriber1(){
        RTopicReactive topic = this.client.getTopic("slack-room1", StringCodec.INSTANCE);
        topic.getMessages(String.class)
                .doOnError(System.out::println)
                .doOnNext(System.out::println)
                .subscribe();
        sleep(600_000); 
    }

    @Test
    public void subscriber2(){ // listen to multiple channels that starts with slack-room using patterns
        RPatternTopicReactive patternTopic = this.client.getPatternTopic("slack-room*", StringCodec.INSTANCE);
        patternTopic.addListener(String.class, new PatternMessageListener<String>() {
            @Override
            public void onMessage(CharSequence pattern, CharSequence topic, String msg) {
                System.out.println(pattern + " : " + topic + " : " + msg);
            }
        }).subscribe();
        sleep(600_000);
    }

    // publish the messages using redis-cli with the command: publish slack-room "Welcome Team"
    
    @Test
    public void producer(){
        RTopicReactive topic = this.client.getTopic("slack-room1", StringCodec.INSTANCE);
        topic.publish("Welcome Team").doOnNext(System.out::println).block();
 		topic.publish("Hope all well...").doOnNext(System.out::println).block();
    }
}
