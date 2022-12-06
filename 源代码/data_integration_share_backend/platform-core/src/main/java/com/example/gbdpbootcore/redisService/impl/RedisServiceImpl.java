package com.example.gbdpbootcore.redisService.impl;

import com.example.gbdpbootcore.redisService.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

	@Autowired
    private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private RedisTemplate<Object,Object> redisTemplate;

    @Override
    public void setValue(String key, String value,long expires) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set(key, value, expires, TimeUnit.SECONDS);
    }

    @Override
    public String getValue(String key) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        return ops.get(key);
    }

    @Override
    public void delete(String key) {    	
    	stringRedisTemplate.delete(key);    	    	
    }

	@Override
	public void sadd(String key, String... values) {
		stringRedisTemplate.opsForSet().add(key, values);
		
	}

	@Override
	public Set<String> smembers(String key) {
		return stringRedisTemplate.opsForSet().members(key);
	}

    @Override
    public void setObject(String key, Object obj, long expires) {
        redisTemplate.opsForValue().set(key, obj, expires, TimeUnit.SECONDS);
    }

    @Override
    public void setObjectWithoutExpires(String key, Object obj) {
        redisTemplate.opsForValue().set(key, obj);
    }

    @Override
    public void updateObject(String key, Object obj) {
        Long expires = redisTemplate.getExpire(key);
        if(expires != null){
            redisTemplate.opsForValue().set(key, obj, expires, TimeUnit.SECONDS);
        }

    }

    @Override
    public Object getObject(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
	public void setValueWithoutExpires(String key, String value) {
		ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set(key, value);
	}
    
    

}
