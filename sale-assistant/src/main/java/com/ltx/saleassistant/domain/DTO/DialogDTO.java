package com.ltx.saleassistant.domain.DTO;

import com.alibaba.fastjson.JSONObject;
import lombok.*;

import java.util.List;
@Data
//@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DialogDTO {
    private String jiajuId;
    private List<JSONObject> jsonList;
    private String token;
    private Integer store;
}
