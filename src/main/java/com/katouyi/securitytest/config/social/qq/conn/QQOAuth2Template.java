package com.katouyi.securitytest.config.social.qq.conn;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * author: ZGF
 * context : 默认的 OAuth2Template 在根据授权码回调时，不能处理ContentType为 “text/html” 的请求
 */
@Slf4j
public class QQOAuth2Template extends OAuth2Template {

    /**
     * useParametersForClientAuthentication 没有默认值，为true的话才会调用到clientId和clientSecret
     * 见 super.exchangeForAccess() 方法
     */
    public QQOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
        setUseParametersForClientAuthentication(true);
    }

    /**
     * 覆盖方法
     * 在原有的 super.createRestTemplate(); 上加一个ContentType  (text/html)
     *
     * 如果没有加这个，会默认跳转到 signin 路径中
     */
    @Override
    protected RestTemplate createRestTemplate() {
        RestTemplate restTemplate = super.createRestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;
    }

    /**
     * 覆盖方法，本意是返回JSON之后，处理取出数据，但是腾讯QQ返回的并不是json字符串，而是一个由 & 拼接的结果
     * @param accessTokenUrl
     * @param parameters
     * @return
     */
    @Override
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
        /** Map.class  改成  String.class */
        String accessTokenVOString = getRestTemplate().postForObject(accessTokenUrl, parameters, String.class, new Object[0]);
        QQOAuth2Template.log.info("获取accessToken的响应" + accessTokenVOString);
        String[] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(accessTokenVOString, "&");

        /**
         * 官网结果示例  access_token=FE04************************CCE2&expires_in=7776000&refresh_token=88E4************************BE14
         */
        String accessToken  = StringUtils.substringAfterLast(items[0], "=");
        Long   expiresIn    = new Long(StringUtils.substringAfterLast(items[1], "="));
        String refreshToken = StringUtils.substringAfterLast(items[2], "=");

        return new AccessGrant(accessToken, null, refreshToken, expiresIn);
    }
}