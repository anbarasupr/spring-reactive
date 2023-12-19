package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;
import java.util.List;

class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {

        //given

        //when
        var stringFlux = fluxAndMonoGeneratorService.namesFlux();

        //then
        StepVerifier.create(stringFlux)
                //.expectNext("alex", "ben", "chloe")
                //.expectNextCount(3)
                .expectNext("alex")
                .expectNextCount(2)
                .verifyComplete();

    }

    @Test
    void namesFlux_Immutability() {

        //given

        //when
        var stringFlux = fluxAndMonoGeneratorService.namesFlux_immutablity()
                .log();

        //then
        StepVerifier.create(stringFlux)
                .expectNext("ALEX", "BEN", "CHLOE")
                //.expectNextCount(3)
                .verifyComplete();


    }

    @Test
    void namesMono() {

        //given
        //when
        var stringMono = fluxAndMonoGeneratorService.namesMono();

        //then
        StepVerifier.create(stringMono)
                .expectNext("alex")
                .verifyComplete();

    }

    @Test
    void namesMono_map_filter() {

        //given
        int stringLength = 3;

        //when
        var stringMono = fluxAndMonoGeneratorService.namesMono_map_filter(stringLength);

        //then
        StepVerifier.create(stringMono)
                .expectNext("ALEX")
                .verifyComplete();

    }

    @Test
    void namesMono_map_empty() {

        //given
        int stringLength = 4;

        //when
        var stringMono = fluxAndMonoGeneratorService.namesMono_map_filter(stringLength);

        //then
        StepVerifier.create(stringMono)
                .expectNext("default")
                .verifyComplete();

    }


    @Test
    void namesFlux_map() {

        //given
        int stringLength = 3;

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_map(stringLength).log();

        //then
        StepVerifier.create(namesFlux)
                //.expectNext("ALEX", "BEN", "CHLOE")
                .expectNext("4-ALEX", "5-CHLOE")
                .verifyComplete();

    }

    @Test
    void namesFlux_flatmap() {

        //given
        int stringLength = 3;

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_flatmap(stringLength).log();

        //then
        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();

    }

    @Test
    void namesFlux_flatmap_async() {

        //given
        int stringLength = 3;

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_flatmap_async(stringLength).log();

        //then
        StepVerifier.create(namesFlux)
                /*.expectNext("0-A", "1-L", "2-E", "3-X")
                .expectNextCount(5)*/
                .expectNextCount(9)
                .verifyComplete();

    }
    /*
     * Map:
     * 	One to One Transformation
     *  Does the simple transformation from T to V
     *  Used for simple synchronous transformation
     *  Does not support transformations that returns Publisher
     *  
     * FlatMap:
     *  One to N Transformations
     *  Does more than just transformation. Subscribes tp Flux or Mono that is part of the transformation and then flattens it and sends to downstreams
     *  used for asynchronous transformations
     *  
     * */
    
    
    /*
     * Flatmap: 
     * 	Async in nature
     *  Ordering does not maintain and it is fast
     *  
     * ConcatMap:
     *  Sync in nature
     *  Ordering maintains and it takes time to complete due to ordering maintaining
     * */
    
    @Test
    void namesFlux_concatMap() {

        //given
        int stringLength = 3;

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_concatmap(stringLength).log();

        //then
        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X")
                //expectNext("0-A", "1-L", "2-E", "3-X")
                .expectNextCount(5)
                .verifyComplete();

    }

    @Test
    void namesFlux_concatmap_withVirtualTime() {
        //given
        VirtualTimeScheduler.getOrSet();
        int stringLength = 3;

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_concatmap(stringLength).log();

        //then
        StepVerifier.withVirtualTime(()-> namesFlux)
                .thenAwait(Duration.ofSeconds(10))
                .expectNext("A","L","E","X","C","H","L","O","E")
                //.expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesMono_flatmap() {

        //given
        int stringLength = 3;

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesMono_flatmap(stringLength).log();

        //then
        StepVerifier.create(namesFlux)
                .expectNext(List.of("A", "L", "E", "X"))
                .verifyComplete();

    }
     
    /*
     * flatMapMany() in Mono
     *  Works similiar to flatMap
     *  Useful in Mono transformation logic returns a flux. Used to transform a Mono object into a Flux object. 
     *  DelayElements - It delays the publishing of each element by a defined duration
     * */
    @Test
    void namesMono_flatmapMany() {

        //given
        int stringLength = 3;

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesMono_flatmapMany(stringLength).log();

        //then
        StepVerifier.create(namesFlux)
                 .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();

    }


    @Test
    void namesFlux_transform() {

        //given
        int stringLength = 3;

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform(stringLength).log();

        //then
        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X")
                .expectNextCount(5)
                .verifyComplete();

    }

    @Test
    void namesFlux_transform_1() {

        //given
        int stringLength = 6;

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform(stringLength).log();

        //then
        StepVerifier.create(namesFlux)
                .expectNext("default")
                //.expectNextCount(5)
                .verifyComplete();

    }

    @Test
    void namesFlux_transform_switchIfEmpty() {

        //given
        int stringLength = 6;

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform_switchIfEmpty(stringLength).log();

        //then
        StepVerifier.create(namesFlux)
                .expectNext("D", "E", "F", "A", "U", "L", "T")
                //.expectNextCount(5)
                .verifyComplete();

    }
    
    /*
     * concat() and concatWith()
     * 	Used to combine two reactive streams into one
     * 	Concatenation of reactice streams happens in a sequence
     * 		First one is subscribed first and completes
     * 		Second one subscribed after that and then completes
     * 
     * 	concat() is a static method in Flux
     * 	concatWith() is a instance method in Flux and Mono
     *  Both are similiar
     * 
     * */

    @Test
    void namesFlux_transform_concatwith() {

        //given
        int stringLength = 3;

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform_concatwith(stringLength).log();

        //then
        StepVerifier.create(namesFlux)
                //.expectNext("ALEX", "BEN", "CHLOE")
                .expectNext("4-ALEX", "5-CHLOE", "4-ANNA")
                .verifyComplete();

    }

    @Test
    void name_defaultIfEmpty() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.name_defaultIfEmpty().log();

        //then
        StepVerifier.create(value)
                .expectNext("Default")
                .verifyComplete();

    }

    @Test
    void name_switchIfEmpty() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.name_switchIfEmpty().log();

        //then
        StepVerifier.create(value)
                .expectNext("Default")
                .verifyComplete();

    }

    @Test
    void explore_concat() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.explore_concat().log();

        //then
        StepVerifier.create(value)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();

    }


    @Test
    void explore_concatWith() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.explore_concatWith().log();

        //then
        StepVerifier.create(value)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();

    }

    @Test
    void explore_concat_mono() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.explore_concatWith_mono().log();

        //then
        StepVerifier.create(value)
                .expectNext("A", "B")
                .verifyComplete();

    }

    @Test
    void explore_merge() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.explore_merge();

        //then
        StepVerifier.create(value)
                // .expectNext("A", "B", "C", "D", "E", "F")
                .expectNext("A", "D", "B", "E", "C", "F")
                .verifyComplete();

    }

    /*
     * merge() and mergeWith()
     * 	Both the publishers are subscribed at the same time
     * 		Publishers are subscribed eagerly and the merge happens in interleaved fashion
     * 
     * 	concat() subscribes to the publishers in a sequence
     * 	
     * 
     * 	merge() is a static method in Flux
     * 	mergeWith() is a instance method in Flux and Mono
     *  Both are similiar
     * */
    @Test
    void explore_mergeWith() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.explore_mergeWith();

        //then
        StepVerifier.create(value)

                .expectNext("A", "D", "B", "E", "C", "F")
                .verifyComplete();

    }

    @Test
    void explore_mergeWith_mono() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.explore_mergeWith_mono();

        //then
        StepVerifier.create(value)

                .expectNext("A", "B")
                .verifyComplete();

    }

    /*
     * mergeSequential()
     * 	Used to combine two publishers(Flux) into one
     *  Static method flux
     *  Both the publishers are subscribed at the same time
     *  	Publishers are subscribed eagerly
     *  	Eventhough the publishers are subscribed eagerly the merge happens in a sequence     *  
     * */
    @Test
    void explore_mergeSequential() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.explore_mergeSequential();

        //then
        StepVerifier.create(value)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();

    }
    
    /*
     * Combining multiple publishers using Zip and Zipwith
     * 
     * zip() and zipWith()
     * 
     * zip()
     * 	Static method in Flux
     * 	Can be used to merge upto 2 to 8 publushers (Flux or Mono) in to one
     * 
     * zipWith()
     * 	It is an instance method in both Flux and Mono
     * 	Used to merge two publishers into one
     * 
     * Publishers are subscribed eagerly
     * Waits for all the publishers involved in the transformation to emit one element
     * 		Continues until one publisher sends an onComplete event
     * 	
     * */
    
    @Test
    void explore_zip() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.explore_zip().log();

        //then
        StepVerifier.create(value)
                .expectNext("AD", "BE", "CF")
                .verifyComplete();

    }

    @Test
    void explore_zip_1() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.explore_zip_1().log();

        //then
        StepVerifier.create(value)
                .expectNext("AD14", "BE25")
                //.expectNext("AD14", "BE25", "CF36")
                .verifyComplete();

    }


    @Test
    void explore_zip_2() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.explore_zip_2().log();

        //then
        StepVerifier.create(value)
                .expectNext("AB")
                .verifyComplete();

    }

    @Test
    void explore_zipWith() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.explore_zipWith().log();

        //then
        StepVerifier.create(value)
                .expectNext("AD", "BE", "CF")
                .verifyComplete();

    }

    @Test
    void explore_zipWith_mono() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.explore_zipWith_mono().log();

        //then
        StepVerifier.create(value)
                .expectNext("AB")
                .verifyComplete();

    }

}