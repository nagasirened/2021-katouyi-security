package com.katouyi.securitytest.config.social.wechat.api;

import com.katouyi.securitytest.config.social.wechat.base.WechatUserInfo;

/**
 * author: ZGF
 * 微信信息接口
 */

public interface Wechat {

    /**
     * 比QQ多传一个参数
     *
     * QQ的openId是用一个接口获取，微信授权码不仅可以拿到accessToken，还可以拿到openId，简化了一点步骤
     */
    WechatUserInfo getUserInfo(String openId);
}
