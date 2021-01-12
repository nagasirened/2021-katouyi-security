package com.katouyi.securitytest.config.social.qq.conn;

import com.katouyi.securitytest.config.social.qq.api.QQ;
import com.katouyi.securitytest.config.social.qq.api.QQApiImpl;
import lombok.Getter;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

/**
 * author: ZGF
 * context : ServiceProvider
 */

public class KatouyiServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {

    @Getter
    private String appId;

    public static final String AUTHORIZE_URL = "https://graph.qq.com/oauth2.0/authorize";

    public static final String ACCESSTOKEN_URL = "https://graph.qq.com/oauth2.0/token";
    /**
     * 父类没有午餐构造器，定义一个构造器，返回一个ServiceProvider即可
     */
    public KatouyiServiceProvider(String appId, String appSecret) {
        /**
         * appId , appSecret, 获取授权码的链接， 获取accessToken的链接
         * OAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl)
         */
        super(new QQOAuth2Template(appId, appSecret, AUTHORIZE_URL, ACCESSTOKEN_URL));
        /**
         * appId要传给 getApi() 这个接口，因此保留次参数
         */
        this.appId = appId;
    }

    /**
     * 传入参数为accessToken
     * @param accessToken
     * @return
     */
    @Override
    public QQApiImpl getApi(String accessToken) {
        return new QQApiImpl(appId, accessToken);
    }
}