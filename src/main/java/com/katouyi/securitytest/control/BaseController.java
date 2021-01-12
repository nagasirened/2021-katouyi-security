package com.katouyi.securitytest.control;

import com.katouyi.securitytest.citing.SecurityConstants;
import com.katouyi.securitytest.citing.SimpleResponse;
import com.katouyi.securitytest.citing.SocialUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * author: ZGF
 */
@RestController
@Slf4j
public class BaseController {

    private RequestCache requestCache = new HttpSessionRequestCache();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    @GetMapping("/test")
    public String test(){
        return "test string";
    }


    /**
     * 没有登录的话，就返回401，前端处理要求登录就行
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @GetMapping("/authentication/require")
    public SimpleResponse requireAuthencation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            String redirectUrl = savedRequest.getRedirectUrl();
            log.info("引发跳转的请求是：" + redirectUrl);
            if (StringUtils.endsWithIgnoreCase(redirectUrl, ".html"))
                redirectStrategy.sendRedirect(request, response, redirectUrl);
        }
        return new SimpleResponse(401, "请登录", null);
    }


    /**
     * 在注册界面调用，提示用户 【您将要与微信的某某账号绑定，账号昵称，头像等展示出来】
     * @return
     */
    @GetMapping(SecurityConstants.SOCIAL_USER_INFO)
    public SocialUserInfo getSocialInfo(HttpServletRequest request){
        ServletWebRequest servletWebRequest = new ServletWebRequest(request);
        Connection<?> connection = providerSignInUtils.getConnectionFromSession(servletWebRequest);
        return SocialUserInfo.builder().nickName(connection.getDisplayName())
                                    .headImg(connection.getImageUrl())
                                    .providerId(connection.getKey().getProviderId())
                                    .providerUserId(connection.getKey().getProviderUserId())
                                    .build();
    }

    /**
     * 用户注册，无论是注册还是绑定用户，都会拿到用户的唯一标识，拿到唯一标识后，处理插入到对应数据库的操作。
     * 这样的话，后续再次进行调用social登录时，Social的Provider就可以查询到数据库中的对应信息了，这样就不会抛出并跳出到注册页面了
     */
    @PostMapping(SecurityConstants.DEFAULT_USER_REGIST)
    public void register (User user, HttpServletRequest request, HttpServletResponse response){

        // TODO 用户注册逻辑
        // userService.register(user);


        /** 这里我把username当做唯一标识 */
        String userId = user.getUsername();
        providerSignInUtils.doPostSignUp(userId, new ServletWebRequest(request));

        // TODO 注册成功后，可以自动登录即可
        // FIXME 用户下一次用QQ登录，就可以因为数据库中有关联的用户而直接登录进去
    }

    /**
     * 默认的Session最小时长为一分钟
     * Session失效的跳转位置
     */
    @GetMapping(SecurityConstants.DEFAULT_SESSION_INVALID)
    public SimpleResponse sessionInvalid(){
        return new SimpleResponse(HttpStatus.SC_UNAUTHORIZED, "Session失效", null);
    }

    /**
     * 退出登录: 1、当前 session 失效
     *          2、清除相关的 RememberMe 信息
     *          3、清空当前 SecurityContext 信息
     */
    @GetMapping("/logout/success")
    public SimpleResponse logout(){
        return new SimpleResponse(HttpStatus.SC_OK, "Session失效", null);
    }
}
