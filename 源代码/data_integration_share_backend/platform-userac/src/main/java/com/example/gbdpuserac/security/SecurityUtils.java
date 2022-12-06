package com.example.gbdpuserac.security;

import com.example.gbdpuserac.dto.UacUserDto;
import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.Collection;
import java.util.Set;


/**
 * The class Security utils.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityUtils {

	private static final String AUTH_LOGIN_AFTER_URL = "/user/logout/*";
	private static final String AUTH_LOGOUT_URL = "/user/logout";

	/**
	 * Gets current login name.
	 *
	 * @return the current login name
	 */
	public static String getCurrentLoginName() {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {

			return ((UserDetails) principal).getUsername();

		}

		if (principal instanceof Principal) {

			return ((Principal) principal).getName();

		}

		return String.valueOf(principal);

	}

	public static Set<String> getCurrentAuthorityUrl() {
		Set<String> path = Sets.newHashSet();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		for (final GrantedAuthority authority : authorities) {
			String url = authority.getAuthority();
			if (StringUtils.isNotEmpty(url)) {
				path.add(url);
			}
		}
		path.add(AUTH_LOGIN_AFTER_URL);
		path.add(AUTH_LOGOUT_URL);
		return path;
	}

	/**
	 * 获取Authentication
	 */
	public static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 获取用户
	 */
	public static UacUserDto getUser(Authentication authentication) {
		Object principal = authentication.getPrincipal();
		if (principal instanceof UacUserDto) {
			return (UacUserDto) principal;
		}
		return null;
	}

	/**
	 * 获取用户
	 */
	public static UacUserDto getUser() {
		Authentication authentication = getAuthentication();
		if (authentication == null) {
			return null;
		}
		return getUser(authentication);
	}

	/**
	 * 获取用户角色信息
	 *
	 * @return 角色集合
	 */
	/*public List<Integer> getRoles() {
		Authentication authentication = getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		List<Integer> roleIds = new ArrayList<>();
		authorities.stream()
				.filter(granted -> StrUtil.startWith(granted.getAuthority(), SecurityConstants.ROLE))
				.forEach(granted -> {
					String id = StrUtil.removePrefix(granted.getAuthority(), SecurityConstants.ROLE);
					roleIds.add(Integer.parseInt(id));
				});
		return roleIds;
	}*/
}
