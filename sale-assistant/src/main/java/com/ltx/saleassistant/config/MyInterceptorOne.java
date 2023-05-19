//package com.ltx.saleassistant.config;
//
//import com.ltx.saleassistant.service.StatictisService;
//import org.apache.poi.ss.formula.functions.T;
//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.concurrent.TimeUnit;
//
//
//@Component
//public class MyInterceptorOne implements HandlerInterceptor {
//
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//
//    @Autowired
//    RedissonClient redissonClient;
//
//    @Autowired
//    StatictisService statictisService;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        Integer count = Integer.valueOf(stringRedisTemplate.opsForValue().get("rest:dialog"));
//        if(count == null || count <= 0){
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            return false;
//        }
//        String client_token = request.getHeader("client_token");
//        String restCount = stringRedisTemplate.opsForValue().get(client_token);
//        if(client_token == null){
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            return false;
//        }
//        if(restCount == null){
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            return false;
//        }
//        Integer restCountNum = Integer.valueOf(restCount);
//        if(restCountNum <= 0){
//            RLock mylock = redissonClient.getLock("radis:lock:inc");
//            try {
//                mylock.lock(30, TimeUnit.SECONDS);
//                stringRedisTemplate.opsForValue().increment("rest:dialog");
//                stringRedisTemplate.delete(client_token);
//                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//                return false;
//            }finally {
//                mylock.unlock();
//            }
//
//
//        }
//        if(client_token != null && restCount!= null && restCountNum-1>=0){
//            RLock declock = redissonClient.getLock("radis:lock:dec");
//            try {
//                declock.lock(30,TimeUnit.SECONDS);
//                stringRedisTemplate.opsForValue().set(client_token, String.valueOf(restCountNum - 1));
//                stringRedisTemplate.expire(client_token, 1, TimeUnit.MINUTES);
//            }finally {
//                declock.unlock();
//            }
//            return true;
//        }
//        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//        return false;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
//    }
//}
//
