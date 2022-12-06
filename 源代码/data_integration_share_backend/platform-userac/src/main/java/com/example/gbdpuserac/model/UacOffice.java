package com.example.gbdpuserac.model;

import com.example.gbdpbootcore.validation.CreateGroup;
import com.example.gbdpuserac.core.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * 机构表
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("uacOffice对象")
public class UacOffice extends BaseEntity {

    private static final long serialVersionUID = 1L;
    /**
     * 父级编号
     */
    @NotBlank(message = "机构父Id不能为空")
    @ApiModelProperty(value = "父级Id", example = "1")
    @Length(max = 64, message = "父级编号不能超过64个字符")
    private String parentId;
    /**
     * 所有父级编号
     */
    @ApiModelProperty(value = "所有父级编号", hidden = true)
    @Length(max = 2000, message = "所有父级编号不能超过2000个字符")
    private String parentIds;
    /**
     * 名称
     */
    @NotBlank(message = "机构名不能为空", groups = CreateGroup.class)
    @Length(max = 100, message = "机构名不能超过100个字符")
    @ApiModelProperty(value = "机构名", example = "运维部")
    private String name;
    /**
     * 机构类型
     */
    @ApiModelProperty(value = "机构类型", example = "2")
    @Length(max = 1, message = "机构类型不能超过1个字符")
    private String type;
    /**
     * 机构等级
     */
    @ApiModelProperty(value = "机构等级", hidden = true)
    @Length(max = 11, message = "机构等级不能超过11个字符")
    private String grade;
    /**
     * 排序号
     */
    @ApiModelProperty(value = "机构排序", example = "999")
    private Integer sort;
    /**
     * 联系地址
     */
    @ApiModelProperty(value = "机构地址", example = "www.gbdp.com")
    @Length(max = 255, message = "机构地址不能超过255个字符")
    private String address;
    /**
     * 邮政编码
     */
    @ApiModelProperty(value = "邮政编码", example = "100000")
    @Length(max = 100, message = "邮政编码不能超过255个字符")
    private String zipCode;
    /**
     * 电话
     */
    @ApiModelProperty(value = "电话", example = "010-10000000")
    @Length(max = 200, message = "电话不能超过200个字符")
    private String phone;
    /**
     * 邮件地址
     */
    @ApiModelProperty(value = "邮箱", example = "company@gbdp.com")
    @Length(max = 200, message = "邮箱不能超过200个字符")
    private String email;
    /**
     * 传真
     */
    @ApiModelProperty(value = "传真", example = "010-10000000")
    @Length(max = 200, message = "传真不能超过200个字符")
    private String fax;
    /**
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用 1-启用 0-禁用", example = "1")
    @Length(max = 11, message = "是否启用不能超过11个字符")
    private String useAble;
    /**
     * 主负责人
     */
    @ApiModelProperty(value = "主负责人", example = "")
    private UacUser primaryPerson;
    /**
     * 副负责人
     */
    @ApiModelProperty(value = "副负责人", example = "")
    private UacUser deputyPerson;

    /**
     * 主负责人id
     */
    @ApiModelProperty(value = "主负责人id", hidden = true)
    @Length(max = 64, message = "主负责人id不能超过64个字符")
    private String primaryPersonId;
    /**
     * 副负责人id
     */
    @ApiModelProperty(value = "副负责人id", hidden = true)
    @Length(max = 64, message = "副负责人id不能超过64个字符")
    private String deputyPersonId;
    /**
     * 父节点
     */
    @ApiModelProperty(value = "父节点", example = "")
    private UacOffice parent;

    /**
     * 对输入list进行树形排序
     *
     * @param list 排序后列表
     * @param sourceList 源列表
     * @param parentId 当前父节点id
     */
    @JsonIgnore
    public static void treeArraySort(List<UacOffice> list, List<UacOffice> sourceList, String parentId) {
        for (int i = 0; i < sourceList.size(); i++) {
            UacOffice e = sourceList.get(i);
            if (StringUtils.isNotEmpty(e.getParentId()) && e.getParentId().equals(parentId)) {
                list.add(e);
                // 判断是否还有子节点, 有则继续获取子节点
                for (int j = 0; j < sourceList.size(); j++) {
                    UacOffice child = sourceList.get(j);
                    if (child.getParentId() != null && child.getParentId().equals(e.getId())) {
                        treeArraySort(list, sourceList, e.getId());
                        break;
                    }
                }
            }
        }
    }

    /**
     * controller层调用的方法   ，并将数据以list的形式返回
     * 对输入list进行树形排序
     *
     * @param offices 源列表
     * @param markOfficeIds 需要标记的机构id列表
     */
    @JsonIgnore
    public static List<Object> officeTree(List<UacOffice> offices, List<String> markOfficeIds){
        // 设置hasChild字段
        setHasChild(offices);
        List<Object> list = new ArrayList<>();
        for (UacOffice x : offices) {
            Map<String,Object> mapArr = new LinkedHashMap<>();
            //判断是否为父极
            if(x.isHasChild() && x.getParentId().equals("0")){
                mapArr.put("id", x.getId());
                mapArr.put("parentId", x.getParentId());
                mapArr.put("parentIds", x.getParentIds());
                mapArr.put("name", x.getName());
                mapArr.put("useAble", x.getUseAble());
                mapArr.put("sort", x.getSort());
                mapArr.put("grade", x.getGrade());
                mapArr.put("address", x.getAddress());
                mapArr.put("type", x.getType());
                mapArr.put("hasChildren", true);
                mapArr.put("mark", markOfficeIds.contains(x.getId()));
                mapArr.put("email", x.getEmail());
                mapArr.put("fax", x.getFax());
                mapArr.put("zipCode", x.getZipCode());
                mapArr.put("phone", x.getPhone());
                mapArr.put("parent", x.parent);
                mapArr.put("primaryPerson", x.getPrimaryPerson());
                mapArr.put("deputyPerson", x.getDeputyPerson());

                //去子集查找遍历
                mapArr.put("child", officeChild(x.getId(), offices, markOfficeIds));
                list.add(mapArr);
            }
        }
        return list;
    }

    public static List<?> officeChild(String id, List<UacOffice> office, List<String> markOfficeIds){ //子集查找遍历
        List<Object> lists = new ArrayList<Object>();
        for(UacOffice a : office){
            Map<String,Object> childArray = new LinkedHashMap<String, Object>();
            if(a.getParentId().equals(id) ){
                childArray.put("id", a.getId());
                childArray.put("parentId", a.getParentId());
                childArray.put("parentIds", a.getParentIds());
                childArray.put("name", a.getName());
                childArray.put("useAble", a.getUseAble());
                childArray.put("sort", a.getSort());
                childArray.put("grade", a.getGrade());
                childArray.put("address", a.getAddress());
                childArray.put("type", a.getType());
                childArray.put("hasChildren", a.isHasChild());
                childArray.put("mark", markOfficeIds.contains(a.getId()));
                childArray.put("email", a.getEmail());
                childArray.put("fax", a.getFax());
                childArray.put("phone", a.getPhone());
                childArray.put("zipCode", a.getZipCode());
                childArray.put("parent", a.parent);
                childArray.put("primaryPerson", a.getPrimaryPerson());
                childArray.put("deputyPerson", a.getDeputyPerson());
                childArray.put("child", officeChild(a.getId(), office, markOfficeIds));
                lists.add(childArray);
            }
        }
        return lists;
    }

    private static void setHasChild(List<UacOffice> office) {
        if (CollectionUtils.isEmpty(office)) {
            return;
        }

        List<UacOffice> finaloffice = office;
        office = office.stream().filter(Objects::nonNull).map(
                officeBean -> {
                    String id = officeBean.getId();
                    officeBean.setHasChild(checkHasChild(id, finaloffice));


                    return officeBean;
                }
        ).collect(Collectors.toList());
    }

    private static boolean checkHasChild(String id, List<UacOffice> officeList) {
        for (UacOffice office : officeList) {
            if (office != null && office.getParentIds().contains(',' + id + ',')) {
                return true;
            }
        }
        return false;
    }
}