package com.ltx.saleassistant.service;

import com.ltx.saleassistant.domain.jiajuEntity.Dialog;
import org.springframework.stereotype.Service;

@Service
public interface VisitService {
    public void record(Dialog dialog);
}
