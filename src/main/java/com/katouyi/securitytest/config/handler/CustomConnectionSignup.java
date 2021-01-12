package com.katouyi.securitytest.config.handler;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;

/**
 * author: ZGF
 * SpringSocial中，用户用QQ登录等，需要绑定或者注册一个账号并关联进本地数据库
 *
 * SocialAuthenticationProvider 中的  toUserId()  方法下可知，
 * 如果自定义实现一个 connectionSignUp 对象，重写一个 execute 方法，
 * 就相当于我们将此QQ或者微信账号偷偷注册为本地的一个账号了，不需要用户注册进去了
 */

@Component
public class CustomConnectionSignup implements ConnectionSignUp {

    /**
     * 根据社交用户信息默认返回一个用户信息
     * @param connection
     * @return
     */
    @Override
    public String execute(Connection<?> connection) {

        return connection.getDisplayName();
    }

}
