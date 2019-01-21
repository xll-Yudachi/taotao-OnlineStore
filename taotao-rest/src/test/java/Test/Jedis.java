package Test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sun.org.apache.xml.internal.security.Init;

import redis.clients.jedis.JedisCluster;

public class Jedis {

	/**
	 * 
	 * @Description 集群版测试 
	 * @Author xll
	 * @Date 2019年1月2日 下午10:19:11
	 *
	 */
	/*@Test
	public void test() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		JedisCluster jedisCluster = (JedisCluster)applicationContext.getBean("redisClient");
		jedisCluster.set("hello", "jedis");
		System.out.println(jedisCluster.get("hello"));
		jedisCluster.close();
	}*/
	
	@Test
	public void test() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		JedisCluster jedisCluster = (JedisCluster)applicationContext.getBean("redisClient");
		for(int i=0;i<=1000;i++) {
		System.out.println(jedisCluster.hdel("INDEX_CATEGORY_REDIS_KEY",i+""));
		}
	}
}
