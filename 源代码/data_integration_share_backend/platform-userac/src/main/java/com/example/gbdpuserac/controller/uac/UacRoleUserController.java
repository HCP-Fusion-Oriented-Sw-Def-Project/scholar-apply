package com.example.gbdpuserac.controller.uac;


import com.example.gbdpbootcore.httpResult.Result;
import com.example.gbdpbootcore.httpResult.ResultGenerator;
import com.example.gbdpbootcore.page.PageRequest;
import com.example.gbdpbootcore.publicToolUtil.GyToolUtil;
import com.example.gbdpbootcore.validation.CreateGroup;
import com.example.gbdpbootcore.validation.UpdateGroup;
import com.example.gbdpuserac.core.BaseController;
import com.example.gbdpuserac.model.UacRoleUser;
import com.example.gbdpuserac.service.UacRoleUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * UacRoleUser 控制器
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
@RestController
@RequestMapping("/uac/role/user")
@Api(value = "UacRoleUserController", description = "角色用户维护")
@Validated
public class UacRoleUserController extends BaseController {

    @Autowired
    private UacRoleUserService uacRoleUserService;

    //  @PreAuthorize("@pms.hasPermission('uac:roleUser:edit')")
    @ApiOperation(value = "用户角色绑定功能保存接口", notes = "部门权限绑定功能保存接口")
    @PostMapping
    public Result save(@RequestBody @Validated(CreateGroup.class) UacRoleUser uacRoleUser) {
        log.info("UacRoleUserController save {}", uacRoleUser);
        int save = uacRoleUserService.saveOrUpdate(uacRoleUser);
        return save > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("UacRoleUser表insert失败");
    }

    //  @PreAuthorize("@pms.hasPermission('uac:roleUser:edit')")
    @ApiOperation(value = "用户角色绑定功能更新接口", notes = "部门角色绑定功能更新接口")
    @PutMapping
    public Result update(@RequestBody @Validated(UpdateGroup.class) UacRoleUser uacRoleUser) {
        log.info("UacRoleUserController update {}", uacRoleUser);
        int update = uacRoleUserService.saveOrUpdate(uacRoleUser);
        return update > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("UacRoleUser表update失败");
    }

    //  @PreAuthorize("@pms.hasPermission('uac:roleUser:view')")
    @ApiOperation(value = "通过id获取用户角色信息接口", notes = "通过id获取用户角色信息接口")
    @GetMapping("/{id}")
    public Result get(@PathVariable String id) {
        UacRoleUser acRoleUser = uacRoleUserService.getById(id);
        return ResultGenerator.genSuccessResult(acRoleUser);
    }

    // @PreAuthorize("@pms.hasPermission('uac:roleUser:view')")
    @ApiOperation(value = "获取用户角色信息接口", notes = "获取用户角色信息接口")
    @GetMapping
    public Result list(@Validated PageRequest pageRequest, UacRoleUser uacRoleUser) {
        return ResultGenerator.genSuccessResult(uacRoleUserService.page(pageRequest, uacRoleUser));
    }

    // @PreAuthorize("@pms.hasPermission('uac:roleUser:edit')")
    @ApiOperation(value = "通过id删除用户角色信息接口", notes = "通过id删除用户角色信息接口")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id) {
        int i = uacRoleUserService.deleteById(id);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("deleteById失败");
    }

    //@PreAuthorize("@pms.hasPermission('uac:roleUser:edit')")
    //@ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "通过ids批量删除用户角色信息接口", notes = "通过ids批量删除用户角色信息接口")
    @DeleteMapping
    public Result deleteByIds(@RequestBody String idsStr) {
        int i = uacRoleUserService.deleteByIds(idsStr);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("deleteList失败");
    }
}
