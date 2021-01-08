package com.katouyi.securitytest.config.valid.code.generator;


import com.katouyi.securitytest.config.valid.code.po.ValidateCode;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 生成验证码
 */
public interface ValidateCodeGenerator {

    public ValidateCode generateCode(ServletWebRequest request);
}
