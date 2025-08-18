package com.UniHUB.Server.config;

import jakarta.annotation.PostConstruct;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevConfig {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("studentCache", "allStudentsCache");
    }

    @PostConstruct
    public void init() {
        System.out.println("🔥 Dev Profile Active!");
    }
}
