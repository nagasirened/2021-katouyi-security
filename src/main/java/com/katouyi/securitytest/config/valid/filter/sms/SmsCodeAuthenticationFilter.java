package com.katouyi.securitytest.config.valid.filter.sms;

import com.katouyi.securitytest.citing.SecurityConstants;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: sucurity-demo
 * @description: SmsCodeAuthenticationFilter
 * @author: ZengGuangfu
 * @create 2018-11-30 16:34
 */
public class SmsCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String SPRING_SECURITY_FORM_MOBILE_KEY = "mobile";
    private String mobileParameter = SPRING_SECURITY_FORM_MOBILE_KEY;
    private boolean postOnly = true;   //是不是只处理post请求

    /**
     * 拦截登录请求短信验证码登录请求
     */
    public SmsCodeAuthenticationFilter() {
        super(new AntPathRequestMatcher(SecurityConstants.SMS_LOGIN_URI, "POST"));
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            String mobile = this.obtainParameter(request);
            if (mobile == null) {
                mobile = "";
            }
            mobile = mobile.trim();
            SmsAuthencationToken authRequest = new SmsAuthencationToken(mobile);
            this.setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }


    public String obtainParameter(HttpServletRequest request){
        return request.getParameter("mobile");
    }

    /**
     * @param request
     * @param authRequest
     */
    protected void setDetails(HttpServletRequest request, SmsAuthencationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public void obtainMobileParameter(HttpServletRequest request) throws ServletRequestBindingException {
        ServletRequestUtils.getRequiredStringParameter((ServletRequest)request,"mobile");
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getMobileParameter() {
        return this.mobileParameter;
    }

    public void setMobileParameter(String mobile) {
        Assert.hasText(mobile,"mobile parameter must not be empty or null");
        this.mobileParameter = mobile;
    }

}
