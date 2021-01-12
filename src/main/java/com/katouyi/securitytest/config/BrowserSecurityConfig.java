package com.katouyi.securitytest.config;

import com.katouyi.securitytest.citing.SecurityConstants;
import com.katouyi.securitytest.config.handler.KatouyiLogoutSuccessHandler;
import com.katouyi.securitytest.config.handler.KatouyiUserDetailService;
import com.katouyi.securitytest.config.properties.SecurityProperties;
import com.katouyi.securitytest.config.session.KatouyiExpiredSessionStrategy;
import com.katouyi.securitytest.config.valid.ValidateCodeFilterConfig;
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
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.social.security.SpringSocialConfigurer;


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

    /**
     * spring-social过滤器信息
     * @return
     */
    @Autowired
    private SpringSocialConfigurer katouyiSpringSocialConfigurer;


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LogoutSuccessHandler katouyiLogoutSuccessHandler(){
        return new KatouyiLogoutSuccessHandler();
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
                    .sessionManagement()                                           // session超时时间最小为1m，设置小于这个也会被改变
                    .invalidSessionUrl(SecurityConstants.DEFAULT_SESSION_INVALID)  // Session失效的跳转地址
                    .maximumSessions(1)
                    .expiredSessionStrategy(new KatouyiExpiredSessionStrategy())  // 踢出上一个人之后的策略，二选一
                    .maxSessionsPreventsLogin(true)                                // 这个是true的话，会阻止后来者的登录行为，与上面的二选一
                .and()
                .and()
                     .logout()                                               // 默认的退出登录地址是 logout
                     .logoutUrl("/signOut")
                     .logoutSuccessUrl("/logout/success")
                     .logoutSuccessHandler(katouyiLogoutSuccessHandler())   // 跟上面的logoutSuccessUrl互斥
                     .deleteCookies("JSESSIONID")
                .and()
                    .authorizeRequests()                                     // validata 代表获取验证码   /oauth代表oauth2实现  "/oauth/authorize"  /oauth/token
                    .antMatchers(SecurityConstants.DEFAULT_AUTHENTICATION_REQUIRE,
                             SecurityConstants.DEFAULT_AUTHENTICATION_FORM,
                            "/validate/**", "/oauth/**",
                            SecurityConstants.SOCIAL_USER_INFO,
                            SecurityConstants.DEFAULT_USER_REGIST
                    ).permitAll()
                    .anyRequest()
                    .authenticated()
                .and()
                    .apply(validateCodeFilterConfig)
                .and()
                    .apply(katouyiSpringSocialConfigurer)
                .and()
                    .csrf().disable();

    }
}
