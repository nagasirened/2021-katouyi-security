package com.katouyi.securitytest.config.social.qq.api;

import com.katouyi.securitytest.config.social.qq.base.QQUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * author: ZGF
 * context : 简历另外一个获取Adapter的参数
 */

public class QQApiAdapter implements ApiAdapter<QQ> {

    /**
     * 认为API是否还可以被调用，此处默认为true
     * @param qqApi
     * @return
     */
    @Override
    public boolean test(QQ qqApi) {
        return true;
    }

    @Override
    public void setConnectionValues(QQ qqApi, ConnectionValues connectionValues) {
        QQUserInfo userInfo = qqApi.getUserInfo();

        connectionValues.setDisplayName(userInfo.getNickname());
        connectionValues.setImageUrl(userInfo.getFigureurl_qq_1());
        connectionValues.setProfileUrl(null);                       //个人主页链接，微博有，QQ没有
        connectionValues.setProviderUserId(userInfo.getOpenId());   //服务商的用户ID
    }

    /**
     * 通过api拿到用户的标准信息，暂未实现
     * @param api
     * @return
     */
    @Override
    public UserProfile fetchUserProfile(QQ api) {
        return null;
    }

    /**
     * 一般用于更新Profile，暂未实现
     * @param qqApi
     * @param s
     */
    @Override
    public void updateStatus(QQ qqApi, String s) {
        return;
    }
}
