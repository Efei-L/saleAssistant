package com.ltx.saleassistant.job;

import com.ltx.saleassistant.domain.statisticsEntity.VisitStatistics;
import com.ltx.saleassistant.mapper.VisitMapper;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class TimerTasks {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    VisitMapper visitMapper;
    @Scheduled(cron = "0 0 1 * * ?")
    public void updateVisitedCount() {
        String todayCountRedis = stringRedisTemplate.opsForValue().get("today:visit");
        if(todayCountRedis == null){
            return;
        }
        VisitStatistics visitInfo = visitMapper.selectById(1);
        if(ObjectUtils.isEmpty(visitInfo)){
            return;
        }
        Integer dayCount = Integer.valueOf(todayCountRedis);
        Integer today = 0;
        Integer week = visitInfo.getThisWeek();
        Integer month = visitInfo.getThisMonth();
        Integer year = visitInfo.getThisYear();
        Integer total = visitInfo.getTotal();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        week += dayCount;
        if(calendar.get(calendar.DAY_OF_WEEK)-1 == 1){
            week = 0;
        }
        month += dayCount;
        if(calendar.get(calendar.DAY_OF_MONTH) == 1){
            month = 0;
        }
        year += dayCount;
        if(calendar.get(calendar.DAY_OF_YEAR) == 1){
            year = 0;
        }
        total = total + dayCount;
        visitInfo.setToday(today);
        visitInfo.setThisWeek(week);
        visitInfo.setThisMonth(month);
        visitInfo.setThisYear(year);
        visitInfo.setTotal(total);
        visitMapper.updateById(visitInfo);
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("定时任务执行，当前时间："+sdf.format(calendar.getTime()));
        System.out.println(visitInfo);
        //将每日redis中的统计访问量归零
        stringRedisTemplate.opsForValue().set("today:visit","0");
    }

}
