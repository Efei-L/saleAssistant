package com.ltx.saleassistant.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class PythonUtils {
    public static String sendPOSTRequest(String url, MultiValueMap<String, List<JSONObject>> params) {
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.POST;
        // 以表单的方式提交
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 将请求头部和参数合成一个请求
        HttpEntity<MultiValueMap<String, List<JSONObject>>> requestEntity = new HttpEntity<>(params, headers);
        // 执行HTTP请求，将返回的结构使用String类格式化
        ResponseEntity<String> response = client.exchange(url, method, requestEntity, String.class);
        System.out.println(response.getBody());
        return response.getBody();
    }
}
