package com.taotao.sso.service.Impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.sun.glass.ui.Size;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.service.UserService;

import redis.clients.jedis.JedisCluster;

/**
 * 
 * @ClassName UserServiceImpl
 * @Description 用户管理service
 * @Author xll
 * @Date 2019年1月7日 下午3:42:28
 * @Version 1.0
 *
 */
@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private TbUserMapper tbUserMapper;
	
	@Autowired
	private JedisCluster jedisCluster;
	
	@Value("${REDIS_USER_SESSION_KEY}")
	private String REDIS_USER_SESSION_KEY;
	
	@Value("${SSO_SESSION_EXPIRE}")
	private Integer SSO_SESSION_EXPIRE;
	
	@Override
	public TaotaoResult checkData(String content, Integer type) {
		//创建查询条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		//对数据进行校验:1、2、3分别代表username、phone、email
		//用户名校验
		if(type == 1) {
			criteria.andUsernameEqualTo(content);
		}else if(type == 2) {
			criteria.andPhoneEqualTo(content);
		}else {
			criteria.andEmailEqualTo(content);
		}
		//执行查询
		List<TbUser> list = tbUserMapper.selectByExample(example);
		if(list == null || list.size() == 0 ) {
			return TaotaoResult.ok(true);
		}
		return TaotaoResult.ok(false);
	}

	@Override
	public TaotaoResult createUser(TbUser tbUser) {
		tbUser.setCreated(new Date());
		tbUser.setUpdated(new Date());
		//md5加密
		tbUser.setPassword(DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes()));
		tbUserMapper.insert(tbUser);
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult userLogin(String username, String password, HttpServletRequest request,HttpServletResponse response) {
		
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = tbUserMapper.selectByExample(example);
		if(list==null || list.size()==0) {
			TaotaoResult.build(400, "用户名或密码错误");
		}
		TbUser user = list.get(0);
		//比对密码
		if(!(DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword()))) {
			return TaotaoResult.build(400, "用户名或密码错误");
		}
		
		//生成令牌token
		String token = UUID.randomUUID().toString();
		//保存用户之前,把用户对象中的密码清空
		user.setPassword(null);
		//把用户信息写入redis
		jedisCluster.set(REDIS_USER_SESSION_KEY + ":" + token,JsonUtils.objectToJson(user));
		//设置session的过期时间
		jedisCluster.expire(REDIS_USER_SESSION_KEY + ":" + token, SSO_SESSION_EXPIRE);
		
		//添加写cookie的逻辑，cookie的有效期是关闭浏览器就失效
		CookieUtils.setCookie(request, response, "TT_TOKEN", token);
		
		//返回token
		return TaotaoResult.ok(token);
	
	}

	/**
	 * 根据token获得用户信息
	 */
	@Override
	public TaotaoResult getUserByToken(String token) {
		//根据token从redis中查询用户信息
		String userData = jedisCluster.get(REDIS_USER_SESSION_KEY + ":" + token);
		//判断是否为空
		if(StringUtils.isBlank(userData)) {
			return TaotaoResult.build(400, "此身份已经过期,请重新登录");
		}
		//如果不为空 更新过期时间
		jedisCluster.expire(REDIS_USER_SESSION_KEY + ":" + token, SSO_SESSION_EXPIRE);
		//返回用户信息
		return TaotaoResult.ok(JsonUtils.jsonToPojo(userData, TbUser.class));
	}
}
