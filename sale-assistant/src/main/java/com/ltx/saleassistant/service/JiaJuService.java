package com.ltx.saleassistant.service;

import com.ltx.saleassistant.domain.jiajuEntity.JiaJu;
import org.springframework.stereotype.Service;

@Service
public interface JiaJuService {
    public String insert(JiaJu jiaJu);
    public String update(JiaJu jiaJu);
    public JiaJu getOne(String id);
    public String delete(String id);

}
