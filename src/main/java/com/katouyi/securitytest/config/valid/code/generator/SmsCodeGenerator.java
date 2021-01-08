package com.katouyi.securitytest.config.valid.code.generator;

import com.katouyi.securitytest.config.valid.code.po.ValidateCode;
import com.katouyi.securitytest.config.properties.SecurityProperties;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * author: ZGF
 * 01-2021/1/7 : 14:32
 * context :
 */

public class SmsCodeGenerator implements ValidateCodeGenerator {

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 随机生成验证码，可控制验证码长度
     * @param request
     * @return
     */
    @Override
    public ValidateCode generateCode(ServletWebRequest request) {
        String randomNumeric = RandomStringUtils.randomNumeric(securityProperties.getValid().getValidateCodeLength());
        return new ValidateCode(randomNumeric, securityProperties.getValid().getExpireTimeLong());
    }
}
