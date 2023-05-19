package com.ltx.saleassistant.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.io.Serializable;

/**
 * 描述:
 * 默认情况下的模板只能支持 RedisTemplate<String,String>，
 * 只能存入字符串，很多时候，我们需要自定义 RedisTemplate ，设置序列化器，这样我们可以很方便的操作实例对象。
 *
 * 开启过期key监听:配置文件redis.windows-service.conf 设置为Ex -> notify-keyspace-events "Ex"
 * @author 闲走天涯
 * @create 2021/8/6 14:15
 */
@Configuration
public class RedisConfig {

    /**
     * 定义事件监听器
     * 用于监听redis中key过期事件触发
     * @param connectionFactory
     * @return
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory){
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(connectionFactory);
        return redisMessageListenerContainer;
    }
}
