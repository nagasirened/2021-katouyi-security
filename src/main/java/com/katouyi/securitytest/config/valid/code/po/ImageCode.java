package com.katouyi.securitytest.config.valid.code.po;

import lombok.Data;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * author: ZGF
 * context : 图片验证码
 */

@Data
public class ImageCode extends ValidateCode{

    private BufferedImage image;

    public ImageCode(BufferedImage image, String code, Integer expireTimeLong) {
        super(code, expireTimeLong);
        this.image = image;
    }

    ImageCode() {}

    public ImageCode(BufferedImage image, String code, LocalDateTime expire) {
        super(code, expire);
        this.image = image;
    }
}
