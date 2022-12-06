package com.example.gbdpuserac.controller.uac;


import com.example.gbdpbootcore.httpResult.Result;
import com.example.gbdpbootcore.httpResult.ResultGenerator;
import com.example.gbdpbootcore.page.PageRequest;
import com.example.gbdpbootcore.publicToolUtil.GyToolUtil;
import com.example.gbdpbootcore.validation.CreateGroup;
import com.example.gbdpbootcore.validation.UpdateGroup;
import com.example.gbdpuserac.core.BaseController;
import com.example.gbdpuserac.model.UacRoleMenu;
import com.example.gbdpuserac.service.UacRoleMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * UacRoleMenu 控制器
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
@RestController
@RequestMapping("/v1/uac/role/menu")
@Api(value = "UacRoleMenuController", description = "角色菜单维护")
@Validated
public class UacRoleMenuController extends BaseController {

    @Autowired
    private UacRoleMenuService uacRoleMenuService;

   // @PreAuthorize("@pms.hasPermission('uac:roleMenu:edit')")
    @ApiOperation(value = "角色菜单功能保存接口", notes = "角色菜单功能保存接口")
    @PostMapping
    public Result save(@RequestBody @Validated(CreateGroup.class) UacRoleMenu uacRoleMenu) {
        log.info("UacRoleMenuController save {}", uacRoleMenu);
        int i = uacRoleMenuService.saveOrUpdate(uacRoleMenu);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("insert失败");
    }

   // @PreAuthorize("@pms.hasPermission('uac:roleMenu:edit')")
    @ApiOperation(value = "角色菜单功能更新接口", notes = "角色菜单功能更新接口")
    @PutMapping
    public Result update(@RequestBody @Validated(UpdateGroup.class) UacRoleMenu uacRoleMenu) {
        log.info("UacRoleMenuController update {}", uacRoleMenu);
        int i = uacRoleMenuService.saveOrUpdate(uacRoleMenu);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("update失败");
    }

   // @PreAuthorize("@pms.hasPermission('uac:roleMenu:view')")
    @ApiOperation(value = "通过id删除角色菜单接口", notes = "通过id删除角色菜单接口")
    @GetMapping("/{id}")
    public Result get(@PathVariable String id) {
        UacRoleMenu acRoleMenu = uacRoleMenuService.getById(id);
        return ResultGenerator.genSuccessResult(acRoleMenu);
    }

   // @PreAuthorize("@pms.hasPermission('uac:roleMenu:view')")
    @ApiOperation(value = "查询所有角色菜单接口", notes = "查询所有角色菜单接口")
    @GetMapping
    public Result list(@Validated PageRequest pageRequest, UacRoleMenu uacRoleMenu) {
        return ResultGenerator.genSuccessResult(uacRoleMenuService.page(pageRequest, uacRoleMenu));
    }

   // @PreAuthorize("@pms.hasPermission('uac:roleMenu:edit')")
    @ApiOperation(value = "通过id删除角色菜单接口", notes = "通过id删除角色菜单接口")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id) {
        int i = uacRoleMenuService.deleteById(id);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("deleteById失败");
    }

   // @PreAuthorize("@pms.hasPermission('uac:roleMenu:edit')")
    @ApiOperation(value = "通过ids批量删除角色菜单接口", notes = "通过ids批量删除角色菜单接口")
    @DeleteMapping
    public Result deleteByIds(@RequestBody String idsStr) {
        int i = uacRoleMenuService.deleteByIds(idsStr);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("deleteList失败");
    }
}
