package com.katouyi.securitytest.config.valid.filter.sms;

import com.katouyi.securitytest.citing.SecurityConstants;
import com.katouyi.securitytest.config.valid.code.po.ValidateCode;
import com.katouyi.securitytest.config.valid.filter.AbstractValidateFilter;
import com.katouyi.securitytest.config.valid.processor.AbstractValidateCodeProcessor;
import lombok.Setter;
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
 * context : 短信方式登录过滤器，仅仅是验证短信码对不对，不对就不谈了，类似于图片验证码，先验证图片
 */

@Setter
public class SmsValidateFilter extends AbstractValidateFilter {

    private static final String VALID_TYPE = "sms";
    
    public void init() {
        String smsInterceptorUrls = super.securityProperties.getValid().getSmsInterceptorUrls();
        if (!org.apache.commons.lang3.StringUtils.isEmpty(smsInterceptorUrls)) {
            String[] splitStrings = smsInterceptorUrls.split(",");
            urls.addAll(Arrays.asList(splitStrings));
        }
        /** 须加入短信登录接口 */
        urls.add(SecurityConstants.SMS_LOGIN_URI);
    }

    public void validateCode(HttpServletRequest request) throws ServletRequestBindingException, AuthenticationException {
        if (!StringUtils.equalsIgnoreCase(request.getMethod(), "POST")) {
            throw new AuthenticationCredentialsNotFoundException("需要POST请求验证");
        }
        HttpSession session = request.getSession(false);
        ValidateCode sessionValidateCode = (ValidateCode) session.getAttribute(AbstractValidateCodeProcessor.SESSION_AUTHTICATION_PREFIX + "image");
        String paramSmsCode = ServletRequestUtils.getStringParameter(request, "smsCode");
        if (org.apache.commons.lang3.StringUtils.isEmpty(paramSmsCode)){
            throw new AuthenticationCredentialsNotFoundException("验证码不能为空");
        }
        if(sessionValidateCode == null ){
            throw new AuthenticationCredentialsNotFoundException("验证码不存在");
        }
        if(sessionValidateCode.isExpire()){
            session.removeAttribute(AbstractValidateCodeProcessor.SESSION_AUTHTICATION_PREFIX + VALID_TYPE);
            throw new AuthenticationCredentialsNotFoundException("验证码已过期");
        }
        if(!org.apache.commons.lang3.StringUtils.equals(sessionValidateCode.getCode(), paramSmsCode)){
            throw new AuthenticationCredentialsNotFoundException("验证码不匹配");
        }
        session.removeAttribute(AbstractValidateCodeProcessor.SESSION_AUTHTICATION_PREFIX + VALID_TYPE);
    }
}
