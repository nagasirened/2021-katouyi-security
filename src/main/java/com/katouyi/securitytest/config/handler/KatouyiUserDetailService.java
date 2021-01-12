package com.katouyi.securitytest.config.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

/**
 * author: ZGF
 * context : 用户获取
 */
@Slf4j
public class KatouyiUserDetailService implements UserDetailsService, SocialUserDetailsService {

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
        log.info("用户名密码登录");
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

    /**
     * 社交三方登录使用的
     * @param userId    并非一定就是主键，根据规则可以自由设定
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        log.info("社交登录");
        return new SocialUser("user", passwordEncoder.encode("123456"),
                true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }
}
