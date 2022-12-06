package com.example.gbdpuserac.security;

import com.example.gbdpbootcore.publicToolUtil.GyToolUtil;
import com.example.gbdpbootcore.util.PublicUtil;
import com.example.gbdpuserac.dto.UacUserDto;
import com.example.gbdpuserac.model.UacUser;
import com.example.gbdpuserac.service.UacMenuService;
import com.example.gbdpuserac.service.UacUserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The class Uac user details service.
 */
@Configuration
@Component
public class UacUserDetailsServiceImpl implements UserDetailsService {

	@Resource
	private UacUserService uacUserService;

	@Resource
	private UacMenuService uacMenuService;

	/**
	 * Load user by username user details.
	 *
	 * @param username the username
	 *
	 * @return the user details
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
 		Collection<GrantedAuthority> grantedAuthorities = null;
		UacUser user = uacUserService.getByLoginName(username);
		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
		}
		List<String> permissionList = uacMenuService.listMenuPerByUserId(user.getId());
		List<String> permissionNewList = new ArrayList<String>();
		for(String string : permissionList){
			if(!GyToolUtil.isNull(string)){
				permissionNewList.add(string);
			}
		}
		if (PublicUtil.isNotEmpty(permissionNewList)){
			grantedAuthorities = AuthorityUtils.createAuthorityList(permissionNewList.toArray(new String[permissionNewList.size()]));
		}
		UacUserDto uacUserDto = uacUserService.getUserInfoList(username);
		uacUserDto.setLoginPwd(null);
		uacUserDto.setPwdErrorCount(null);
		return new SecurityUser(user.getId(), username, user.getPassword(),
				user.getName(), user.getDelFlag(), user.getStatus(),uacUserDto, grantedAuthorities);
	}

}
