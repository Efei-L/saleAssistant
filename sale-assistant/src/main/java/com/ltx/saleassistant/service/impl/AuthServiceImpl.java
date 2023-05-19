package com.ltx.saleassistant.service.impl;

import com.alibaba.fastjson.JSON;
import com.ltx.saleassistant.domain.DTO.DialogDTO;
import com.ltx.saleassistant.domain.jiajuEntity.Dialog;
import com.ltx.saleassistant.service.AuthService;
import com.ltx.saleassistant.service.StatictisService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {
    @Value("${my-setting.tokenExpireTime}")
    private Integer tokenExpireTime;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    StatictisService statictisService;
    @Override
    public boolean judgeChatAuth(DialogDTO dialogDTO) {
        Integer count = Integer.valueOf(stringRedisTemplate.opsForValue().get("rest:dialog"));
        if(count == null || count <= 0){
            return false;
        }
        String client_token = dialogDTO.getToken();
        String restCount = stringRedisTemplate.opsForValue().get(client_token);
        if(client_token == null){
            return false;
        }
        if(restCount == null){
            return false;
        }
        Integer restCountNum = Integer.valueOf(restCount);
        if(restCountNum <= 0){
            String objectJson = stringRedisTemplate.opsForValue().get("dialog:record:"+dialogDTO.getToken());
            DialogDTO recordDialog = JSON.parseObject(objectJson, DialogDTO.class);
            if(recordDialog.getStore() != 1){
                statictisService.storeSummary(dialogDTO);
            }
            RLock mylock = redissonClient.getLock("radis:lock:inc");
            try {
                mylock.lock(30, TimeUnit.SECONDS);
                stringRedisTemplate.opsForValue().increment("rest:dialog");
                stringRedisTemplate.delete(client_token);
                return false;
            }finally {
                mylock.unlock();
            }
        }
        if(client_token != null && restCount!= null && restCountNum-1>=0){
            RLock declock = redissonClient.getLock("radis:lock:dec");
            try {
                declock.lock(30,TimeUnit.SECONDS);
                stringRedisTemplate.opsForValue().decrement(client_token);
                stringRedisTemplate.expire(client_token, tokenExpireTime, TimeUnit.MINUTES);
            }finally {
                declock.unlock();
            }
            return true;
        }
        return false;
    }
}
