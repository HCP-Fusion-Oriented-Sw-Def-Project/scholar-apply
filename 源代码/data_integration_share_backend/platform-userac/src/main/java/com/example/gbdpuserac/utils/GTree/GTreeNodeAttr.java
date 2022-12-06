package com.example.gbdpuserac.utils.GTree;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数形节点注解，标注在对应节点上，type值对应GTreeNodeAttrEnum中的值
 *  * id 标注在 id上
 *  * name 标注在name对应属性上，可以不标注
 *  * pId 标注在父亲id对应属性上，必须标注
 *  * sort 标注在排序对应的属性上，可以不标注，则不会对结果进行排序
 *  标注形式例如@GTreeNodeAttr(type = GTreeNodeAttrEnum.pId)
 *  详细例子可以查看ContentType实体类 ContentTypeController 中
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GTreeNodeAttr {

    GTreeNodeAttrEnum type() default GTreeNodeAttrEnum.id;

}
