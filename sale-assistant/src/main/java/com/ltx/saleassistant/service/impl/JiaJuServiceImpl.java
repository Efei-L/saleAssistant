package com.ltx.saleassistant.service.impl;

import com.ltx.saleassistant.mapper.JiaJuMapper;
import com.ltx.saleassistant.domain.jiajuEntity.JiaJu;
import com.ltx.saleassistant.service.JiaJuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JiaJuServiceImpl implements JiaJuService {
    @Autowired
    private JiaJuMapper jiaJuMapper;

    /**
     * 增
     *
     * @param jiaJu
     * @Param: [user]
     * @return: java.lang.String
     * @Author: MaSiyi
     * @Date: 2021/12/11
     */
    @Override
    public String insert(JiaJu jiaJu) {
        jiaJuMapper.insert(jiaJu);
        return "ok";
    }

    /**
     * 改
     *
     * @param jiaJu
     * @Param: [user]
     * @return: java.lang.String
     * @Author: MaSiyi
     * @Date: 2021/12/11
     */
    @Override
    public String update(JiaJu jiaJu) {
        jiaJuMapper.update(jiaJu,null);
        return "ok";
    }

    /**
     * 查
     *
     * @param id
     * @Param: [id]
     * @return: java.lang.String
     * @Author: MaSiyi
     * @Date: 2021/12/11
     */
    @Override
    public JiaJu getOne(String id) {
        return jiaJuMapper.selectById(id);
    }

    /**
     * 删
     *
     * @param id
     * @Param: [id]
     * @return: java.lang.String
     * @Author: MaSiyi
     * @Date: 2021/12/11
     */
    @Override
    public String delete(String id) {
        jiaJuMapper.deleteById(id);
        return "ok";
    }
}
