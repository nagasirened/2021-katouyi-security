package com.katouyi.securitytest.test;

import com.katouyi.securitytest.config.valid.processor.AbstractValidateCodeProcessor;
import com.katouyi.securitytest.config.valid.processor.ImageCodeProcessor;
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
 * 01-2021/1/8 : 16:07
 * context :
 */
@Controller
public class TestController {


    @Autowired
    private Map<String, AbstractValidateCodeProcessor> processorMap;

    @GetMapping("/validate/{type}")
    public void getImageCode(HttpServletRequest request, HttpServletResponse response, @PathVariable String type) throws Exception {
        processorMap.get(type + "CodeProcessor").createValidateCode(new ServletWebRequest(request, response));
    }

}
