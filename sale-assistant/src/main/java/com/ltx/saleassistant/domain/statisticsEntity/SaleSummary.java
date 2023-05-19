package com.ltx.saleassistant.domain.statisticsEntity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.models.auth.In;
import lombok.*;

import java.util.Date;

@Data
@TableName(value = "sale_summary")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SaleSummary {
    @Generated
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String jiajuId;
    private String type;
    private String saleDate;
    private String summary;
    private String imgUrl;
}
