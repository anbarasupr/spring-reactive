package com.example.pricing.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(enableKeyspaceEvents = EnableRedisRepositories.EnableKeyspaceEvents.ON_STARTUP)
public class RedisConfig {}
