package com.katouyi.securitytest.citing;

import lombok.Builder;
import lombok.Data;

/**
 * author: ZGF
 */

@Data
@Builder
public class SocialUserInfo {

    private String providerId;

    private String providerUserId;

    private String nickName;

    private String headImg;
}
