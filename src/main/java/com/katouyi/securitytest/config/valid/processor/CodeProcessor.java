package com.katouyi.securitytest.config.valid.processor;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * author: ZGF
 * context : 验证码生成器
 */

public interface CodeProcessor {

    /**
     * 生成验证码
     * @throws Exception
     */
    void createValidateCode(ServletWebRequest request) throws Exception;
}
