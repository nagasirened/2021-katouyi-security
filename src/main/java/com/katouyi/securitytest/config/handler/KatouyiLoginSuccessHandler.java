package com.katouyi.securitytest.config.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.katouyi.securitytest.citing.LoginType;
import com.katouyi.securitytest.config.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * author: ZGF
 * context : 登录成功处理器
 */

@Slf4j
@Component
public class KatouyiLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ObjectMapper objectMapper;
    /**
     * 登录成功处理器
     * @param request            request
     * @param response           response
     * @param authentication     认证令牌
     * @throws ServletException
     * @throws IOException
     *
     * 如果是JSON返回，则返回JSON信息即可
     * 如果是返回页面，调用父类直接转发到对应的即可
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        log.info("login success, author is {}", authentication.getPrincipal());
        if (LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(authentication));
        } else if (LoginType.REDIRECT.equals(securityProperties.getBrowser().getLoginType())) {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
