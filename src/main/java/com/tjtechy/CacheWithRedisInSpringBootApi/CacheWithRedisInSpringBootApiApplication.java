package com.tjtechy.CacheWithRedisInSpringBootApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class CacheWithRedisInSpringBootApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(CacheWithRedisInSpringBootApiApplication.class, args);
	}

}
