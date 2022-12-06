package com.example.gbdpuserac.controller.uac;

import com.example.gbdpbootcore.dto.ResetPasswordDto;
import com.example.gbdpbootcore.httpResult.Result;
import com.example.gbdpbootcore.httpResult.ResultCode;
import com.example.gbdpbootcore.httpResult.ResultGenerator;
import com.example.gbdpbootcore.page.PageRequest;
import com.example.gbdpbootcore.page.PageResult;
import com.example.gbdpbootcore.publicToolUtil.GyToolUtil;
import com.example.gbdpbootcore.publicToolUtil.IPUtil;
import com.example.gbdpuserac.core.BaseController;
import com.example.gbdpuserac.core.BaseService;
import com.example.gbdpuserac.dto.UacUserDto;
import com.example.gbdpuserac.model.UacUser;
import com.example.gbdpuserac.security.PcAuthenticationSuccessHandler;
import com.example.gbdpuserac.security.UacUserUtils;
import com.example.gbdpuserac.service.UacUserService;
import com.example.gbdpuserac.service.UacUserTokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * <p>
 * UacUser 控制器
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
@RestController
@RequestMapping("/uac/user")
@Api(value = "UacUserController", description = "用户维护")
@Validated
public class UacUserController extends BaseController {

    @Autowired
    private UacUserService uacUserService;
    @Autowired
    private UacUserTokenService uacUserTokenService;

    @Autowired
    private PcAuthenticationSuccessHandler handler;

    /**
     * 后台新建用户
     */
    // @PreAuthorize("@pms.hasPermission('uac:user:edit')")
    @ApiOperation(value = "用户功能保存接口", notes = "用户功能保存接口")
    @PostMapping
    public Result save(@RequestBody @Valid UacUserDto uacUserDto) {
        if (GyToolUtil.isNull(uacUserDto)) {
            return ResultGenerator.genFailResult(ResultCode.MISSING_PARAMETERS);
        }
        if (CollectionUtils.isEmpty(uacUserDto.getOfficeList())) {
            return ResultGenerator.genFailResult(ResultCode.MISSING_PARAMETERS, "新建用户没有设置机构！");
        }
        if (CollectionUtils.isEmpty(uacUserDto.getRoleList())) {
            return ResultGenerator.genFailResult(ResultCode.MISSING_PARAMETERS, "新建用户没有设置角色！");
        }
        if (uacUserService.checkDuplicateLoginName(uacUserDto)) {
            return ResultGenerator.genFailResult(ResultCode.DUPLICATE_KEY, "账号名重复！");
        }
        if (uacUserService.checkDuplicatePhone(uacUserDto)) {
            return ResultGenerator.genFailResult(ResultCode.DUPLICATE_KEY, "电话号码重复！");
        }
        if (uacUserService.checkDuplicateEmail(uacUserDto)) {
            return ResultGenerator.genFailResult(ResultCode.DUPLICATE_KEY, "邮箱重复！");
        }
        if (StringUtils.isNotEmpty(uacUserDto.getLoginPwd()) &&
                !uacUserService.checkPasswordStrength(uacUserDto.getLoginPwd())){
            return ResultGenerator.genFailResult(ResultCode.SIMPLE_PASSWORD);
        }
        boolean save = uacUserService.saveUser(uacUserDto);
        return save ? ResultGenerator.genSuccessResult("新建用户成功") : ResultGenerator.genFailResult("saveUser ["+ uacUserDto.getLoginName() +"] fail");
    }

    /**
     * 修改用户信息.
     */
    // @PreAuthorize("@pms.hasPermission('uac:user:edit')")
    @ApiOperation(value = "用户功能修改接口", notes = "用户功能修改接口")
    @PutMapping
    public Result update(@RequestBody @Valid UacUserDto uacUserDto, ServletRequest servletRequest) {

        if (GyToolUtil.isNull(uacUserDto)) {
            return ResultGenerator.genFailResult(ResultCode.MISSING_PARAMETERS);
        }
        if (StringUtils.isEmpty(uacUserDto.getId())){
            return ResultGenerator.genFailResult("UserId不能为空");
        }
        if (StringUtils.isEmpty(uacUserDto.getLoginName())){
            return ResultGenerator.genFailResult("登录名不能为空");
        }
        uacUserDto.setLoginName(uacUserDto.getLoginName().trim());

        if (!uacUserService.checkUserExist(uacUserDto.getId())){
            return ResultGenerator.genFailResult(ResultCode.RECORD_NOT_EXIST);
        }
        if (uacUserService.checkDuplicateLoginName(uacUserDto)) {
            return ResultGenerator.genFailResult(ResultCode.DUPLICATE_KEY, "账号名重复！");
        }
        if (uacUserService.checkDuplicatePhone(uacUserDto)) {
            return ResultGenerator.genFailResult(ResultCode.DUPLICATE_KEY, "电话号码重复！");
        }
        if (uacUserService.checkDuplicateEmail(uacUserDto)) {
            return ResultGenerator.genFailResult(ResultCode.DUPLICATE_KEY, "邮箱重复！");
        }
        if (CollectionUtils.isEmpty(uacUserDto.getOfficeList())) {
            return ResultGenerator.genFailResult(ResultCode.MISSING_PARAMETERS, "更新用户没有设置机构！");
        }
        if (CollectionUtils.isEmpty(uacUserDto.getRoleList())) {
            return ResultGenerator.genFailResult(ResultCode.MISSING_PARAMETERS, "更新用户没有设置角色！");
        }
        if (StringUtils.isNotEmpty(uacUserDto.getLoginPwd()) &&
                !uacUserService.checkPasswordStrength(uacUserDto.getLoginPwd())){
            return ResultGenerator.genFailResult(ResultCode.SIMPLE_PASSWORD);
        }
        boolean update = uacUserService.updateUser(uacUserDto);
        if(update&&getUserInfo().getId().equals(uacUserDto.getId())){
            final String clientId =  ((HttpServletRequest) servletRequest).getHeader("client_id");
            handler.updateUserToken2(uacUserService.getUserDto(getUserInfo().getId()),clientId);

        }
        return update ? ResultGenerator.genSuccessResult("更新用户成功") : ResultGenerator.genFailResult("updateUser ["+ uacUserDto.getLoginName() +"] fail");
    }



    @ApiOperation(value = "用户自身功能修改接口", notes = "用户自身功能修改接口")
    @PutMapping(value = "/self")
    public Result updateSelf(@RequestBody @Valid UacUserDto uacUserDto, ServletRequest servletRequest) {
        if (GyToolUtil.isNull(uacUserDto)) {
            return ResultGenerator.genFailResult(ResultCode.MISSING_PARAMETERS);
        }
        if (StringUtils.isEmpty(uacUserDto.getId())){
            return ResultGenerator.genFailResult("UserId不能为空");
        }
        if (!uacUserService.checkUserExist(uacUserDto.getId())){
            return ResultGenerator.genFailResult(ResultCode.RECORD_NOT_EXIST);
        }
        if (uacUserService.checkDuplicateLoginName(uacUserDto)) {
            return ResultGenerator.genFailResult(ResultCode.DUPLICATE_KEY, "账号名重复！");
        }
        if (uacUserService.checkDuplicatePhone(uacUserDto)) {
            return ResultGenerator.genFailResult(ResultCode.DUPLICATE_KEY, "电话号码重复！");
        }
        if (uacUserService.checkDuplicateEmail(uacUserDto)) {
            return ResultGenerator.genFailResult(ResultCode.DUPLICATE_KEY, "邮箱重复！");
        }
        if (StringUtils.isNotEmpty(uacUserDto.getLoginPwd()) &&
                !uacUserService.checkPasswordStrength(uacUserDto.getLoginPwd())){
            return ResultGenerator.genFailResult(ResultCode.SIMPLE_PASSWORD);
        }
        boolean update = uacUserService.updateUser(uacUserDto);
        if(update&&getUserInfo().getId().equals(uacUserDto.getId())){
            final String clientId =  ((HttpServletRequest) servletRequest).getHeader("client_id");
            handler.updateUserToken2(uacUserService.getUserDto(getUserInfo().getId()),clientId);

        }
        return update ? ResultGenerator.genSuccessResult("更新用户成功") : ResultGenerator.genFailResult("updateUser ["+ uacUserDto.getLoginName() +"] fail");
    }


    // @PreAuthorize("@pms.hasPermission('uac:user:view')")
    @ApiOperation("获取指定用户信息")
    @GetMapping(value = "/{id}")
    public Result get(@PathVariable String id) {
        if (StringUtils.isEmpty(id)) {
            return ResultGenerator.genFailResult("UserId不能为空");
        }
        if (!uacUserService.checkUserExist(id)){
            return ResultGenerator.genFailResult(ResultCode.RECORD_NOT_EXIST);
        }
        UacUserDto uacUserDto = uacUserService.getUserDto(id);
        return ResultGenerator.genSuccessResult(uacUserDto);
    }

    // @PreAuthorize("@pms.hasPermission('uac:user:view')")
    @ApiOperation("获取当前用户信息")
    @GetMapping(value = "/self")
    public Result self() {
        UacUserDto userInfo = getUserInfo();
        return ResultGenerator.genSuccessResult(userInfo);
    }

    // @PreAuthorize("@pms.hasPermission('uac:user:view')")
    @ApiOperation("分页查看用户信息:当前除了user本身的字段筛选，只支持roleId和officeId，其他字段请忽略")
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uacRole.id",value = "角色id", required = false, example = "1"),
            @ApiImplicitParam(name = "uacOffice.id",value = "机构id", required = false, example = "1"),
    })
    public Result list(@Valid PageRequest pageRequest, UacUser uacUser) {
        // 数据权限
        //uacUser.getSqlMap().put("dsSql", BaseService.dataScopeFilter(getUserInfo(), "o", "t"));
        // findUser方法还需要完善
        PageResult<UacUser> page= uacUserService.findUser(pageRequest, uacUser);
        //PageResult<UacUser> page = uacUserService.page(pageRequest, uacUser);
        return ResultGenerator.genSuccessResult(page);
    }

    // @PreAuthorize("@pms.hasPermission('uac:user:edit')")
    @ApiOperation("通过id删除用户")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable String id) {
        if (!uacUserService.checkUserExist(id)) {
            return ResultGenerator.genFailResult(ResultCode.RECORD_NOT_EXIST);
        }
        if (StringUtils.isNotEmpty(id) && id.equals(UacUserUtils.getUserInfoFromRequest().getId())) {
            return ResultGenerator.genFailResult(ResultCode.CAN_NOT_DELETE);
        }
        int i = uacUserService.deleteUserById(id);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("delete用户[" + id +"]失败");
    }

    // @PreAuthorize("@pms.hasPermission('uac:user:edit')")
    @ApiOperation("批量删除用户")
    @DeleteMapping
    public Result deleteByIds(@RequestBody String ids) {
        int i = uacUserService.deleteUserByIds(ids);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("批量delete用户[" + ids +"]失败");
    }

    // @PreAuthorize("@pms.hasPermission('uac:user:edit')")
    @ApiOperation("修改指定用户密码")
    @PostMapping(value = "/changPassword")
    public Result changPassword(@RequestBody @Validated ResetPasswordDto resetPasswordDto) {
        String passwordNew = resetPasswordDto.getPasswordNew();
        String confirmPasswordNew = resetPasswordDto.getConfirmPasswordNew();
        if (StringUtils.isEmpty(passwordNew) || StringUtils.isEmpty(confirmPasswordNew)) {
            return ResultGenerator.genFailResult(ResultCode.MISSING_PARAMETERS);
        }
        if (!confirmPasswordNew.equals(passwordNew)) {
            return ResultGenerator.genFailResult(ResultCode.ILLEGAL_PARAMETER, "两次输入的密码不一致！");
        }
        if (!uacUserService.checkPasswordStrength(passwordNew)) {
            return ResultGenerator.genFailResult(ResultCode.SIMPLE_PASSWORD);
        }
        boolean b = uacUserService.updatePassword(resetPasswordDto);
        return b ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult();
    }

    // @PreAuthorize("@pms.hasPermission('uac:user:edit')")
    @ApiOperation("验证登陆名是否存在：返回data：true/false")
    @GetMapping(value = "/checkLoginName")
    public Result checkUserName(@Valid @NotBlank(message = "loginName不能为空！") String loginName) {
        UacUser uacUser = new UacUser();
        uacUser.setLoginName(loginName);
        int count = uacUserService.count(uacUser);
        boolean b = count > 0;
        return ResultGenerator.genSuccessResult(b);
    }

    // @PreAuthorize("@pms.hasPermission('uac:user:edit')")
    @ApiOperation("禁用用户")
    @RequestMapping(value = "/disable/{userId}", method = RequestMethod.POST)
    public Result disableUser(@PathVariable String userId) {
        if (!uacUserService.checkUserExist(userId)) {
            return ResultGenerator.genFailResult(ResultCode.RECORD_NOT_EXIST);
        }
        boolean b = uacUserService.disableUser(userId);
        return b ? ResultGenerator.genSuccessResult("禁用用户成功") : ResultGenerator.genFailResult("禁用用户失败");
    }

    // @PreAuthorize("@pms.hasPermission('uac:user:view')")
    @ApiOperation(value = "通过角色id查看所有用户")
    @GetMapping("/role/{roleId}")
    public Result getUserByRoleId(@PathVariable String roleId, UacUser uacUser, PageRequest pageRequest) {
        if (StringUtils.isEmpty(roleId)) {
            return ResultGenerator.genFailResult(ResultCode.MISSING_PARAMETERS);
        }
        uacUser.getSqlMap().put("dsSql", BaseService.dataScopeFilter(getUserInfo(), "o", "t"));
        PageResult userByRoleId = uacUserService.getUserByRoleId(roleId, uacUser, pageRequest);
        return ResultGenerator.genSuccessResult(userByRoleId);
    }

    // @PreAuthorize("@pms.hasPermission('uac:user:view')")
    @ApiOperation(value = "通过机构id查看所有用户")
    @GetMapping("/office/{officeId}")
    public Result getUserByOfficeId(@PathVariable String officeId, UacUser uacUser, PageRequest pageRequest) {
        if (StringUtils.isEmpty(officeId)) {
            return ResultGenerator.genFailResult(ResultCode.MISSING_PARAMETERS);
        }
        uacUser.getSqlMap().put("dsSql", BaseService.dataScopeFilter(getUserInfo(), "o", "t"));
        PageResult userByRoleId = uacUserService.getUserByOfficeId(officeId, uacUser, pageRequest);
        return ResultGenerator.genSuccessResult(userByRoleId);
    }

    @ApiOperation(value = "获取当前组织以及其上级组织的所有用户")
    @GetMapping("/responsibleUser/{officeId}")
    public Result getResponsibleUserByOfficeId(@PathVariable String officeId, UacUser uacUser){
        if (StringUtils.isEmpty(officeId)) {
            return ResultGenerator.genFailResult(ResultCode.MISSING_PARAMETERS);
        }
        uacUser.getSqlMap().put("dsSql", BaseService.dataScopeFilter(getUserInfo(), "o", "t"));
        return ResultGenerator.genSuccessResult(uacUserService.getResponsibleUserByOfficeId(officeId,uacUser));
    }
}
