package com.ltx.saleassistant.service;

import com.ltx.saleassistant.domain.DTO.DialogDTO;
import com.ltx.saleassistant.domain.jiajuEntity.Dialog;
import com.ltx.saleassistant.domain.jiajuEntity.JiaJu;
import com.ltx.saleassistant.enums.Result;
import org.springframework.stereotype.Service;

@Service
public interface DialogService {
    public Result initialChat(Dialog dialog);

    public Result getChat(DialogDTO dialogDTO);


}
