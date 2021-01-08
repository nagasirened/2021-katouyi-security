package com.katouyi.securitytest.config.handler;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * author: ZGF
 * context : 用户获取
 */

public class KatouyiUserDetailService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 根据用户名 username 获取用户信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 如果是短信验证码，根据电话号码获取用户信息
        if (StringUtils.startsWith(username, "mobile-")) {
            String mobile = StringUtils.substringAfter(username, "mobile-");
            /**
             * 根据电话号码查询用户先 巴拉巴拉
             */
            return new User(mobile, passwordEncoder.encode("123456"),
                    true, true, true, true,
                    AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        }
        /** 普通的根据名字查询的逻辑 */
        return new User(username, passwordEncoder.encode("123456"),
                true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }

}
