package com.katouyi.securitytest.config.social.wechat.api;

import com.alibaba.fastjson.JSONObject;
import com.katouyi.securitytest.config.social.wechat.base.WechatUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.nio.charset.Charset;
import java.util.List;

/**
 * author: ZGF
 * context : 获取信息信息
 *
 * AbstractOAuth2ApiBinding 的目的是要获取accessToken 看它需要什么参数
 */

public class WechatImpl extends AbstractOAuth2ApiBinding implements Wechat {

    /**
     * 获取用户信息的url，不需要使用appId也可以
     */
    private static final String URL_GET_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?openid=";


    public WechatImpl (String accessToken) {
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
    }

    @Override
    public WechatUserInfo getUserInfo(String openId) {
        String userInfoUrl = URL_GET_USER_INFO + openId;
        String userInfoString = getRestTemplate().getForObject(userInfoUrl, String.class);
        if (StringUtils.isNotEmpty(userInfoString)) {
            return JSONObject.parseObject(userInfoString, WechatUserInfo.class);
        }
        return null;
    }

    /**
     * 默认注册的StringHttpMessageConverter字符集为ISO-8859-1，而微信返回的是UTF-8的，所以覆盖了原来的方法。
     */
    protected List<HttpMessageConverter<?>> getMessageConverters() {
        List<HttpMessageConverter<?>> messageConverters = super.getMessageConverters();
        messageConverters.remove(0);
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return messageConverters;
    }
}
