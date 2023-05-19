package com.ltx.saleassistant.domain.statisticsEntity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@TableName(value = "visit_statistics")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VisitStatistics {
    @TableId
    private Integer id;
    private Integer today;
    private Integer thisWeek;
    private Integer thisMonth;
    private Integer thisYear;
    private Integer total;
}
