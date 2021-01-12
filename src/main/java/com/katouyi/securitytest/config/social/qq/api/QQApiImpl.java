package com.katouyi.securitytest.config.social.qq.api;

import com.alibaba.fastjson.JSONObject;
import com.katouyi.securitytest.config.social.qq.base.QQUserInfo;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

/**
 * author: ZGF
 * context : API 拓展  ☆☆☆ 需要包含获取用户信息的接口 ☆☆☆
 *
 * AbstractOAuth2ApiBinding 中已经实际包含了 accessToken
 */
@Data
public class QQApiImpl extends AbstractOAuth2ApiBinding implements QQ {

    private String openId;

    private String appId;

    /**
     * openId是可以自己获取的，但是appId需要传入
     */
    public static final String URL_OPEN_ID = "https://graph.qq.com/oauth2.0/me?access_token=%s";

    /**
     * 获取用户信息的链接
     * @param appId         oauth_consumer_key及时appId
     * @param accessToken
     */
    public static final String URL_USER_INFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";

    public QQApiImpl(String appId, String accessToken) {
        /**
         * super(accessToken) 默认单参数构造器是将accessToken放在Header中
         * 数据要求默认放在param参数中，所以使用TokenStrategy.ACCESS_TOKEN_PARAMETER
         */
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
        this.appId = appId;

        String getOpenId = String.format(URL_OPEN_ID, accessToken);
        String openIdResult = getRestTemplate().getForObject(getOpenId, String.class);
        this.openId = StringUtils.substringBetween(openIdResult, "\"openId\":\"", "\"}");
    }

    public QQUserInfo getUserInfo(){
        String getUserInfoUrl = String.format(URL_USER_INFO, appId, openId);
        String userInfoString = getRestTemplate().getForObject(getUserInfoUrl, String.class);
        QQUserInfo userInfo;
        try {
            userInfo = JSONObject.parseObject(userInfoString, QQUserInfo.class);
            userInfo.setOpenId(openId);
        } catch (Exception e) {
            throw new RuntimeException("获取用户信息失败"+e.getMessage());
        }
        return userInfo;
    }
}
