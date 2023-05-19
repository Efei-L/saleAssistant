package com.ltx;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltx.saleassistant.mapper.JiaJuMapper;
import com.ltx.saleassistant.domain.jiajuEntity.JiaJu;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
@Slf4j
public class testr {
    @Autowired
    private  StringRedisTemplate stringRedisTemplate;
    @Test
    public void testPage(){
        stringRedisTemplate.opsForValue().set("today:count","0");
    }
    @Test
    public void test(){
        /*
            必须要减一，因为美国一周是从星期天开始，到星期六结束，
            数组下标从0开始，内容是{1,2,3,4,5,6,7}，
            分别表示：星期天，星期一，星期二，星期三，星期四，星期五，星期六 。如果当天是星期一会返回2，所以减一，才等于1，才是你想要的，如果是星期天就返回1-1=0
         */
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        System.out.println(calendar.get(Calendar.DAY_OF_WEEK)-1 == 0);

    }
    @Test
    public void isFirstDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        System.out.println(calendar.get(Calendar.DAY_OF_MONTH));

    }
}
