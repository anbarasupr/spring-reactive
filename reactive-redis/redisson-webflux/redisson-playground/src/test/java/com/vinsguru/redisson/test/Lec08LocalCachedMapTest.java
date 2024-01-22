package com.vinsguru.redisson.test;

import com.vinsguru.redisson.test.config.RedissonConfig;
import com.vinsguru.redisson.test.dto.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

public class Lec08LocalCachedMapTest extends BaseTest {

    private RLocalCachedMap<Integer, Student> studentsMap;

    // use [hgetall students], [hget 1]
    
    @BeforeAll
    public void setupClient(){
        RedissonConfig config = new RedissonConfig();
        RedissonClient redissonClient = config.getClient();

        // LocalCachedMapOptions is used to cache the whole hash in the application itself to avoid to going to redis in network
        // and also we can set the time to live in the application
        // if any updates happen, it will publish to all the application who are subcribed to it
        
        /* if any update happens in redis,
         * SyncStrategy.NONE - do not publish to any apps if any update happens in redis and the redis is have the updated data and apps not
         * SyncStrategy.INVALIDATE - removes the invalidate data in other instances and upon requesting, apps will get the new data
         * SyncStrategy.UPDATE  - publish to all instance with latest data and get reflected in apps
         * 
         * ReconnectionStrategy.CLEAR - if the connection b/w app and redis is down for a while due to redis is down or any, 
         * the app will serve the data with its local copy  and the connection is alive,
         * the app data get cleared and get the latest from redis
         * eventhough the SyncStrategy is update redis cannot publish new data to the app due to its connectivity issue.
         * ReconnectionStrategy.NONE - the client will serve the data always with its local copy irrespective of any update happens in redis by other apps
         * */
  
        LocalCachedMapOptions<Integer, Student> mapOptions = LocalCachedMapOptions.<Integer, Student>defaults()
                .syncStrategy(LocalCachedMapOptions.SyncStrategy.NONE)
                .reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.CLEAR);

        this.studentsMap = redissonClient.getLocalCachedMap(
                "students",
                new TypedJsonJacksonCodec(Integer.class, Student.class),
                mapOptions
        );
    }

    /* UPDATE, NONE - app1 gets the data, app2 updates the data in redis, 
     * redis publish the latest data to apps  due to SyncStrategy is Update
     * so app1 gets the latest data,  
     *
    */
    
    /* NONE, NONE - app1 gets the data, app2 updates the data in redis, 
     * redis does not publish the latest data to apps due to SyncStrategy is none
     * so app1 serve the stale data
     *
    */
    
    /* NONE, CLEAR - app1 gets the data, app2 updates the data in redis, 
     * redis does not publish the latest data to apps due to SyncStrategy is none
     * app1 still serves the local stale data, make redis down and broke the connection, 
     * app1 still serves the local stale data,
     * make redis up and reconnection happens from app1 to redis, 
     * now app1 gets the latest data due to ReconnectionStrategy strategy clear even though SyncStrategy is none
     *
     * The app gets the latest data whenever app reconnects to redis due to connection breakout with app and the redis
     * and app tries to reconnect and the latest data. If there is reconnection happens, it will serve the stale data
     *
    */
    
    /* UPDATE, CLEAR - app1 gets the data, app2 updates the data in redis, 
     * redis publish the latest data to apps due to SyncStrategy is Update
     * app1 serves the latest data, make redis down and broke the connection, 
     * app1 still serves the local stale data,
     * make redis up and reconnection happens from app1 to redis, 
     * now app1 still get the latest data again from redis due to ReconnectionStrategy strategy clear
    */
    

    @Test
    public void appServer1(){
        Student student1 = new Student("sam", 10, "atlanta", List.of(1, 2, 3));
        Student student2 = new Student("jake", 30, "miami", List.of(10, 20, 30));
        this.studentsMap.put(1, student1);
        this.studentsMap.put(2, student2);

        Flux.interval(Duration.ofSeconds(1))
                .doOnNext(i -> System.out.println(i + " ==> " + studentsMap.get(1)))
                .subscribe();

        sleep(600000); //10 mins
    }

    @Test
    public void appServer2(){
        Student student1 = new Student("sam-updated", 10, "atlanta", List.of(1, 2, 3));
        this.studentsMap.put(1, student1);
    }

}
