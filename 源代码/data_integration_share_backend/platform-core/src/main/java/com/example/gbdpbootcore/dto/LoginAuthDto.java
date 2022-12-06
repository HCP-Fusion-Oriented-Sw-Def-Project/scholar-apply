
package com.example.gbdpbootcore.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * The class Login auth dto.
 */
@Data
@ApiModel(value = "登录人信息")
public class LoginAuthDto implements Serializable {
	private static final long serialVersionUID = -1137852221455042256L;
	@ApiModelProperty(value = "用户ID")
	private String userId;
	@ApiModelProperty(value = "登录名")
	private String loginName;
	@ApiModelProperty(value = "用户名")
	private String userName;
	@ApiModelProperty(value = "组织ID")
	private String officeId;
	@ApiModelProperty(value = "组织名称")
	private String officeName;

	public LoginAuthDto() {
	}

	public LoginAuthDto(String userId, String loginName, String userName) {
		this.userId = userId;
		this.loginName = loginName;
		this.userName = userName;
	}

	public LoginAuthDto(String userId, String loginName, String userName, String officeId, String officeName) {
		this.userId = userId;
		this.loginName = loginName;
		this.userName = userName;
		this.officeId = officeId;
		this.officeName = officeName;
	}
}
