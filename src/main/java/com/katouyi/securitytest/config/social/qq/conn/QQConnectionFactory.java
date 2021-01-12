package com.katouyi.securitytest.config.social.qq.conn;

import com.katouyi.securitytest.config.social.qq.api.QQApiImpl;
import com.katouyi.securitytest.config.social.qq.api.QQ;
import com.katouyi.securitytest.config.social.qq.api.QQApiAdapter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * author: ZGF
 * context : QQ连接工厂
 */

public class QQConnectionFactory extends OAuth2ConnectionFactory<QQ>{

    /**
     public QQConnectionFactory(String providerId, OAuth2ServiceProvider<QQApiImpl> serviceProvider, ApiAdapter<QQApiImpl> apiAdapter) {
        super(providerId, serviceProvider, apiAdapter);
     }
    */
    /**
     * 自定义构造器
     * @param providerId 服务商唯一标识
     * @param appId
     * @param appSecret
     */
    public QQConnectionFactory(String providerId, String appId, String appSecret) {
        super(providerId, new KatouyiServiceProvider(appId, appSecret), new QQApiAdapter());
    }

}
