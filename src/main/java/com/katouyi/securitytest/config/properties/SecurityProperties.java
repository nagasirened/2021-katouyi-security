package com.katouyi.securitytest.config.properties;

import com.katouyi.securitytest.config.properties.normal.BrowserProperties;
import com.katouyi.securitytest.config.properties.normal.ValidateCodeProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * author: ZGF
 * context : 注册Bean
 */

@Data
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
@ConfigurationProperties(prefix = "katouyi.security")
public class SecurityProperties {

    private BrowserProperties browser = new BrowserProperties();

    private ValidateCodeProperties valid = new ValidateCodeProperties();
}
