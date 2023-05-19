package com.ltx.saleassistant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ltx.saleassistant.domain.userEntity.User;
import com.ltx.saleassistant.domain.DTO.UserDTO;
import com.ltx.saleassistant.enums.Result;
import com.ltx.saleassistant.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserMapper userMapper;
    @PostMapping("/login")
    public Result login(UserDTO userDTO){
        QueryWrapper qw = new QueryWrapper();
        qw.eq("user_name", userDTO.getUserName());
        qw.eq("password",userDTO.getPassword());
        User user1 = userMapper.selectOne(qw);
        if(!ObjectUtils.isEmpty(user1)){
            String uuid = "user-token:" + UUID.randomUUID();
            stringRedisTemplate.opsForValue().set(uuid, user1.getUserName().toString(), 30, TimeUnit.MINUTES);
            return Result.success(uuid);
        }
        return Result.error("500","用户名或密码错误！");

    }
}
