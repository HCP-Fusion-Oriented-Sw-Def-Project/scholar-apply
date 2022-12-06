package com.example.gbdpuserac.config;

import lombok.Data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


@Data
public class CTree<T> {

    //当前结点类详细信息
    private T node;

    //当前节点id
    private String id;

    //当前节点排序值
    private Integer sort;

    //当前节点子节点
    private List<CTree> children;

    public CTree(){

    }

    public CTree(T node,String id,Integer sort){
        this.node = node;
        this.id = id;
        this.sort = sort;
    }

    //types：要转化为树形结构的数组；root：根节点的id值；id：结点对象中的id属性名；
    //pid 节点对象中的父亲id属性名；sortAttribute：节点中的排序属性名
    public  List<CTree> getTree(List<T> types,String root,String id,String pid,String sortAttribute) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<CTree> result = new ArrayList<>();
        HashMap<String,CTree> maps = new HashMap<>();
        Class clazz = null;
        Method getId = null;
        Method getPid = null;
        Method getSort = null;
        for(T type:types){
            clazz = type.getClass();
            getId = clazz.getMethod("get"+upperFirstLatter(id));
            getPid = clazz.getMethod("get"+upperFirstLatter(pid));
            getSort = clazz.getMethod("get"+upperFirstLatter(sortAttribute));
            CTree<T> tmp = null;
            String typeId = (String)getId.invoke(type);
            Integer typeSort = (Integer)getSort.invoke(type);
            if(maps.containsKey(typeId)){
                tmp = maps.get(typeId);
                tmp.setNode(type);
                tmp.setId(typeId);
                tmp.setSort(typeSort);
            }else{
                tmp = new CTree<>(type,typeId,typeSort);
                maps.put((String)getId.invoke(type),tmp);
            }
            String typePid =(String) getPid.invoke(type);
            if(typePid.equals(root)){
                result.add(tmp);
            }else{
                if(maps.containsKey(typePid)){
                    CTree<T> pTmp = maps.get(typePid);
                    if(pTmp.getChildren()==null){
                        pTmp.children = new ArrayList<>();
                    }
                    pTmp.children.add(tmp);
                }else{
                    CTree<T> pTmp = new CTree<T>();
                    pTmp.setId(typePid);
                    pTmp.children = new ArrayList<>();
                    pTmp.children.add(tmp);
                    maps.put(typeId,pTmp);
                }
            }

        }
        sortList(result);
        return result;

    }

    public String upperFirstLatter(String letter){
        return letter.substring(0, 1).toUpperCase()+letter.substring(1);
    }

    public void sortList(List<CTree> dto){
        if(dto==null||dto.size()==0)
            return;

        //利用lambda传入排序行为，下面两种方式都可以
        //dto.sort((ContentTypeDto x, ContentTypeDto y) -> x.getSort() < y.getSort()? 1 : -1);

        Collections.sort(dto, (CTree x, CTree y) -> x.getSort().compareTo(y.getSort()));
        //Collections.sort(dto, ContentTypeDto::getSort);
        for(CTree type:dto){
            sortList(type.getChildren());
        }
    }

}
