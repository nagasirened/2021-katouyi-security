package com.katouyi.securitytest.config.valid.code.po;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * author: ZGF
 * context : 验证码
 */

@Data
public class ValidateCode implements Serializable {

    private String code;

    private LocalDateTime expire;

    public ValidateCode(String code, Integer expireTimeLong) {
        this.code = code;
        this.expire = LocalDateTime.now().plusSeconds(expireTimeLong);
    }

    public ValidateCode() { }

    public ValidateCode(String code, LocalDateTime expire) {
        this.code = code;
        this.expire = expire;
    }

    public boolean isExpire(){
        return LocalDateTime.now().isAfter(expire);
    }

}
