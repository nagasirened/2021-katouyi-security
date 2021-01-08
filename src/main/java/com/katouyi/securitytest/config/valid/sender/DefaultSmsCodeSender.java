package com.katouyi.securitytest.config.valid.sender;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * author: ZGF
 * context : 默认的短信发送器
 */

@Component("smsCodeSender")
public class DefaultSmsCodeSender implements SmsCodeSender {

    @Override
    public void sendSms(String mobile, String validateCode) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "<accessId>", "<accessKey>");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        // 自定义参数（手机号，验证码，签名，模板）
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", "ABC商城");
        request.putQueryParameter("TemplateCode", "SMS_205125657");

        // 构建一个短信验证码
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", validateCode);
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(map));
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DefaultSmsCodeSender sender = new DefaultSmsCodeSender();
        sender.sendSms("15123210120", "113648");
    }
}
