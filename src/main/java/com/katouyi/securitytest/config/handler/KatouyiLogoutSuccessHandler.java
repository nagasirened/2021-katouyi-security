package com.katouyi.securitytest.config.handler;

import com.alibaba.fastjson.JSONObject;
import com.katouyi.securitytest.citing.SimpleResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * author: ZGF
 * context : 登出管理控制器
 */
@Slf4j
public class KatouyiLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {

        /**
         * 配置类处理，如果存在登出成功的页面，则跳转到成功页面上
         */
        // String signOutUrl = "signOutSuccess/html";
        String signOutUrl = "";
        log.info("退出成功");
        if (StringUtils.isEmpty(signOutUrl)) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSONObject.toJSONString(new SimpleResponse(200, "退出成功", null)));
        } else {
            response.sendRedirect(signOutUrl);
        }

    }
}
