package com.reader.manga.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ConfigBeans {

    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

}
