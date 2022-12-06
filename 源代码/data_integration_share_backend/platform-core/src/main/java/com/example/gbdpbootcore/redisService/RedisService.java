package com.example.gbdpbootcore.redisService;

import java.util.Set;

public interface RedisService {
	
	/**
     * 设置key-value
     * @param key
     * @param value
     */
    void setValue(String key, String value, long expires);

    void setValueWithoutExpires(String key, String value);

    /**
     * 获取key
     * @param key
     * @return
     */
    String getValue(String key);

    /**
     * 删除key
     * @param key
     */
    void delete(String key);


    void sadd(String key, String... values);

    Set<String> smembers(String key);

    void setObject(String key, Object obj, long expires);

    void setObjectWithoutExpires(String key, Object obj);

    void updateObject(String key, Object obj);


    Object getObject(String key);

}
