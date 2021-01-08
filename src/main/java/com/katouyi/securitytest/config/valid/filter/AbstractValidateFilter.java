package com.katouyi.securitytest.config.valid.filter;

import com.katouyi.securitytest.config.handler.KatouyiLoginFailureHandler;
import com.katouyi.securitytest.config.properties.SecurityProperties;
import lombok.Setter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * author: ZGF
 * context : 自定义父类验证抽象类（专门用于验证）
 */
@Setter
public abstract class AbstractValidateFilter extends OncePerRequestFilter {

    /**
     * 用于路径匹配的关系
     */
    protected AntPathMatcher antPathMatcher;

    protected KatouyiLoginFailureHandler katouyiLoginFailureHandler;

    protected SecurityProperties securityProperties;

    protected Set<String> urls = new HashSet<>();

    /**
     * 初始化，在urls中加入需要拦截的方法
     */
    public abstract void init();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        boolean flag = false;
        String requestURI = request.getRequestURI();
        for (String url : urls) {
            if (antPathMatcher.match(url, requestURI)) {
                flag = true;
            }
        }
        if (flag) {
            try {
                validateCode(request);
            } catch (AuthenticationException e) {
                katouyiLoginFailureHandler.onAuthenticationFailure(request, response, e);
                /** 不加return的话会继续往后执行 */
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    public abstract void validateCode(HttpServletRequest request) throws ServletRequestBindingException, AuthenticationException;
}
