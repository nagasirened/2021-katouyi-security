package com.katouyi.securitytest.config.valid.processor;

import com.katouyi.securitytest.config.valid.code.po.ValidateCode;
import com.katouyi.securitytest.config.valid.sender.SmsCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * author: ZGF
 * context : 短信发送器
 */
@Component
public class SmsCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {

    @Autowired
    private SmsCodeSender smsCodeSender;

    /**
     * 实际，发送短信的方法
     * @param webRequest
     * @param validateCode
     */
    @Override
    protected void send(ServletWebRequest webRequest, ValidateCode validateCode) {
        smsCodeSender.sendSms(webRequest.getParameter("mobile"), validateCode.getCode());
    }
}
