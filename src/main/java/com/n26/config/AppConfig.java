package com.n26.config;

import com.n26.cache.CacheManager;
import com.n26.cache.LocalCache;
import com.n26.dto.Statistics;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public CacheManager<String, Statistics> loadCache() {
        return new LocalCache<String, Statistics>();
    }
}
