package com.ltx.saleassistant.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ltx.saleassistant.domain.DTO.DialogDTO;
import com.ltx.saleassistant.domain.jiajuEntity.Dialog;
import com.ltx.saleassistant.domain.jiajuEntity.JiaJu;
import com.ltx.saleassistant.domain.jiajuEntity.Prompt;
import com.ltx.saleassistant.domain.statisticsEntity.SaleSummary;
import com.ltx.saleassistant.mapper.JiaJuMapper;
import com.ltx.saleassistant.mapper.PromptMapper;
import com.ltx.saleassistant.mapper.StatisticsMapper;
import com.ltx.saleassistant.service.StatictisService;
import com.ltx.saleassistant.utils.PythonUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class StatictisServiceImpl implements StatictisService {
    @Autowired
    JiaJuMapper jiaJuMapper;
    @Autowired
    PromptMapper promptMapper;
    @Autowired
    StatisticsMapper statisticsMapper;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Value("${my-setting.recordThreshold}")
    private Integer recordThreshold;
    @Override
    public String getSummaryByChatGPT(DialogDTO dialogDTO) {
        try {
            if(dialogDTO.getJsonList().size()<recordThreshold){
                return null;
            }

            Prompt initialPrompt = promptMapper.selectById(dialogDTO.getJiajuId());
            List<JSONObject> chatList = new ArrayList<>();
            JSONObject initialJson = new JSONObject();
            initialJson.put("role","system");
            initialJson.put("content",initialPrompt.getPrompt());
            chatList.add(initialJson);
            chatList.addAll(dialogDTO.getJsonList());
            //若最后一句是用户说的，则将这一句改变
            JSONObject lastDialog = dialogDTO.getJsonList().get(dialogDTO.getJsonList().size() - 1);
            if(lastDialog.get("role").equals("user")){
                chatList.get(chatList.size()-1).put("content", "请根据客户的对话内容帮我做一个100字以内的销售总结。");

            }
            //若最后一句是AI说的，则再加一句
            else{
                JSONObject sumWords = new JSONObject();
                sumWords.put("role","user");
                sumWords.put("content", "请根据以上对话内容帮我做一个100字以内的销售总结。");
                chatList.add(sumWords);
            }
            MultiValueMap<String, List<JSONObject>> params = new LinkedMultiValueMap<>();
            params.set("chatList",chatList);
            String resStr = PythonUtils.sendPOSTRequest("http://127.0.0.1:5000/getSummary",params);
            return resStr;
        }catch (Exception e){
            return e.getMessage();
        }
    }

    @Override
    public boolean storeSummary(DialogDTO dialogDTO) {
        try {
            RLock mylock = redissonClient.getLock("radis:lock:store");
            dialogDTO.setStore(1);
            try {
                mylock.lock(30, TimeUnit.SECONDS);
                stringRedisTemplate.opsForValue().set("dialog:record:"+dialogDTO.getToken(), JSON.toJSONString(dialogDTO));
            }finally {
                mylock.unlock();
            }
            JiaJu thisJiaju = jiaJuMapper.selectById(dialogDTO.getJiajuId());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
            String nowDate = sdf.format(calendar.getTime());
            String aiSummary = getSummaryByChatGPT(dialogDTO);

            SaleSummary saleSummary = new SaleSummary();
            saleSummary.setJiajuId(dialogDTO.getJiajuId());
            saleSummary.setSaleDate(nowDate);
            saleSummary.setImgUrl(thisJiaju.getImgUrl());
            saleSummary.setSummary(aiSummary);
            saleSummary.setType(thisJiaju.getType());
            statisticsMapper.insert(saleSummary);

            Boolean delete = stringRedisTemplate.delete("dialog:record:" + dialogDTO.getToken());
            if(!delete){
                System.out.println("历史对话记录删除失败。");
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
