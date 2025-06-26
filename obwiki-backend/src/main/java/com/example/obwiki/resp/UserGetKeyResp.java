package com.example.obwiki.resp;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserGetKeyResp {
    private String salt;
}
