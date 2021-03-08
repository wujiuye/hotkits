package com.wujiuye.hotkit.spring.common.profile;

import com.wujiuye.hotkit.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.cloud.bootstrap.BootstrapApplicationListener.BOOTSTRAP_PROPERTY_SOURCE_NAME;

/**
 * 自定义EnvironmentPostProcessor
 *
 * @author wujiuye 2020/06/29
 */
public class K8sProfileEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    private final static Logger logger = LoggerFactory.getLogger(EnvironmentPostProcessor.class);
    private static final String COMMON_PROFILE = "common";

    /**
     * 让Spring Boot容器与Spring Boot都能够读取到
     */
    private final static Set<String> ACTIVE_PROFILES = new HashSet<>();

    private final static boolean WHERE_K8S_CONFIGMAP;
    private static volatile boolean ALERT_ADD_LISTENER = false;

    static {
        File file = new File(System.getProperty("user.home") + "/.kube/config");
        WHERE_K8S_CONFIGMAP = file.exists();
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // 如果当前是Spring Cloud的Bootstart容器
        if (environment.getPropertySources().contains(BOOTSTRAP_PROPERTY_SOURCE_NAME)) {
            PropertySource<?> propertySource = environment.getPropertySources().get("applicationConfig: [classpath:/bootstrap.yaml]");
            String activeProfiles = (String) propertySource.getProperty("spring.cloud.config.choose");
            logger.info("spring.cloud.config.choose：{}", activeProfiles);
            if (StringUtils.isNullOrEmpty(activeProfiles)) {
                return;
            }
            for (String ycpayPro : activeProfiles.split(",")) {
                String profile = COMMON_PROFILE + "-" + ycpayPro;
                ACTIVE_PROFILES.add(profile);
            }
            return;
        }
        // 如果不是测试环境（让其它环境从配置中心读取配置）
        for (String defautActiveProfiles : environment.getActiveProfiles()) {
            if ("dev".equalsIgnoreCase(defautActiveProfiles)) {
                if (WHERE_K8S_CONFIGMAP && !ALERT_ADD_LISTENER) {
                    ALERT_ADD_LISTENER = true;
                    application.addListeners((ApplicationListener<ContextRefreshedEvent>) event ->
                            logger.warn("⚠️⚠️⚠️警告：当前使用的配置来自ConfigMap，原因是你本地存在~/.kube/config文件...⚠️⚠️⚠️"));
                    return;
                }
                if (!hasProfile(environment, COMMON_PROFILE)) {
                    for (String profile : ACTIVE_PROFILES) {
                        Resource resource = new ClassPathResource(profile + ".yml");
                        environment.getPropertySources().addFirst(loadProfiles(resource));
                    }
                    // 添加一个Profile，标志有ProfileEnvironmentPostProcessor添加的配置
                    environment.addActiveProfile(COMMON_PROFILE);
                }
            }
        }
    }

    private boolean hasProfile(Environment environment, String profile) {
        for (String activeProfile : environment.getActiveProfiles()) {
            if (profile.equalsIgnoreCase(activeProfile)) {
                return true;
            }
        }
        return false;
    }

    private PropertySource<?> loadProfiles(Resource resource) {
        if (!resource.exists()) {
            throw new IllegalArgumentException("file" + resource + "not exist");
        }
        try {
            YamlPropertySourceLoader sourceLoader = new YamlPropertySourceLoader();
            List<PropertySource<?>> propertySources = sourceLoader.load(resource.getFilename(), resource);
            return propertySources.get(0);
        } catch (IOException ex) {
            throw new IllegalStateException("load resource exception" + resource, ex);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

}
