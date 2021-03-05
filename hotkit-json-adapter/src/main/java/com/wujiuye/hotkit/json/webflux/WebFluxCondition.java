package com.wujiuye.hotkit.json.webflux;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author wujiuye 2020/10/28
 */
public class WebFluxCondition implements Condition {

    private final static boolean IS_WEBMVC;

    static {
        boolean flag;
        try {
            Class.forName("org.springframework.web.reactive.config.WebFluxConfigurer");
            flag = true;
        } catch (ClassNotFoundException e) {
            flag = false;
        }
        IS_WEBMVC = flag;
    }

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return IS_WEBMVC;
    }

}
