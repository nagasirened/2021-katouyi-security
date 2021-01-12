package com.katouyi.securitytest.config.social.binding;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.katouyi.securitytest.citing.SimpleResponse;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.Connection;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 执行绑定流程的请求 Post /connect/{providerId}
 *             返回的视图 /connect/{providerId}Connected
 *
 *     ☆☆☆   解绑 Delete /connect/{providerId}  ☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆
 * 用Bean来代替
 */
public class GlobalConnectionView extends AbstractView {

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 视图渲染方法
     * @param map 即是 model
     */
    @Override
    protected void renderMergedOutputModel(Map<String, Object> map,
                                           HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        SimpleResponse simpleResponse;
        if (map.get("connection") != null) {
            simpleResponse = new SimpleResponse(200, "绑定成功", null);
        } else {
            simpleResponse = new SimpleResponse(200, "解绑成功", null);
        }
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(simpleResponse));
    }
}
