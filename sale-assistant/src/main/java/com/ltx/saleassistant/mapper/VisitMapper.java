package com.ltx.saleassistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ltx.saleassistant.domain.statisticsEntity.VisitStatistics;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VisitMapper extends BaseMapper<VisitStatistics> {
}
