/**
 * Tata Elxsi, A TATA Group. All rights reserved.
 */

package com.justin.cruddemos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.justin.cruddemos.dto.CustomerDTO;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String REDIS_HOSTNAME;
    @Value("${spring.redis.port}")
    private int REDIS_PORT;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        final RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(REDIS_HOSTNAME,
                REDIS_PORT);

        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String, CustomerDTO> redisTemplate() {
        final RedisTemplate<String, CustomerDTO> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

    // @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        final LettuceConnectionFactory lcf = new LettuceConnectionFactory(REDIS_HOSTNAME, REDIS_PORT);
        return lcf;
    }

}
