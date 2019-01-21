package com.taotao.rest.dao;

/**
 * 
 * @ClassName JedisClient
 * @Description redis的基本操作 
 * @Author xll
 * @Date 2019年1月3日 下午2:46:35
 * @Version 1.0
 *
 */
public interface JedisClient {
 
	String get(String key);
	String set(String key,String value);
	String hget(String hkey,String key);
	long hset(String hkey,String key,String value);
	long incr(String key);
	long expire(String key,int second);
	long ttl(String key);
	long del(String key);
	long hdel(String hkey,String key);
}
