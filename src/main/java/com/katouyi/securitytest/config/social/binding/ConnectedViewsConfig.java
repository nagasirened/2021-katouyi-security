package com.katouyi.securitytest.config.social.binding;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.View;

/**
 * author: ZGF
 * context : 配置ConnectedViews
 */
@Configuration
public class ConnectedViewsConfig {

    @Bean("/connect/wechatConnected")
    @ConditionalOnMissingBean(name = "wechatConnectedView")
    public View WechatConnectedView() {
        return new GlobalConnectionView();
    }

    @Bean("/connect/qqConnected")
    @ConditionalOnMissingBean(name = "qqConnectedView")
    public View qqConnectedView() {
        return new GlobalConnectionView();
    }

    @Bean("/connect/weiboConnected")
    @ConditionalOnMissingBean(name = "weiboConnectedView")
    public View WeiboConnectedView() {
        return new GlobalConnectionView();
    }

}
