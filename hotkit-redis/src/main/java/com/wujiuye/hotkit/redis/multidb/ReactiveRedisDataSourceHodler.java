package com.wujiuye.hotkit.redis.multidb;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 支持反应式API
 *
 * @author wujiuye 2020/11/04
 */
public final class ReactiveRedisDataSourceHodler {

    private final static String DB_KEY = "redis_db";

    public static Object doOnDatabase(Object mono, int db) {
        if (mono instanceof Mono) {
            return warpDatabase((Mono<?>) mono, db);
        } else if (mono instanceof Flux) {
            return warpDatabase((Flux<?>) mono, db);
        }
        return warpDatabase(Mono.from((Publisher<?>) mono), db);
    }

    private static <T> Mono<T> warpDatabase(Mono<T> mono, int db) {
        return mono.subscriberContext(context -> context.put(DB_KEY, db));
    }

    private static <T> Flux<T> warpDatabase(Flux<T> flux, int db) {
        return flux.subscriberContext(context -> context.put(DB_KEY, db));
    }

    public static Mono<Integer> currentDatabase() {
        return Mono.subscriberContext().handle((context, integerSynchronousSink) -> {
            if (context.hasKey(DB_KEY)) {
                integerSynchronousSink.next(context.get(DB_KEY));
            } else {
                integerSynchronousSink.next(0);
            }
        });
    }

}
