package com.katouyi.securitytest.config;

import com.katouyi.securitytest.citing.SecurityConstants;
import com.katouyi.securitytest.config.handler.KatouyiUserDetailService;
import com.katouyi.securitytest.config.properties.SecurityProperties;
import com.katouyi.securitytest.config.valid.ValidateCodeFilterConfig;
import com.katouyi.securitytest.config.remember.RememberSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;


@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessHandler katouyiLoginSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler katouyiLoginFailureHandler;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private PersistentTokenRepository tokenRepository;

    @Bean
    @ConditionalOnMissingBean(name = "userDetailsService")
    public UserDetailsService userDetailsService(){
        return new KatouyiUserDetailService();
    }

    /**
     * 验证码相关配置
     */
    @Autowired
    private ValidateCodeFilterConfig validateCodeFilterConfig;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
            http.formLogin()
                    .loginProcessingUrl(SecurityConstants.DEFAULT_AUTHENTICATION_FORM)
                    .loginPage(SecurityConstants.DEFAULT_AUTHENTICATION_REQUIRE)
                    .successHandler(katouyiLoginSuccessHandler)
                    .failureHandler(katouyiLoginFailureHandler)
                .and()
                    .rememberMe()
                    .alwaysRemember(securityProperties.getBrowser().getAllwaysRememberMe())
                    .tokenRepository(tokenRepository)
                    .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeTimelong())
                    .userDetailsService(userDetailsService())
                .and()
                    .authorizeRequests()
                    .antMatchers(SecurityConstants.DEFAULT_AUTHENTICATION_REQUIRE,
                             SecurityConstants.DEFAULT_AUTHENTICATION_FORM,
                            /** 这个参数前缀需要和 AbstractValidateCodeProcessor 中的一样，且定义了接口名称 */
                            "/validate/*"
                    ).permitAll()
                    .anyRequest()
                    .authenticated()
                .and()
                    .apply(validateCodeFilterConfig)
                .and()
                    .csrf().disable();

    }
}
