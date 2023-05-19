package com.ltx.saleassistant.domain.jiajuEntity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@TableName(value = "sale")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class JiaJu {
    @TableId
    private String jiajuId;

    @Length(max=20, message = "家具名称长度不能超过20")
    @NotNull(message  = "家具名称不能为空！")
    @NotBlank
    private String name;

    @Length(max = 20, message = "家具品牌长度不能超过20")
    @NotNull(message = "家具品牌信息不能为空")
    @NotBlank
    private String brand;

    @Length(max=20, message = "家具类型长度不能超过20")
    @NotNull(message = "家具类型不能为空！")
    @NotBlank
    private String type;

    @Length(max=50, message = "家具材料描述长度不能超过20")
    @NotNull(message = "家具材料不能为空！")
    @NotBlank
    private String cailiao;

    @Length(max=5, message = "家具颜色描述长度不能超过5")
    @NotNull(message = "家具颜色不能为空！")
    @NotBlank
    private String color;

    @Length(max=50, message = "家具尺寸文本长度不能超过20")
    @NotNull(message = "家具尺寸不能为空！")
    @NotBlank
    private String size;


    @NotNull(message = "家具价格不能为空！")
    private int price;

    @Length(max=100, message = "折扣信息长度不能超过20")
    private String zheko;

    @Length(max=100, message = "特色描述信息长度不能超过40")
    private String tese;

    @Length(max=512, message = "销售方法长度不能超过512")
    private String xiaoshouMethod;

    @Length(max=512, message = "注意事项长度不能超过512")
    private String zhuyi;

    private String imgUrl;



}
