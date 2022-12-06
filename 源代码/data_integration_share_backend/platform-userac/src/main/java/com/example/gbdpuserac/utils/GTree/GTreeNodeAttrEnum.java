package com.example.gbdpuserac.utils.GTree;

/**
 * id 标注在 id上
 * name 标注在name对应属性上，可以不标注
 * pId 标注在父亲id对应属性上，必须标注
 * sort 标注在排序对应的属性上，可以不标注，则不会对结果进行排序
 */
public enum GTreeNodeAttrEnum {

    id,name,pId,sort,children

}
