package com.katouyi.securitytest.config.social.qq;

import lombok.Setter;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;
import org.springframework.stereotype.Component;

/**
 * author: ZGF
 * context : 【拓展过滤器】在原基础的过滤器加上一些自定义内容
 */
@Component
public class KatouyiSpringSocialconfigurer extends SpringSocialConfigurer {

    @Setter
    private String filterProcessUrl;

    /**
     * 默认是拦截  /auth 开头的链接
     *                      **** 自定义拦截地址 ****
     * @param object
     * @param <T>
     * @return
     */
    @Override
    protected <T> T postProcess(T object) {
        SocialAuthenticationFilter filter = (SocialAuthenticationFilter)super.postProcess(object);
        filter.setFilterProcessesUrl(filterProcessUrl);
        return (T)filter;
    }
}
