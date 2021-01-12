package com.katouyi.securitytest.config.properties.normal;

import lombok.Data;

/**
 * author: ZGF
 */

@Data
public class CustomSocialProperties {

    /**
     * 自定义 social 认证的前缀
     */
    private String filterProcessUrl;

    private QQProperties qq = new QQProperties();

    private WechatProperties wechat = new WechatProperties();
}
