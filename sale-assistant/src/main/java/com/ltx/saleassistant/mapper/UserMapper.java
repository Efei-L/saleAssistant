package com.ltx.saleassistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ltx.saleassistant.domain.jiajuEntity.Prompt;
import com.ltx.saleassistant.domain.userEntity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
