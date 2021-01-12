package com.katouyi.securitytest.config.session;

import com.alibaba.fastjson.JSONObject;
import com.katouyi.securitytest.citing.SimpleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * author: ZGF
 * context : 后来用户踢出前者，session踢出后的策略，可以记录踢者的部分信息
 */
@Slf4j
public class KatouyiExpiredSessionStrategy implements SessionInformationExpiredStrategy{

    /**
     * @param sessionInformationExpiredEvent  封装了后来踢人者的请求和响应信息
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException, ServletException {
        HttpServletResponse response = sessionInformationExpiredEvent.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSONObject.toJSONString(new SimpleResponse(401, "并发登录！", null)));
    }
}
