package com.ltx.saleassistant.utils;

//import org.redisson.Redisson;
//import org.redisson.api.RLock;
//import org.redisson.api.RLock;
import com.alibaba.fastjson.JSON;
import com.ltx.saleassistant.domain.DTO.DialogDTO;
import com.ltx.saleassistant.domain.RedisEntity.Chat;
import com.ltx.saleassistant.domain.jiajuEntity.Dialog;
import com.ltx.saleassistant.service.StatictisService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 描述:
 * 1.开启过期key监听:配置文件redis.windows-service.conf 设置为Ex -> notify-keyspace-events "Ex"
 * 2.redis 配置类配置自定义过期事件监听器
 * 3.配置监听器 接收过期key
 * redis 过期事件监听器
 * 接收过期key
 * @author 闲走天涯
 * @create 2021/8/6 16:11
 */
@Component
public class RedisKeyExpireListener extends KeyExpirationEventMessageListener {
    public RedisKeyExpireListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }
    @Autowired
    RedissonClient redissonClient;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private StatictisService statictisService;

    @Override
    public void onMessage(Message message, byte[] pattern){
    	//key过期会触发
        //获取过期的key

        String key = message.toString();
        if(!key.startsWith("client-token")){
            return;
        }
        //若key过期，则将其对应的历史记录送入chatgpt生成总结，并记录到数据库
        String objectJson = stringRedisTemplate.opsForValue().get("dialog:record:"+key);
        DialogDTO dialogDTO = JSON.parseObject(objectJson, DialogDTO.class);
        if(!ObjectUtils.isEmpty(dialogDTO)){
            if(dialogDTO.getStore() != 1){
                statictisService.storeSummary(dialogDTO);
            }
        }
        //end

        //当有一个会话过期后将总数+1
        RLock mylock = redissonClient.getLock("radis:lock:inc");
        if(key.startsWith("client-token:")){
            try{

                mylock.lock(30,TimeUnit.SECONDS);
                stringRedisTemplate.opsForValue().increment("rest:dialog");

            }catch (Exception e){
                System.out.println(e.getMessage());
            }finally {
                mylock.unlock();
            }
        }
    }
}
