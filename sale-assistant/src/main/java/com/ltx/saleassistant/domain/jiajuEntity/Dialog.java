package com.ltx.saleassistant.domain.jiajuEntity;

import com.alibaba.fastjson.JSONObject;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
//@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Dialog {

    private String jiajuId;
    private List<JSONObject> jsonList;
    private String token;
}
