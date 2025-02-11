package com.tjtechy.CacheWithRedisInSpringBootApi.product.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

/**
 * starts on application startup
 * Responsible for configuring Redis cache.
 * Uses Jackson to serialize and deserialize objects.
 * Responsible for converting Java objects to JSON and vice versa.
  * Resolve the issue of serialization and deserialization of Java 8 Date/Time API in Redis.
 */
@Configuration
public class RedisCacheConfig {
    //Redis cache configuration
  @Bean
  public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {

    //custom object with JavaTimeModule
    var objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());// Enables LocalDateTime serialization
    objectMapper.activateDefaultTyping(
            objectMapper.getPolymorphicTypeValidator(),
            ObjectMapper.DefaultTyping.NON_FINAL,
            JsonTypeInfo.As.PROPERTY
    );// Enables type information


    // Create serializer with the ObjectMapper in constructor
    Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

    // Redis cache configuration
    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(1))
            .disableCachingNullValues()
            .serializeValuesWith(
                    RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer)
            );
    return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(redisCacheConfiguration)
            .build();
  }
}
