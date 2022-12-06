package com.example.gbdpuserac.utils.GTree;

import lombok.Data;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Data
public class GTree<T> {

    private List<GTreeNode<T>> headNode ;

    private HashMap<String,GTreeNode<T>> maps;

    private List<GTreeNode<T>> trees;


    public GTree(List<T> list) throws IllegalAccessException {
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
            GTreeNode nodeTmp = new GTreeNode(nodeField,temp);
            if (nodeTmp.getId()==null){
                return;
            }
            if(maps.containsKey(tid)){
                maps.replace(tid,nodeTmp);
            }else{
                maps.put(tid,nodeTmp);
            }
            String tPid = nodeTmp.getPId();
            if (tPid!=null){
                if(isHead.containsKey(tid))
                    isHead.replace(tid,false);
                else
                    isHead.put(tid,false);
                if(!maps.containsKey(tPid)){
                    GTreeNode<T> pNode = new GTreeNode<T>();
                    pNode.setId(tPid);
                    pNode.setChildren(new ArrayList<>());
                    pNode.getChildren().add(nodeTmp);
                    maps.put(tPid,pNode);
                    isHead.put(tPid,true);
                }else{
                    GTreeNode<T> pNode = maps.get(tPid);
                    if (pNode.getChildren()==null){
                        pNode.setChildren(new ArrayList<>());
                    }
                    pNode.getChildren().add(nodeTmp);
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
            sortList(headNode);
        }

        this.trees = this.headNode;

    }

    public GTreeNode<T> getTreeByHeadId(String id){
        if (this.getMaps().containsKey(id))
            return this.getMaps().get(id);
        return null;

    }


    public String upperFirstLatter(String letter){
        return letter.substring(0, 1).toUpperCase()+letter.substring(1);
    }

    public void sortList(List<GTreeNode<T>> dto){
        if(dto==null||dto.size()==0)
            return;


        Collections.sort(dto, (GTreeNode<T> x, GTreeNode<T> y) ->{
            if (x.getSort()!=null&&y.getSort()!=null)
                return x.getSort().compareTo(y.getSort());
            else
                return 0;
        });
        //Collections.sort(dto, ContentTypeDto::getSort);
        for(GTreeNode<T> type:dto){
            sortList(type.getChildren());
        }
    }


}
