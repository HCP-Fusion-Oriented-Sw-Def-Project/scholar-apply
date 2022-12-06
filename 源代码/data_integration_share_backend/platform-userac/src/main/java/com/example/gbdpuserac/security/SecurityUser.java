
package com.example.gbdpuserac.security;

import com.example.gbdpuserac.dto.UacUserDto;
import lombok.Data;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * The class Security user.
 */
@Data
public class SecurityUser implements UserDetails {
	private static final long serialVersionUID = 4872628781561412463L;

	private static final String ENABLE = "ENABLE";

	private Collection<GrantedAuthority> authorities;

	private String userId;

	private String nickName;

	private String loginName;

	private String loginPwd;

	private String status;

	private String delFlag;

	private UacUserDto uacUserDto;

	public SecurityUser(String userId, String loginName, String loginPwd, String nickName, String delFlag, String status, UacUserDto uacUserDto, Collection<GrantedAuthority> grantedAuthorities) {
		this.setUserId(userId);
		this.setLoginName(loginName);
		this.setLoginPwd(loginPwd);
		this.setNickName(nickName);
		this.setStatus(status);
		this.setDelFlag(delFlag);
		this.setUacUserDto(uacUserDto);
		this.authorities = grantedAuthorities;
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.getLoginPwd();
	}

	@Override
	public String getUsername() {
		return this.getLoginName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return StringUtils.equals(this.status, ENABLE);
	}
}
