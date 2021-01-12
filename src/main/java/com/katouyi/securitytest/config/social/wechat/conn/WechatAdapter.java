package com.katouyi.securitytest.config.social.wechat.conn;

import com.katouyi.securitytest.config.social.wechat.api.Wechat;
import com.katouyi.securitytest.config.social.wechat.base.WechatUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * 微信 api适配器，将微信 api的数据模型转为spring social的标准模型。
 */
public class WechatAdapter implements ApiAdapter<Wechat> {

    private String openId;
	
	public WechatAdapter() {}
	
	public WechatAdapter(String openId){
		this.openId = openId;
	}

	/**
	 * @param api
	 * @return
	 */
	@Override
	public boolean test(Wechat api) {
		return true;
	}

	/**
	 * @param api
	 * @param values
	 */
	@Override
	public void setConnectionValues(Wechat api, ConnectionValues values) {
		WechatUserInfo profile = api.getUserInfo(openId);
		values.setProviderUserId(profile.getOpenid());
		values.setDisplayName(profile.getNickname());
		values.setImageUrl(profile.getHeadimgurl());
	}

	/**
	 * @param api
	 * @return
	 */
	@Override
	public UserProfile fetchUserProfile(Wechat api) {
		return null;
	}

	/**
	 * @param api
	 * @param message
	 */
	@Override
	public void updateStatus(Wechat api, String message) {
		//do nothing
	}

}