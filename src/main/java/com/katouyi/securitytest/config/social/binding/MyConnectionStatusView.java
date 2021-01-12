package com.katouyi.securitytest.config.social.binding;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
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
 * author: ZGF
 * context : 绑定信息的view
 *
 * 页面调用的路径 /connect
 *
 * SpringSocial 为我们提供了查看各种社交平台的绑定信息的接口，即 ConnectController.connectionStatus()
 * 这个接口为我们返回了绑定的社交平台的信息
 *
 * 但是由于它是返回到视图View的，我们没有这个页面的话，它会抛出405 找不到页面的异常。
 *
 * 我们可以自定义一个View视图器,这个视图拦截了返回的结果，并将数据从视图的model中取出，并返回
 */

/**
 * 查看所有社交平台的登录状态
 *
 * 自定义视图并拦截
 */
@Component("/connect/status")
public class MyConnectionStatusView extends AbstractView {

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 视图渲染方法
     * @param map 即是 model
     */
    @Override
    protected void renderMergedOutputModel(Map<String, Object> map,
                                           HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        Map<String, List<Connection<?>>> connectionMap = (Map<String, List<Connection<?>>>)map.get("connectionMap");
        // 返回有没有绑定社交平台账号即可
        Map<String, Boolean> result = new HashMap<>();
        Set<String> keySet = connectionMap.keySet();
        for (String key : keySet) {
            result.put(key, CollectionUtils.isNotEmpty(connectionMap.get(key)));
        }

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
