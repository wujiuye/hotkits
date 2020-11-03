package com.wujiuye.hotkit.r2dbc;

import com.wujiuye.hotkit.r2dbc.mode.BaseModeConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.data.r2dbc.connectionfactory.lookup.AbstractRoutingConnectionFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * ConnectionFactory路由
 *
 * @author wujiuye 2020/11/03
 */
public class HotkitR2dbcRoutingConnectionFactory extends AbstractRoutingConnectionFactory {

    private final static String DB_KEY = "HOTKIT-R2DBC-DB";

    public HotkitR2dbcRoutingConnectionFactory(BaseModeConnectionFactory modeConnectionFactory) {
        Map<String, ConnectionFactory> connectionFactoryMap = modeConnectionFactory.build();
        setTargetConnectionFactories(connectionFactoryMap);
        ConnectionFactory defaultTarget = connectionFactoryMap.get(modeConnectionFactory.getDefaultDataBase());
        setDefaultTargetConnectionFactory(defaultTarget);
    }

    public static <T> Mono<T> warpDataSource(Mono<T> mono, String dataSource) {
        return Mono.subscriberContext().flatMap(context -> mono)
                .subscriberContext(context -> context.put(DB_KEY, dataSource));
    }

    public static <T> Flux<T> warpDataSource(Flux<T> flux, String dataSource) {
        return Mono.subscriberContext().flatMapMany(context -> flux)
                .subscriberContext(context -> context.put(DB_KEY, dataSource));
    }

    @Override
    protected Mono<Object> determineCurrentLookupKey() {
        return Mono.subscriberContext().flatMap(context -> {
            if (context.hasKey(DB_KEY)) {
                return Mono.just(context.get(DB_KEY));
            }
            // 使用默认数据源
            return Mono.empty();
        });
    }

}
