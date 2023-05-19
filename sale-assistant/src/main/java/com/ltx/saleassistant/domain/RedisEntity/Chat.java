package com.ltx.saleassistant.domain.RedisEntity;

import com.alibaba.fastjson.JSONObject;
import io.swagger.models.auth.In;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Chat {
    private String jiajuId;
    private List<JSONObject> chatRecord;
}
