package com.katouyi.securitytest.config.valid;

import com.katouyi.securitytest.config.valid.filter.image.ImageCodeValidateFilter;
import com.katouyi.securitytest.config.valid.filter.sms.SmsCodeAuthenticationFilter;
import com.katouyi.securitytest.config.valid.filter.sms.SmsCodeAuthenticationProvider;
import com.katouyi.securitytest.config.valid.filter.sms.SmsValidateFilter;
import com.katouyi.securitytest.config.handler.KatouyiLoginFailureHandler;
import com.katouyi.securitytest.config.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

/**
 * author: ZGF
 * context : 验证码相关的过滤器，整合配置
 */
@Component
public class ValidateCodeFilterConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private KatouyiLoginFailureHandler katouyiLoginFailureHandler;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationSuccessHandler katouyiLoginSuccessHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        /**
         * 提前校验-图片验证码-Filter
         */
        ImageCodeValidateFilter imageFilter = new ImageCodeValidateFilter();
        imageFilter.setAntPathMatcher(antPathMatcher);
        imageFilter.setKatouyiLoginFailureHandler(katouyiLoginFailureHandler);
        imageFilter.setSecurityProperties(securityProperties);
        imageFilter.init();

        /**
         * 提前校验-短信验证码-Filter
         */
        SmsValidateFilter smsFilter = new SmsValidateFilter();
        smsFilter.setAntPathMatcher(antPathMatcher);
        smsFilter.setKatouyiLoginFailureHandler(katouyiLoginFailureHandler);
        smsFilter.setSecurityProperties(securityProperties);
        smsFilter.init();

        /**
         * 封装短信校验之后，mobile校验的逻辑
         */
        SmsCodeAuthenticationFilter smsCodeAuthenticationFilter = new SmsCodeAuthenticationFilter();
        smsCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        smsCodeAuthenticationFilter.setAuthenticationSuccessHandler(katouyiLoginSuccessHandler);
        smsCodeAuthenticationFilter.setAuthenticationFailureHandler(katouyiLoginFailureHandler);

        SmsCodeAuthenticationProvider smsCodeAuthenticationProvider = new SmsCodeAuthenticationProvider();
        smsCodeAuthenticationProvider.setUserDetailsService(userDetailsService);

        http//.addFilterBefore(imageFilter, UsernamePasswordAuthenticationFilter.class)
            //.addFilterBefore(smsFilter, UsernamePasswordAuthenticationFilter.class)
            .authenticationProvider(smsCodeAuthenticationProvider)
            .addFilterAfter(smsCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
