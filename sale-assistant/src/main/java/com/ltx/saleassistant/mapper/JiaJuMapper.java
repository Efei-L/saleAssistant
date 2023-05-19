package com.ltx.saleassistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ltx.saleassistant.domain.jiajuEntity.JiaJu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface JiaJuMapper extends BaseMapper<JiaJu> {
    Long selectCount();
//    @Select(" select count(*) from sale ")
//    public int count();
}
