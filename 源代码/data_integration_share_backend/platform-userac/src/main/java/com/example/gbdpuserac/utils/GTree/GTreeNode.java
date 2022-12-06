package com.example.gbdpuserac.utils.GTree;

import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Data
public class GTreeNode<T> {

    //当前结点类详细信息


    //当前节点id
    private String id;

    private String pId;

    private String name;

    //当前节点排序值
    private Integer sort;

    private T details;

    //当前节点子节点
    private List<GTreeNode<T>> children;

    public GTreeNode(){

    }

    public GTreeNode(T details,String id,Integer sort){
        this.details = details;
        this.id = id;
        this.sort = sort;
    }

    public GTreeNode(NodeField nodeField,T details) throws IllegalAccessException {

        this.id = (String)nodeField.getId().get(details);
        this.pId = (String)nodeField.getPId().get(details);
        if (nodeField.getName()!=null)
            this.name = (String)nodeField.getName().get(details);
        if (nodeField.getSort()!=null)
            this.sort = (Integer)nodeField.getSort().get(details);
        this.details = details;
    }

}
