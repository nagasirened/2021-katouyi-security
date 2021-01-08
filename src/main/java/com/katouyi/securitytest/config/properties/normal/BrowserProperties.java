package com.katouyi.securitytest.config.properties.normal;

import com.katouyi.securitytest.citing.LoginType;
import lombok.Data;

/**
 * author: ZGF
 * context : PC端核心配置
 */

@Data
public class BrowserProperties {

    /**
     * 登录成功后返回类型，默认是JSON
     */
    private LoginType loginType = LoginType.JSON;

    /**
     * 默认开启rememberMe功能
     */
    private Boolean allwaysRememberMe = true;

    /**
     * rememberMe 超时时间，暂设置为3天
     */
    private Integer rememberMeTimelong = 3600 * 24 * 3;
}
