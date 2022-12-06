package com.example.gbdpuserac.controller.uac;


import com.example.gbdpbootcore.httpResult.Result;
import com.example.gbdpbootcore.httpResult.ResultGenerator;
import com.example.gbdpbootcore.page.PageRequest;
import com.example.gbdpbootcore.publicToolUtil.GyToolUtil;
import com.example.gbdpbootcore.validation.CreateGroup;
import com.example.gbdpbootcore.validation.UpdateGroup;
import com.example.gbdpuserac.core.BaseController;
import com.example.gbdpuserac.model.UacRoleOffice;
import com.example.gbdpuserac.service.UacRoleOfficeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * UacRoleOffice 控制器
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
@RestController
@RequestMapping("/v1/uac/role/office")
@Api(value = "UacRoleOfficeController", description = "角色部门维护")
@Validated
public class UacRoleOfficeController extends BaseController {

    @Autowired
    private UacRoleOfficeService uacRoleOfficeService;

   // @PreAuthorize("@pms.hasPermission('uac:RoleOffice:edit')")
    @ApiOperation(value = "部门角色绑定功能保存接口", notes = "部门权限绑定功能保存接口")
    @PostMapping
    public Result save(@RequestBody @Validated(CreateGroup.class) UacRoleOffice uacRoleOffice) {
        log.info("UacRoleOfficeController save {}", uacRoleOffice);
        int save = uacRoleOfficeService.saveOrUpdate(uacRoleOffice);
        return save > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("UacRoleOffice表insert失败");
    }

   // @PreAuthorize("@pms.hasPermission('uac:RoleOffice:edit')")
    @ApiOperation(value = "部门角色绑定功能更新接口", notes = "部门角色绑定功能更新接口")
    @PutMapping
    public Result update(@RequestBody @Validated(UpdateGroup.class) UacRoleOffice uacRoleOffice) {
        log.info("UacRoleOfficeController update {}", uacRoleOffice);
        int update = uacRoleOfficeService.saveOrUpdate(uacRoleOffice);
        return update > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("UacRoleOffice表update失败");
    }

   // @PreAuthorize("@pms.hasPermission('uac:RoleOffice:view')")
    @ApiOperation(value = "通过id获取部门角色信息接口", notes = "通过id获取部门角色信息接口")
    @GetMapping("/{id}")
    public Result get(@PathVariable String id) {
        UacRoleOffice acRoleOffice = uacRoleOfficeService.getById(id);
        return ResultGenerator.genSuccessResult(acRoleOffice);
    }

  //  @PreAuthorize("@pms.hasPermission('uac:RoleOffice:view')")
    @ApiOperation(value = "获取全部部门角色信息接口", notes = "获取全部部门角色信息接口")
    @GetMapping
    public Result list(@Validated PageRequest pageRequest, UacRoleOffice uacRoleOffice) {
        return ResultGenerator.genSuccessResult(uacRoleOfficeService.page(pageRequest, uacRoleOffice));
    }

    //@PreAuthorize("@pms.hasPermission('uac:RoleOffice:edit')")
    @ApiOperation(value = "通过id删除部门角色信息接口", notes = "通过id删除部门角色信息接口")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id) {
        int i = uacRoleOfficeService.deleteById(id);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("deleteById失败");
    }

   // @PreAuthorize("@pms.hasPermission('uac:RoleOffice:edit')")
    @ApiOperation(value = "通过ids批量删除部门角色信息接口", notes = "通过ids批量删除部门角色信息接口")
    @DeleteMapping
    public Result deleteByIds(@RequestBody String idsStr) {
        int i = uacRoleOfficeService.deleteByIds(idsStr);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("deleteList失败");
    }
}
