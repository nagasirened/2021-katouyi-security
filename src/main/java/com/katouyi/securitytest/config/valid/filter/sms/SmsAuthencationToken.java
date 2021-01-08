package com.katouyi.securitytest.config.valid.filter.sms;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * author: ZGF
 * 01-2021/1/8 : 14:54
 * context : 短信认证没有密码，因此去掉 dredentials
 */

public class SmsAuthencationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 530L;

    private final Object principal;

    /**
     * 只有一个参数，刚进来时创建的没有通过认证的token，刚进来时存储电话号码
     * @param principal
     */
    public SmsAuthencationToken(Object principal) {
        super((Collection)null);
        this.principal = principal;
        this.setAuthenticated(false);
    }

    /**
     * 认证成功后，创建新的认证成功的
     * @param principal 认证成功后，principal可以用来存储用户名
     * @param authorities
     */
    public SmsAuthencationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }
}
