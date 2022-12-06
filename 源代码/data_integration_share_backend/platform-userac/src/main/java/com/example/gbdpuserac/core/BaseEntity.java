package com.example.gbdpuserac.core;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.gbdpbootcore.validation.UpdateGroup;
import com.example.gbdpuserac.dto.UacUserDto;
import com.example.gbdpuserac.security.UacUserUtils;
import com.example.gbdpuserac.utils.GTree.GTreeNodeAttr;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.annotation.Resource;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.*;

/**
 * The class Base entity.
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode
public class BaseEntity implements Serializable {
	private static final long serialVersionUID = 2393269568666085258L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotBlank(message = "Id不能为空", groups = UpdateGroup.class)
	@ApiModelProperty(value = "id:新建不填，更新必填")
	@Length(max =64, message = "id长度异常！")
	@GTreeNodeAttr()
	protected String id;

	/**
	 * 创建人
	 */
	@Column(name = "create_by")
	@ApiModelProperty(value = "创建人", hidden = true)
	protected String createBy;

	/**
	 * 创建时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "create_date")
	@ApiModelProperty(value = "创建时间", hidden = true)
	protected Date createDate;
	/**
	 * 最近操作人
	 */
	@Column(name = "update_by")
	@ApiModelProperty(value = "最近操作人", hidden = true)
	protected String updateBy;

	/**
	 * 更新时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "update_date")
	@ApiModelProperty(value = "更新时间", hidden = true)
	protected Date updateDate;

	/**
	 * 备注信息
	 */
	@Column(name = "remarks")
	@ApiModelProperty(value = "备注信息", hidden = true)
	@Length(max = 255)
	protected String remarks;

	/**
	 * 删除标记
	 */
	@JSONField(serialize = false)
	@JsonIgnore
	@Column(name = "del_flag")
	@ApiModelProperty(value = "del_flag", hidden = true)
	protected String delFlag;

	/**
	 * 自定义SQL（SQL标识，SQL内容）
	 */
	@JSONField(serialize = false)
	@JsonIgnore
	@Transient
	@ApiModelProperty(value = "sqlMap", hidden = true)
	protected Map<String, String> sqlMap = new HashMap<>(2);

	/**
	 * 树形排序需要
	 */
	@JSONField(serialize = false)
	@JsonIgnore
	@ApiModelProperty(value = "has_child", hidden = true)
	private boolean hasChild;


	/**
	 * Is new boolean.
	 *
	 * @return the boolean
	 */
	@JSONField(serialize = false)
	@JsonIgnore
	@ApiModelProperty(value = "del_flag", hidden = true)
	public boolean isNew() {
		return StringUtils.isEmpty(this.id);
	}

	@JSONField(serialize = false)
	/*@Resource
	@JsonIgnore
	private TokenStore tokenStore;*/
	/**
	 * save操作处理
	 * 给createBy，date, id, del_flag赋值
	 */
	public void preSave() {
		if (this.isNew()){
			setId(UUID.randomUUID().toString());
			setDelFlag("0");
		}
		UacUserDto user = UacUserUtils.getUserInfoFromRequest();
		if (StringUtils.isNotBlank(user.getId())){
			this.createBy = user.getId();
			this.updateBy = user.getId();
		}
		this.createDate = new Date();
		this.updateDate = new Date();
	}

	/**
	 * update操作处理
	 * 给updateBy，date赋值
	 */
	public void preUpdate() {
		UacUserDto user = UacUserUtils.getUserInfoFromRequest();
		if (StringUtils.isNotBlank(user.getId())){
			this.updateBy = user.getId();
		}
		this.updateDate = new Date();
	}
}
