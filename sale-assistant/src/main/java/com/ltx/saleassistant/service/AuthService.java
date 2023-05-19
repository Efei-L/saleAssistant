package com.ltx.saleassistant.service;

import com.ltx.saleassistant.domain.DTO.DialogDTO;
import com.ltx.saleassistant.domain.jiajuEntity.Dialog;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    public boolean judgeChatAuth(DialogDTO dialogDTO);
}
