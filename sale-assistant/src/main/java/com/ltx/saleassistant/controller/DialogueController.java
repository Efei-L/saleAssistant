package com.ltx.saleassistant.controller;

import com.ltx.saleassistant.domain.DTO.DialogDTO;
import com.ltx.saleassistant.domain.jiajuEntity.Dialog;
import com.ltx.saleassistant.enums.Result;
import com.ltx.saleassistant.mapper.JiaJuMapper;
import com.ltx.saleassistant.mapper.PromptMapper;
import com.ltx.saleassistant.service.AuthService;
import com.ltx.saleassistant.service.DialogService;
import com.ltx.saleassistant.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dialog")
public class DialogueController {
    @Autowired
    JiaJuMapper jiaJuMapper;
    @Autowired
    private DialogService dialogService;
    @Autowired
    PromptMapper promptMapper;

    @Autowired
    private VisitService visitService;

    @Autowired
    private AuthService authService;


    @PostMapping("/initialDiaLog")
    public Result initialDiaLog(Dialog dialog) {
        visitService.record(dialog);
        Result result = dialogService.initialChat(dialog);
        return result;

    }

    @PostMapping("/getChatMessage")
    public Result getChatMessage(DialogDTO dialogDTO) {
        boolean auth = authService.judgeChatAuth(dialogDTO);
        if(!auth){
            return Result.error("500","会话已经结束");
        }

        Result result = dialogService.getChat(dialogDTO);
        return result;
    }
}
