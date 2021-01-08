package com.katouyi.securitytest.config.valid.processor;

import com.katouyi.securitytest.config.valid.code.po.ImageCode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * author: ZGF
 * context : 图片验证码发送
 */

@Component
public class ImageCodeProcessor extends AbstractValidateCodeProcessor<ImageCode> {

    @Override
    protected void send(ServletWebRequest webRequest, ImageCode imageCode) throws IOException {
        ImageIO.write(imageCode.getImage(), "JPEG", webRequest.getResponse().getOutputStream());
    }
}
