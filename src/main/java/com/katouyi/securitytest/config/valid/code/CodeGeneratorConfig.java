package com.katouyi.securitytest.config.valid.code;

import com.katouyi.securitytest.config.valid.code.generator.ImageCodeGenerator;
import com.katouyi.securitytest.config.valid.code.generator.SmsCodeGenerator;
import com.katouyi.securitytest.config.valid.code.generator.ValidateCodeGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * author: ZGF
 * 配置codeGenerator
 */

@Configuration
public class CodeGeneratorConfig {

    @Bean
    @ConditionalOnMissingBean(name = "smsCodeGenerator")
    public ValidateCodeGenerator smsCodeGenerator(){
        return new SmsCodeGenerator();
    }

    @Bean
    @ConditionalOnMissingBean(name = "imageCodeGenerator")
    public ValidateCodeGenerator imageCodeGenerator(){
        return new ImageCodeGenerator();
    }
}
