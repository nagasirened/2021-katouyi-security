package com.katouyi.securitytest.config.valid.filter.sms;

import lombok.Data;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Objects;

/**
 * author: ZGF
 * context : 短信登录逻辑处理器
 */
@Data
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;
    /**
     * 认证逻辑
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsAuthencationToken token = (SmsAuthencationToken)authentication;
        /**
         * 名义根据用户名查询，其实是根据电话号码查询用户
         */
        UserDetails user = userDetailsService.loadUserByUsername("mobile-" + String.valueOf(authentication.getPrincipal()));
        if (Objects.isNull(user)) {
            throw new InternalAuthenticationServiceException("无法读取用户信息");
        }
        SmsAuthencationToken authedToken = new SmsAuthencationToken(user.getUsername(), user.getAuthorities());
        /** 将未认证的部分信息塞入authedToken 重要！！！ */
        authedToken.setDetails(token.getDetails());
        return authedToken;
    }

    /**
     * 拦截到自定义的token
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return SmsAuthencationToken.class.isAssignableFrom(aClass);
    }
}
