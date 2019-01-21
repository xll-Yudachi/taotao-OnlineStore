package com.taotao.portal.service.Impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.pojo.TbUser;
import com.taotao.portal.service.UserService;

/**
 * 
 * @ClassName UserServiceImpl
 * @Description 用户管理service
 * @Author xll
 * @Date 2019年1月8日 下午3:15:14
 * @Version 1.0
 *
 */

@Service
public class UserServiceImpl implements UserService{

	@Value("${SSO_BASE_URL}")
	public String SSO_BASE_URL;
	
	@Value("${SSO_USER_TOKEN}")
	private String SSO_USER_TOKEN;
	
	@Value("${SSO_PAGE_LOGIN}")
	public String SSO_PAGE_LOGIN;
	
	
	@Override
	public TbUser getUserByToken(String token) {
		try {
			//调用SSO系统的服务,根据token取用户信息
			String userData = HttpClientUtil.doGet(SSO_BASE_URL + SSO_USER_TOKEN + token);
			//把json转换成taotaoresult
			TaotaoResult result = TaotaoResult.formatToPojo(userData, TbUser.class);
			if(result.getStatus() == 200) {
				TbUser user = (TbUser)result.getData();
				return user;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
