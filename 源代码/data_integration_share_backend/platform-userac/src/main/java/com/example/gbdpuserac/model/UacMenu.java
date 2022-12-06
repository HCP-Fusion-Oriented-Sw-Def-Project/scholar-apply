package com.example.gbdpuserac.model;

import com.example.gbdpuserac.core.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.CollectionUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("uacMenu对象")
public class UacMenu extends BaseEntity {

    private static final long serialVersionUID = 1L;
    /**
     * 父级Id
     */
    @NotBlank(message = "父级菜单编号不能为空")
    @ApiModelProperty(value = "父级Id", example = "1")
    @Length(max = 64, message = "父级菜单编号不能超过64个字符")
    private String parentId;
    /**
     * 所有父级菜单编号
     */
    @ApiModelProperty(value = "所有父级菜单编号", hidden = true)
    @Length(max = 2000, message = "所有父级菜单编号不能超过2000个字符")
    private String parentIds;
    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名不能为空")
    @Length(max = 100, message = "菜单名不能超过100个字符")
    @ApiModelProperty(value = "菜单名称", example = "测试工具")
    private String name;
    /**
     * 菜单编码
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ApiModelProperty(value = "菜单编码（英文标记）", example = "test_menu")
    @Length(max = 100, message = "菜单编码不能超过100个字符")
    private String code;
    /**
     * 菜单URL
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ApiModelProperty(value = "菜单路径（可显示菜单才有，非增删改查）", example = "/demo/test/menu")
    @Length(max = 150, message = "菜单URL不能超过150个字符")
    private String url;
    /**
     * 图标
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ApiModelProperty(value = "菜单图标", example = "")
    private String icon;
    /**
     * 是否在菜单中显示
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ApiModelProperty(value = "是否显示，1：显示，0：不显示", example = "0")
    @Length(max = 1, message = "菜单显示标记，长度不对")
    private String isShow;
    /**
     * 菜单排序 - 从小到大
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ApiModelProperty(value = "菜单排序 - 从小到大", example = "999")
    private Integer sort;
    /**
     * 层级(最多三级1,2,3) 后台填写
     */
    @ApiModelProperty(value = "层级(最多三级1,2,3) 后台填写", hidden = true)
    private Integer level;

    /**
     * 菜单类型   0：目录   1：菜单   2：按钮
     */
    @ApiModelProperty(value = "菜单类型 0：目录 1：菜单 2：按钮", example = "0")
    private Integer type;
    /**
     * 权限标识
     */
    @ApiModelProperty(value = "菜单路径（操作资源菜单才有（增删改查））", example = "demo:test:edit")
    @Length(max = 200, message = "权限标识长度不能超过200")
    private String permission;


    /**
     * controller层调用的方法   ，并将数据以list的形式返回
     * 对输入list进行树形排序
     *
     * @param menu 源列表
     * @param markMenuIds 需要标记的menuId列表
     */
    @JsonIgnore
    public static List<Object> menuTree(List<UacMenu> menu, List<String> markMenuIds){
        // 设置hasChild字段
        setHasChild(menu);
        List<Object> list = new ArrayList<>();
        for (UacMenu x : menu) {
            Map<String,Object> mapArr = new LinkedHashMap<String, Object>();
            //判断是否为父极
            if(x.isHasChild() && x.getParentId().equals("0")){
                mapArr.put("id", x.getId());
                mapArr.put("parentId", x.getParentId());
                /*mapArr.put("parentIds", x.getParentIds());*/
                mapArr.put("name", x.getName());
                mapArr.put("isShow", x.getIsShow());
                mapArr.put("sort", x.getSort());
                mapArr.put("level", x.getLevel());
                mapArr.put("url", x.getUrl());
                mapArr.put("icon", x.getIcon());
                mapArr.put("mark", markMenuIds.contains(x.getId()));
                //去子集查找遍历
                mapArr.put("child", menuChild(x.getId(), menu, markMenuIds));
                list.add(mapArr);
            }
        }
        return list;
    }

    /**
     * 对输入list进行树形排序(一维列表)
     *
     * @param list 排序后列表 (一维列表)
     * @param sourceList 源列表
     * @param parentId 当前父节点id
     */
    @JsonIgnore
    public static void treeArraySort(List<UacMenu> list, List<UacMenu> sourceList, String parentId) {
        for (int i = 0; i < sourceList.size(); i++) {
            UacMenu e = sourceList.get(i);
            if (StringUtils.isNotEmpty(e.getParentId()) && e.getParentId().equals(parentId)) {
                list.add(e);
                // 判断是否还有子节点, 有则继续获取子节点
                for (int j = 0; j < sourceList.size(); j++) {
                    UacMenu child = sourceList.get(j);
                    if (child.getParentId() != null && child.getParentId().equals(e.getId())) {
                        treeArraySort(list, sourceList, e.getId());
                        break;
                    }
                }
            }
        }
    }

    public static List<?> menuChild(String id, List<UacMenu> menu, List<String> markMenuIds){ //子集查找遍历
        List<Object> lists = new ArrayList<Object>();
        for(UacMenu a : menu){
            Map<String,Object> childArray = new LinkedHashMap<String, Object>();
            if(a.getParentId().equals(id) ){
                childArray.put("id", a.getId());
                childArray.put("parentId", a.getParentId());
                //childArray.put("parentIds", a.getParentIds());
                childArray.put("name", a.getName());
                childArray.put("isShow", a.getIsShow());
                childArray.put("hasChildren", a.isHasChild());
                childArray.put("sort", a.getSort());
                childArray.put("level", a.getLevel());
                childArray.put("url", a.getUrl());
                childArray.put("icon", a.getIcon());
                childArray.put("mark", markMenuIds.contains(a.getId()));

                childArray.put("child", menuChild(a.getId(), menu, markMenuIds));
                lists.add(childArray);
            }
        }
        return lists;
    }

    private static void setHasChild(List<UacMenu> menu) {
        if (CollectionUtils.isEmpty(menu)) {
            return;
        }

        List<UacMenu> finalMenu = menu;
        menu = menu.stream().filter(Objects::nonNull).map(
                menuBean -> {
                    String id = menuBean.getId();
                    menuBean.setHasChild(checkHasChild(id, finalMenu));


                    return menuBean;
                }
        ).collect(Collectors.toList());
    }

    private static boolean checkHasChild(String id, List<UacMenu> menuList) {
        for (UacMenu menu : menuList) {
            if (menu != null && menu.getParentIds().contains(',' + id + ',')) {
                return true;
            }
        }
        return false;
    }
}
