package com.katouyi.securitytest.config.social.wechat;

import com.katouyi.securitytest.config.properties.SecurityProperties;
import com.katouyi.securitytest.config.properties.normal.WechatProperties;
import com.katouyi.securitytest.config.social.wechat.conn.WechatConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactory;

/**
 * 微信登录配置，将ConnectionFactory 放进策略中
 */
@Configuration
@ConditionalOnProperty(prefix = "katouyi.security.social.wechat",name = "app-id")
public class WechatAutoConfiguration extends SocialConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer configurer,
                                       Environment environment) {
        configurer.addConnectionFactory(createConnectionFactory());
    }

    protected ConnectionFactory<?> createConnectionFactory() {
        WechatProperties wechatConfig = securityProperties.getSocial().getWechat();
        return new WechatConnectionFactory(wechatConfig.getProviderId(), wechatConfig.getAppId(), wechatConfig.getAppSecret());
    }

    @Override
    public UserIdSource getUserIdSource() {
        return () -> {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication.getPrincipal() instanceof UserDetails)
                    return ((UserDetails) authentication.getPrincipal()).getUsername();
                return authentication.getName();
            };
    }
}