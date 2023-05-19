package com.ltx.saleassistant.controller;

import com.alibaba.fastjson.JSONObject;
import com.ltx.saleassistant.domain.jiajuEntity.Dialog;
import com.ltx.saleassistant.domain.jiajuEntity.JiaJu;
import com.ltx.saleassistant.enums.Result;
import com.ltx.saleassistant.mapper.JiaJuMapper;
import com.ltx.saleassistant.utils.PythonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/statistics")
public class StastisticsController {
    @Autowired
    JiaJuMapper jiaJuMapper;
    @PostMapping("/genSummary")
    public Result genSummary(Dialog dialog) {

        return null;

    }
}
