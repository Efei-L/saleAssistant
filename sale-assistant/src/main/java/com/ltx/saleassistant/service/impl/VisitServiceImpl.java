package com.ltx.saleassistant.service.impl;

import com.ltx.saleassistant.domain.jiajuEntity.Dialog;
import com.ltx.saleassistant.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class VisitServiceImpl implements VisitService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public void record(Dialog dialog) {
        if (StringUtils.hasText(dialog.getToken()) && stringRedisTemplate.opsForValue().get(dialog.getToken())!=null){
            return;
        }
        boolean absentBoolean = stringRedisTemplate.opsForValue().setIfAbsent("today:visit","0");
        if(!absentBoolean){
            stringRedisTemplate.opsForValue().increment("today:visit");
        }
    }
}
