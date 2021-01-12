package com.katouyi.securitytest.config.social.qq;

import com.katouyi.securitytest.config.properties.SecurityProperties;
import com.katouyi.securitytest.config.social.qq.conn.QQConnectionFactory;
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
 * author: ZGF
 * context : 如果没有配置appId和appSecret，该配置不生效
 */
@Configuration
@ConditionalOnProperty(prefix = "katouyi.security.social.qq", name = "app-id")
public class QQAutoConfig extends SocialConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer configurer,
                                       Environment environment) {
        configurer.addConnectionFactory(createConnectionFactory());
    }

    private ConnectionFactory createConnectionFactory() {
        return new QQConnectionFactory(securityProperties.getSocial().getQq().getProviderId(),
                securityProperties.getSocial().getQq().getAppId(),
                securityProperties.getSocial().getQq().getAppSecret());
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new UserIdSource() {
            @Override
            public String getUserId() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication.getPrincipal() instanceof UserDetails)
                    return ((UserDetails) authentication.getPrincipal()).getUsername();
                return authentication.getName();
            }
        };
    }
}
