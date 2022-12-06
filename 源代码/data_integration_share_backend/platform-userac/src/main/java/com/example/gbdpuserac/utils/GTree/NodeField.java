package com.example.gbdpuserac.utils.GTree;

import lombok.Data;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class NodeField{
    private Field id;
    private Field name;
    private Field sort;
    private Field pId;
    private Field children;

    public NodeField(Class<?> clazz){
        Field[] fieldsArr = getAllFields(clazz);
        for (Field field:fieldsArr){
            GTreeNodeAttr attr = field.getAnnotation(GTreeNodeAttr.class);
            if (attr ==null){
                continue;
            }
            if (attr.type().equals(GTreeNodeAttrEnum.id)){
                this.id = field;
                field.setAccessible(true);
            }else if (attr.type().equals(GTreeNodeAttrEnum.name)){
                this.name = field;
                field.setAccessible(true);
            }else if (attr.type().equals(GTreeNodeAttrEnum.sort)){
                this.sort = field;
                field.setAccessible(true);
            }else if (attr.type().equals(GTreeNodeAttrEnum.pId)){
                this.pId = field;
                field.setAccessible(true);
            }else if (attr.type().equals(GTreeNodeAttrEnum.children)){
                this.children = field;
                field.setAccessible(true);
            }
        }

    }

    public  Field[] getAllFields(Class clazz){
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null){
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }

}
