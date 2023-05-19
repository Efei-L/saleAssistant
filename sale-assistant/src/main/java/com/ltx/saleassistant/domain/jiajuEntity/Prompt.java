package com.ltx.saleassistant.domain.jiajuEntity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@TableName(value = "prompt")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Prompt {
    @TableId
    private String jiajuId;
    @NotBlank
    private String prompt;
}
