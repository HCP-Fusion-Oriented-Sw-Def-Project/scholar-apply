package com.example.gbdpuserac.security;

import com.example.gbdpbootcore.enums.ErrorCodeEnum;
import com.example.gbdpbootcore.enums.RoleDataScopeEnum;
import com.example.gbdpbootcore.exception.BusinessException;
import com.example.gbdpbootcore.publicToolUtil.GyToolUtil;
import com.example.gbdpuserac.dto.UacRoleDto;
import com.example.gbdpuserac.dto.UacUserDto;
import com.example.gbdpuserac.model.UacRole;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sun.java2d.loops.GeneralRenderer;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;


/**
 * 全局用户工具类，用于获取所有的用户信息，部门，菜单，角色等
 * 获取当前用户
 *
 * @author kongweichang
 */
@Slf4j
@Component
public class UacUserUtils {
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return requestAttributes.getRequest();
    }

    /**
     * 获取当前用户
     *
     * @return 取不到返回 new User()
     * @autho kwc
     */
    public static UacUserDto getUser(TokenStore tokenStore) {
        //使用 Authorization header （postman自动添加的header）
        String token = StringUtils.substringAfter(getRequest().getHeader("Authorization"), "Bearer ");
        if (StringUtils.isEmpty(token)) {
            // 手动添加的header
            token = StringUtils.substringAfter(getRequest().getHeader("token"), "Bearer ");
        }
        log.info("<== preHandle - 权限拦截器.  token={}", token);
        if (StringUtils.isEmpty(token)) {
            // 需要用swagger测试接口的情况下就打开注释
            // return new UacUserDto();
            throw new BusinessException(ErrorCodeEnum.TPC100500017);
        }
        OAuth2Authentication authentication = tokenStore.readAuthentication(token);
        if (GyToolUtil.isNull(authentication)) {
            throw new BusinessException(ErrorCodeEnum.TPC100500018);
        }
        OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(token);
        Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();
        SecurityUser securityUser = (SecurityUser) tokenStore.readAuthentication(token).getUserAuthentication().getPrincipal();
        UacUserDto loginUser = securityUser.getUacUserDto();
        return loginUser;
    }

    public static void setUserInfo2Request(UacUserDto user) {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return;
        }
        request.setAttribute("currentUser", user);
    }

    public static UacUserDto getUserInfoFromRequest() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return new UacUserDto();
        }
        return (UacUserDto) request.getAttribute("currentUser");
    }

    /**
     * 获取当前用户的最高dataScope
     *
     * @return
     */
    public static String getMaxDataScope(UacUserDto user) {
        if (GyToolUtil.isNull(user.getRoleList())) {
            return RoleDataScopeEnum.DATA_SCOPE_CUSTOM.name();
        }
        AtomicReference<String> maxDataScope = new AtomicReference<>(RoleDataScopeEnum.DATA_SCOPE_SELF.name());
        user.getRoleList().stream().filter(Objects::nonNull).forEach(
                role -> {
                    String dataScope = role.getDataScope();
                    if (RoleDataScopeEnum.compareScopeRange(dataScope, maxDataScope.get())) {
                        maxDataScope.set(RoleDataScopeEnum.getDataScope(Integer.valueOf(dataScope)).getName());
                    }
                }
        );
        return maxDataScope.get();
    }

    /**
     * 获取当前用户的最高dataScope对应的角色
     *
     * @return
     */
    public static UacRole getMaxDataScopeRole(UacUserDto user) {
        if (GyToolUtil.isNull(user.getRoleList())) {
            return new UacRole();
        }
        AtomicReference<UacRole> maxDataScopeRole = new AtomicReference<>(new UacRole());
        AtomicReference<String> maxDataScope = new AtomicReference<>(RoleDataScopeEnum.DATA_SCOPE_SELF.name());
        user.getRoleList().stream().filter(Objects::nonNull).forEach(
                role -> {
                    String dataScope = role.getDataScope();
                    if (RoleDataScopeEnum.compareScopeRange(dataScope, maxDataScope.get())) {
                        maxDataScope.set(RoleDataScopeEnum.getDataScope(Integer.valueOf(dataScope)).getName());
                        maxDataScopeRole.set(role);
                    }
                }
        );
        return maxDataScopeRole.get();
    }

    /**
     * 获取当前用户的公司id
     *
     * @return
     */
    public static String getCompanyId(UacUserDto user) {
        if (GyToolUtil.isNull(user.getOfficeList())) {
            return "-1";
        }
        List<String> officeIds = new ArrayList<>();
        user.getOfficeList().stream().filter(Objects::nonNull).forEach(
                office -> officeIds.add(office.getId())
        );
        AtomicReference<Integer> companyGrade = new AtomicReference<>(Integer.MAX_VALUE);
        AtomicReference<String> companyId = new AtomicReference<>("");
        user.getOfficeList().stream().filter(Objects::nonNull).forEach(
                office -> {
                    //todo 1 公司等级为1？最高等级
                    String grade = office.getGrade();
                    if (StringUtils.isNotEmpty(grade)) {
                        Integer gradeInt = Integer.valueOf(grade);
                        Integer min = companyGrade.get();
                        if (gradeInt < min) {
                            companyGrade.set(gradeInt);
                            companyId.set(office.getId());
                        }
                    }
                }
        );

        return companyId.get();
    }

    /**
     * @param
     * @return List<String>
     * @Description 获取用户（叶子结点）的officeId（多个情况下，返回list）
     * @Author kwc
     * @date
     */
    public static List<String> getOfficeId(UacUserDto user) {
        if (GyToolUtil.isNull(user.getOfficeList())) {
            return Collections.singletonList("-1");
        }
        List<String> officeIds = new ArrayList<>();
        user.getOfficeList().stream().filter(Objects::nonNull).forEach(
                office -> officeIds.add(office.getId())
        );
        return officeIds;
    }

    /**
     * 设置不校验数据权限的请求API
     *
     * @return
     */
    public static boolean checkPermitAllUrlFromRequest() {
        Object permitAllTime = getRequest().getAttribute("permitAllTime");
        return permitAllTime != null && (Integer) permitAllTime > 0;
    }

    /**
     * 设置不校验数据权限
     */
    public static void setPermitAllUrl2Request() {
        setPermitAllUrl2Request(PermitAllDataScopeTime.ALL);
    }

    /**
     * 设置不校验数据权限
     *
     * @param range PermitAllDataScopeTime类型，为不校验数据权限次数对应的枚举
     */
    public static void setPermitAllUrl2Request(PermitAllDataScopeTime range) {
        setPermitAllUrl2Request(range.value);
    }

    /**
     * 设置不校验数据权限
     *
     * @param time int,不校验数据权限次数
     */
    public static void setPermitAllUrl2Request(int time) {
        getRequest().setAttribute("permitAllTime", time);
    }

    /**
     * 初始化数据权限
     */
    public static void resetPermitAllUrl2Request() {
        getRequest().removeAttribute("permitAllTime");
    }

    /**
     * 执行查询后，对permitAllTime进行维护
     */
    public static void afterExecuteSql() {
        Object permitAllTime = getRequest().getAttribute("permitAllTime");
        if (permitAllTime == null) {
            return;
        }
        Integer time = (Integer) permitAllTime;
        if (time <= 0) {
            return;
        }
        getRequest().setAttribute("permitAllTime", --time);
    }

    /**
     * PermitAll查询次数
     */
    public enum PermitAllDataScopeTime {
        ALL("all", Integer.MAX_VALUE),
        ONE_TIME("oneTime", 1);

        String name;
        Integer value;

        PermitAllDataScopeTime(String name, Integer value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Integer getValue() {
            return value;
        }

    }
}
