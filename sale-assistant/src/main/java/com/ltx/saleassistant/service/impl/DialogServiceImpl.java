package com.ltx.saleassistant.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ltx.saleassistant.domain.DTO.DialogDTO;
import com.ltx.saleassistant.domain.RedisEntity.Chat;
import com.ltx.saleassistant.domain.jiajuEntity.Dialog;
import com.ltx.saleassistant.domain.jiajuEntity.JiaJu;
import com.ltx.saleassistant.domain.jiajuEntity.Prompt;
import com.ltx.saleassistant.enums.Result;
import com.ltx.saleassistant.mapper.JiaJuMapper;
import com.ltx.saleassistant.mapper.PromptMapper;
import com.ltx.saleassistant.service.DialogService;
import com.ltx.saleassistant.utils.PythonUtils;
import io.swagger.models.auth.In;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class DialogServiceImpl implements DialogService {
    @Autowired
    JiaJuMapper jiaJuMapper;
    @Autowired
    PromptMapper promptMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Value("${my-setting.maxDialogCount:}")
    private String maxDialogCount;

    @Value("${my-setting.dialogRecordExpireTime:}")
    private Integer dialogRecordExpireTime;

    @Value("${my-setting.tokenExpireTime:}")
    private Integer tokenExpireTime;
    @Override
    public Result initialChat(Dialog dialog) {
        String bufferDialog = stringRedisTemplate.opsForValue().get("dialog:record:"+dialog.getToken());
        DialogDTO dialogDTO = JSON.parseObject(bufferDialog, DialogDTO.class);
        if(!ObjectUtils.isEmpty(dialogDTO)){
            dialog.setJsonList(dialogDTO.getJsonList().subList(1,dialogDTO.getJsonList().size()));
            dialog.setToken(dialogDTO.getToken());
            dialog.setJiajuId(dialogDTO.getJiajuId());
            return Result.success(dialog);
        }
        RLock mylock = redissonClient.getLock("radis:lock:dec");
        try {
            //没资源就返回
            boolean hasRest = dialogRest();
            if(!hasRest){
                return Result.error("502","当前访问人数过多！");
            }
            //有资源，判断是否为除此请求或者过期token
            String token = dialog.getToken();
            String uuid = token;
            if(!StringUtils.hasText(token) || stringRedisTemplate.opsForValue().get(token) == null){
                uuid = "client-token:" + UUID.randomUUID();
                stringRedisTemplate.opsForValue().set(uuid, maxDialogCount, tokenExpireTime, TimeUnit.MINUTES);
                //设置初始对话记录
                stringRedisTemplate.opsForValue().set("dialog:record:"+uuid, "", dialogRecordExpireTime, TimeUnit.MINUTES);
                //过期或者是第一次初始化，就将总数减一
                try{
                    mylock.lock(30,TimeUnit.SECONDS);
                    stringRedisTemplate.opsForValue().decrement("rest:dialog");

                }finally {
                    mylock.unlock();
                }
            }


            Prompt promptObject = promptMapper.selectById(dialog.getJiajuId());
            if(!ObjectUtils.isEmpty(promptObject)){
                return Result.success(uuid);
            }
            JiaJu jiaJu = jiaJuMapper.selectById(dialog.getJiajuId());
            if(ObjectUtils.isEmpty(jiaJu)){
                return Result.error("500","家具不存在");
            }
            String type = jiaJu.getType();
            String name = jiaJu.getName();
            String color = jiaJu.getColor();
            String size = jiaJu.getSize();
            Integer price = jiaJu.getPrice();
            String cailiao = jiaJu.getCailiao();
            String zheko = jiaJu.getZheko();
            String tese = jiaJu.getTese();
            String brand = jiaJu.getBrand();
            String xiaoshouMethod = jiaJu.getXiaoshouMethod();
            String zhuyi = jiaJu.getZhuyi();
            String zhekoStr = StringUtils.hasText(zheko)?"折扣信息："+zheko:"";
            String teseStr = StringUtils.hasText(tese)?"、家具特色信息":"";
            String xiaoshouMethodStr =StringUtils.hasText(xiaoshouMethod)?"、销售方法":"";
            String zhuyiStr = StringUtils.hasText(zhuyi)?"、注意事项":"";
            String imgUrl = jiaJu.getImgUrl();
            String iniStr = "你是一个家具销售员，你需要向顾客推销一款"+type+"，在此之前，你需要先了解一下家具详细信息"+teseStr+xiaoshouMethodStr+zhuyiStr+"：\n";
            String introStr = "详细信息：这款"+type+"的名字是"+name+",它的生产厂商是："+ brand + "，颜色是"+color+"色"+"尺寸信息："+size+"，" +
                    "材料信息："+cailiao+",售价："+price+"，"+zhekoStr+"。";
            String finalStr = iniStr + introStr + teseStr.replace("、","").replace("特色信息","特色信息：") + "\n" + tese + "\n" +
                    xiaoshouMethodStr.replace("、","").replace("方法","方法：") + "\n" + xiaoshouMethod + "\n" +
                    zhuyiStr.replace("、","").replace("事项","事项：") + "\n" + zhuyi +"\n" +
                    "若用户想要查看家具图片，则只需要回复图片的URL地址，不要回复其他多余的字。图片地址："+imgUrl;
            Prompt prompt = new Prompt(dialog.getJiajuId(), finalStr);
            promptMapper.insert(prompt);
            return Result.success(uuid);
        }catch (Exception e) {
            return Result.error("500", e.getMessage());
        }

    }

    @Override
    public Result getChat(DialogDTO dialogDTO) {
        try {
            JSONObject lastDialog = dialogDTO.getJsonList().get(dialogDTO.getJsonList().size() - 1);
            if(lastDialog.get("content").toString().length()>=50){
                return Result.error("500","问题长度请不要超过50");
            }
            Prompt initialPrompt = promptMapper.selectById(dialogDTO.getJiajuId());
            List<JSONObject> chatList = new ArrayList<>();
            JSONObject initialJson = new JSONObject();
            initialJson.put("role","system");
            initialJson.put("content",initialPrompt.getPrompt());
            chatList.add(initialJson);
            chatList.addAll(dialogDTO.getJsonList());
            MultiValueMap<String, List<JSONObject>> params = new LinkedMultiValueMap<>();
            params.set("chatList",chatList);
            String resStr = PythonUtils.sendPOSTRequest("http://127.0.0.1:5000/getChat",params);
            //将本轮对话作为历史记录加入redis
            JSONObject resJson = new JSONObject();
            resJson.put("role","assistant");
            resJson.put("content", resStr);
            chatList.add(resJson);
            DialogDTO chatRecord = new DialogDTO();
            chatRecord.setJsonList(chatList);
            chatRecord.setJiajuId(dialogDTO.getJiajuId());
            chatRecord.setToken(dialogDTO.getToken());
            chatRecord.setStore(0);
            stringRedisTemplate.opsForValue().set("dialog:record:"+dialogDTO.getToken(), JSON.toJSONString(chatRecord));
            stringRedisTemplate.expire("dialog:record:"+dialogDTO.getToken(),dialogRecordExpireTime,TimeUnit.MINUTES);
            //end
            JSONObject chatgptRes = new JSONObject();
            chatgptRes.put("role","assistant");
            chatgptRes.put("content",resStr);
            dialogDTO.getJsonList().add(chatgptRes);
            return Result.success(dialogDTO.getJsonList());
        }catch (Exception e){
            return Result.error("500", e.getMessage());
        }
    }
    private boolean dialogRest(){

        Integer restCount = Integer.valueOf(stringRedisTemplate.opsForValue().get("rest:dialog"));
        if(restCount <= 0){
            return false;
        }
        return true;

    }

}
