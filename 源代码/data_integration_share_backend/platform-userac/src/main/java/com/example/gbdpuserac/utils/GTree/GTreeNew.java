package com.example.gbdpuserac.utils.GTree;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Data
public class GTreeNew<T> {

    private List<T> headNode;

    private HashMap<String,T> maps;

    private List<T> trees;

    public GTreeNew(List<T> list) throws IllegalAccessException, InstantiationException {
        if(list==null || list.size()<=0){
            return;
        }
        this.headNode = new ArrayList<>();
        this.maps = new HashMap<>();
        HashMap<String,Boolean> isHead = new HashMap<>();

        T type = list.get(0);
        Class clazz = type.getClass();
        NodeField nodeField = new NodeField(clazz);

        if (nodeField.getId()==null||nodeField.getPId()==null){
            return;
        }

        for (T temp:list){
            if (temp==null)
                continue;
            String tid = (String)nodeField.getId().get(temp);
            if (tid==null){
                return;
            }
            if(maps.containsKey(tid)){
                nodeField.getChildren().set(temp,nodeField.getChildren().get(maps.get(tid)));
                maps.replace(tid,temp);
            }else{
                maps.put(tid,temp);
            }
            String tPid = (String)nodeField.getPId().get(temp);;
            if (tPid!=null){
                if(isHead.containsKey(tid))
                    isHead.replace(tid,false);
                else
                    isHead.put(tid,false);
                if(!maps.containsKey(tPid)){
                    T pNode = (T) clazz.newInstance();
                    nodeField.getId().set(pNode,tPid);
                    List<T> tmpList = new ArrayList<>();
                    tmpList.add(temp);
                    nodeField.getChildren().set(pNode,tmpList);
                    maps.put(tPid,pNode);
                    isHead.put(tPid,true);
                }else{
                    T pNode = maps.get(tPid);
                    List<T> children = (List<T>) nodeField.getChildren().get(pNode);
                    if (children==null){
                        children = new ArrayList<>();
                        nodeField.getChildren().set(pNode,children);
                    }
                    children.add(temp);
                }
            }else {
                if(isHead.containsKey(tid))
                    isHead.replace(tid,true);
                else
                    isHead.put(tid,true);
            }
        }

        isHead.forEach((k, v) -> {
            if(v)
                headNode.add(maps.get(k));
        });

        if (nodeField.getSort()!=null){
            sortList(headNode,nodeField);
        }

        this.trees = this.headNode;
    }

    public T getTreeByHeadId(String id){
        if (this.getMaps().containsKey(id))
            return this.getMaps().get(id);
        return null;

    }

    public void sortList(List<T> dto,NodeField nodeField) throws IllegalAccessException {
        if(dto==null||dto.size()==0)
            return;


        Collections.sort(dto, (T x, T y) ->{
            Integer sortX = null;
            Integer sortY = null;
            try {
                sortX = (Integer) nodeField.getSort().get(x);
                sortY = (Integer) nodeField.getSort().get(y);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            if (sortX!=null&&sortY!=null)
                return sortX.compareTo(sortY);
            else
                return 0;

        });
        //Collections.sort(dto, ContentTypeDto::getSort);

        for(T type:dto){
            List<T> children = (List<T>) nodeField.getChildren().get(type);
            sortList(children,nodeField);
        }
    }

}
