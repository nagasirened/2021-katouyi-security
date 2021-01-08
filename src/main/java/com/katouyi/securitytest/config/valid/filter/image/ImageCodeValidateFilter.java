package com.katouyi.securitytest.config.valid.filter.image;

import com.katouyi.securitytest.citing.SecurityConstants;
import com.katouyi.securitytest.config.valid.code.po.ValidateCode;
import com.katouyi.securitytest.config.valid.filter.AbstractValidateFilter;
import com.katouyi.securitytest.config.valid.processor.AbstractValidateCodeProcessor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

/**
 * author: ZGF
 * context : 图片验证码过滤器
 */

public class ImageCodeValidateFilter extends AbstractValidateFilter {

    private static final String VALID_TYPE = "image";

    public void init() {
        String imageInterceptorUrls = securityProperties.getValid().getImageInterceptorUrls();
        if (!StringUtils.isEmpty(imageInterceptorUrls)) {
            String[] splitStrings = imageInterceptorUrls.split(",");
            urls.addAll(Arrays.asList(splitStrings));
        }
        /** 须加入表单登录接口 */
        urls.add(SecurityConstants.DEFAULT_AUTHENTICATION_FORM);
    }

    public void validateCode(HttpServletRequest request) throws ServletRequestBindingException, AuthenticationException {
        if (!StringUtils.equalsIgnoreCase(request.getMethod(), "POST")) {
            throw new AuthenticationCredentialsNotFoundException("需要POST请求验证");
        }
        HttpSession session = request.getSession(false);
        ValidateCode sessionValidateCode = (ValidateCode) session.getAttribute(AbstractValidateCodeProcessor.SESSION_AUTHTICATION_PREFIX + "image");
        String paramImageCode = ServletRequestUtils.getStringParameter(request, "imageCode");
        if (StringUtils.isEmpty(paramImageCode)){
            throw new AuthenticationCredentialsNotFoundException("验证码不能为空");
        }
        if(sessionValidateCode == null ){
            throw new AuthenticationCredentialsNotFoundException("验证码不存在");
        }
        if(sessionValidateCode.isExpire()){
            session.removeAttribute(AbstractValidateCodeProcessor.SESSION_AUTHTICATION_PREFIX + "image");
            throw new AuthenticationCredentialsNotFoundException("验证码已过期");
        }
        if(!StringUtils.equals(sessionValidateCode.getCode(), paramImageCode)){
            throw new AuthenticationCredentialsNotFoundException("验证码不匹配");
        }
        session.removeAttribute(AbstractValidateCodeProcessor.SESSION_AUTHTICATION_PREFIX + "image");
    }
}
