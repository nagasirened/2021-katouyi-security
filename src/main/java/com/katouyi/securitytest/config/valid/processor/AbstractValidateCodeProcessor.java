package com.katouyi.securitytest.config.valid.processor;

import com.katouyi.securitytest.config.valid.code.generator.ValidateCodeGenerator;
import com.katouyi.securitytest.config.valid.code.po.ImageCode;
import com.katouyi.securitytest.config.valid.code.po.ValidateCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;
import java.util.Map;

/**
 * author: ZGF
 * context : 验证码发送器，封装了验证码生成器与发送逻辑
 *
 * <C> C 代表了验证码的类型
 */

public abstract class AbstractValidateCodeProcessor<C> implements CodeProcessor {

    SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
    /**
     * 通过接口，知道是处理sms还是image验证码请求：比如说/validate/sms，截取前缀后是sms,则是处理短信
     */
    public static final String API_PREFIX = "/validate/";

    public static final String SESSION_AUTHTICATION_PREFIX = "session_valid_code";

    @Autowired
    private Map<String, ValidateCodeGenerator> validateCodeGeneratorMap;

    /**
     * 获取验证类型
     * @param webRequest
     * @return
     */
    private String getType(ServletWebRequest webRequest) {
        return StringUtils.substringAfter(webRequest.getRequest().getRequestURI(), API_PREFIX);
    }

    /**
     * 生成code共有三个步骤，分别是1.生成code  2.存储session或者缓存中  3.发送code出去
     * 其中第二步都是一样且必须的
     * 第一步的生成code，根据不同的验证类型，可以一个接口两个实现
     * 第三步需要自定义实现
     */
    @Override
    public void createValidateCode(ServletWebRequest webRequest) throws Exception {
        C validateCode = generate(webRequest);
        save(webRequest, validateCode);
        send(webRequest, validateCode);
    }

    /**
     * 判断使用哪一种生成器
     * @param webRequest
     * @return
     */
    protected C generate(ServletWebRequest webRequest) {
        String type = getType(webRequest);
        ValidateCodeGenerator codeGenerator = validateCodeGeneratorMap.get(type + "CodeGenerator");
        ValidateCode validateCode = codeGenerator.generateCode(webRequest);
        return (C)validateCode;
    }

    /**
     * 将code保存在Session中
     * @param webRequest
     * @param valid
     */
    protected void save(ServletWebRequest webRequest, C valid) {
        ImageCode imageCode = (ImageCode) valid;
        ValidateCode code = new ValidateCode(imageCode.getCode(), imageCode.getExpire());
        sessionStrategy.setAttribute(webRequest, SESSION_AUTHTICATION_PREFIX + getType(webRequest), code);
    }

    /**
     * 抽象方法待实现
     */
    protected abstract void send(ServletWebRequest webRequest, C validateCode) throws IOException;

}
