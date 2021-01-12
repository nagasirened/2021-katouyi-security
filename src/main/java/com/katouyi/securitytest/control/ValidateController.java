package com.katouyi.securitytest.control;

import com.katouyi.securitytest.config.valid.processor.AbstractValidateCodeProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * author: ZGF
 * context : 获取验证码
 * 目前可用的type分为 sms短信验证码 和 image图片验证码
 */
@Controller
public class ValidateController {


    @Autowired
    private Map<String, AbstractValidateCodeProcessor> processorMap;

    @GetMapping("/validate/{type}")
    public void getImageCode(HttpServletRequest request, HttpServletResponse response, @PathVariable String type) throws Exception {
        processorMap.get(type + "CodeProcessor").createValidateCode(new ServletWebRequest(request, response));
    }

}
