package com.ccx.util;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisCluster;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis 工具类
 * @author lilong
 *
 */
public class RedisUtil {
	@Autowired
	private JedisCluster jedisCluster;


	/**
	 * 插入String 类型的数据
	 * @param key
	 * @param value
	 */
	public void setString(String key,String value){
		try {
			jedisCluster.set(key,value);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 获取String类型的数据
	 * @param key
	 * @return
	 */
	public  String getString(String key) {
		String value = null;
		try {
			value = jedisCluster.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 获取对象
	 * @param key
	 * @return
	 */
	public Object get(String key){
		return ObjectUtil.bytesToObject(get(key.getBytes()));
	}

	/**
	 * 获取byte数据
	 * @param key
	 * @return
	 */
	public  byte[] get(byte[] key) {
		byte[] value = null;
		try {
			value = jedisCluster.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 放入对象
	 * @param key
	 * @param obj
	 */
	public void set(String key,Object obj){
		set(key.getBytes(),ObjectUtil.objectToBytes(obj));
	}

	/**
	 * 放入对象,设置缓存时长
	 * @param key
	 * @param obj
	 */
	public void set(String key,Object obj,int time){
		set(key.getBytes(),ObjectUtil.objectToBytes(obj));
		jedisCluster.expire(key.getBytes(), time);
	}
	public  void set(byte[] key, byte[] value) {

		try {
			jedisCluster.set(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public  void set(byte[] key, byte[] value, int time) {
		try {
			jedisCluster.set(key, value);
			jedisCluster.expire(key, time);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	public  void hset(byte[] key, byte[] field, byte[] value) {
		try {
			jedisCluster.hset(key, field, value);
		} catch (Exception e) {
		}
	}

	public  void hset(String key, String field, String value) {
		try {
			jedisCluster.hset(key, field, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取数据
	 *
	 * @param key
	 * @return
	 */
	public  String hget(String key, String field) {

		String value = null;
		try {
			value = jedisCluster.hget(key, field);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return value;
	}

	/**
	 * 获取数据
	 *
	 * @param key
	 * @return
	 */
	public  byte[] hget(byte[] key, byte[] field) {
		byte[] value = null;
		try {
			value = jedisCluster.hget(key, field);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return value;
	}

	public  void hdel(byte[] key, byte[] field) {
		try {
			jedisCluster.hdel(key, field);
		} catch (Exception e) {
		}
	}
	public  void hdel(String key, String field) {
		try {
			jedisCluster.hdel(key, field);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 存储REDIS队列 顺序存储
	 * @param  key reids键名
	 * @param  value 键值
	 */
	public  void lpush(byte[] key, byte[] value) {
		try {
			jedisCluster.lpush(key, value);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	/**
	 * 存储REDIS队列 顺序存储
	 * @param  key reids键名
	 * @param  value 键值
	 */
	public  void lpush(byte[] key, byte[] value,int time) {
		try {
			jedisCluster.lpush(key, value);
			jedisCluster.expire(key, time);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	/**
	 * 存储REDIS队列 反向存储
	 * @param  key reids键名
	 * @para   value 键值
	 */
	public  void rpush(byte[] key, byte[] value) {
		try {
			jedisCluster.rpush(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将列表 source 中的最后一个元素(尾元素)弹出，并返回给客户端
	 * @param  key reids键名
	 * @param  destination
	 */
	public  void rpoplpush(byte[] key, byte[] destination) {
		try {
			jedisCluster.rpoplpush(key, destination);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	/**
	 * 获取队列数据
	 * @param  key 键名
	 * @return
	 */
	public List<byte[]> lpopList(byte[] key) {

		List<byte[]> list = null;
		try {
			list = jedisCluster.lrange(key, 0, -1);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return list;
	}

	/**
	 * 获取队列数据
	 * @param  key 键名
	 * @return
	 */
	public  byte[] rpop(byte[] key) {

		byte[] bytes = null;
		try {
			bytes = jedisCluster.rpop(key);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return bytes;
	}

	public  void hmset(Object key, Map<String, String> hash) {
		try {
			jedisCluster.hmset(key.toString(), hash);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public  void hmset(Object key, Map<String, String> hash, int time) {
		try {
			jedisCluster.hmset(key.toString(), hash);
			jedisCluster.expire(key.toString(), time);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public  List<String> hmget(Object key, String... fields) {
		List<String> result = null;
		try {
			result = jedisCluster.hmget(key.toString(), fields);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public  Set<String> hkeys(String key) {
		Set<String> result = null;
		try {
			result = jedisCluster.hkeys(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public  List<byte[]> lrange(byte[] key, int from, int to) {
		List<byte[]> result = null;
		try {
			return jedisCluster.lrange(key, from, to);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public  Map<byte[], byte[]> hgetAll(byte[] key) {
		Map<byte[], byte[]> result = null;
		try {
			result = jedisCluster.hgetAll(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public  Map<String, String> hgetAll(String key) {
		Map<String, String> result = null;
		try {
			result = jedisCluster.hgetAll(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public  void del(byte[] key) {
		try {
			jedisCluster.del(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public  long llen(byte[] key) {
		long len = 0;
		try {
			jedisCluster.llen(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return len;
	}
	public  void delRedis(String key){
		del(key.getBytes());
	}

}
