package com.example.gbdpbootcore.publicToolUtil;


import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Objects;

public class BeanToBeanUtil {
    /**
     * 方法说明：将bean转化为另一种bean实体
     * 
     * @param object
     * @param entityClass
     * @return
     */
    public static <T> T convertBean(Object object, Class<T> entityClass) {
        if(null == object) {
            return null;
        }
        return JSON.parseObject(JSON.toJSONString(object), entityClass);
    }
    
    /**
    *@Description copy list
    *@param obj copy前list
     *@param target-copy目标list
     *@param classObj
    *@return void
    *@Author lhf
    *@date 
    */
    public static <T> void copyList(Object obj, List<T> target, Class<T> classObj) {
        if ((!Objects.isNull(obj)) && (!Objects.isNull(target))) {
            List list1 = (List) obj;
            list1.forEach(item -> {
                try {
                    T data = classObj.newInstance();
                    BeanUtils.copyProperties(item, data);
                    target.add(data);
                } catch (InstantiationException e) {
                } catch (IllegalAccessException e) {
                }


            });
        }
    }
}
