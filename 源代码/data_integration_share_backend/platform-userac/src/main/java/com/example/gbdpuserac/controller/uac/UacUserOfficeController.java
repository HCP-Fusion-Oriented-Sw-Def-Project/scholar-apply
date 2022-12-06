package com.example.gbdpuserac.controller.uac;

import com.example.gbdpbootcore.httpResult.Result;
import com.example.gbdpbootcore.httpResult.ResultGenerator;
import com.example.gbdpbootcore.page.PageRequest;
import com.example.gbdpbootcore.publicToolUtil.GyToolUtil;
import com.example.gbdpbootcore.publicToolUtil.StringUtil;
import com.example.gbdpbootcore.validation.CreateGroup;
import com.example.gbdpbootcore.validation.UpdateGroup;
import com.example.gbdpuserac.core.BaseController;
import com.example.gbdpuserac.dto.UacUserDto;
import com.example.gbdpuserac.model.UacUserOffice;
import com.example.gbdpuserac.service.UacUserOfficeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * UacUserOffice 控制器
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
@RestController
@RequestMapping("/v1/uac/user/office")
@Api(value = "UacUserOfficeController", description = "用户部门维护")
@Validated
public class UacUserOfficeController extends BaseController {

    @Autowired
    private UacUserOfficeService uacUserOfficeService;


    // @PreAuthorize("@pms.hasPermission('uac:userOffice:edit')")
    @ApiOperation(value = "用户部门绑定功能保存接口", notes = "用户部门绑定功能保存接口")
    @PostMapping
    public Result save(@RequestBody @Validated(CreateGroup.class) UacUserOffice uacUserOffice) {
        log.info("UacUserOfficeController save {}", uacUserOffice);
        int save = uacUserOfficeService.saveOrUpdate(uacUserOffice);
        return save > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("UacUserOffice表insert失败");
    }

    // @PreAuthorize("@pms.hasPermission('uac:userOffice:edit')")
    @ApiOperation(value = "用户部门绑定功能更新接口", notes = "用户部门绑定功能更新接口")
    @PutMapping
    public Result update(@RequestBody @Validated(UpdateGroup.class) UacUserOffice uacUserOffice) {
        log.info("UacRoleUserController update {}", uacUserOffice);
        int update = uacUserOfficeService.saveOrUpdate(uacUserOffice);
        return update > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("UacUserOffice表update失败");
    }

    // @PreAuthorize("@pms.hasPermission('uac:userOffice:view')")
    @ApiOperation(value = "通过id获取用户部门功能接口", notes = "通过id获取用户部门功能接口")
    @GetMapping("/{id}")
    public Result get(@PathVariable String id) {
        UacUserOffice acUserOffice = uacUserOfficeService.getById(id);
        return ResultGenerator.genSuccessResult(acUserOffice);
    }

    // @PreAuthorize("@pms.hasPermission('uac:userOffice:view')")
    @ApiOperation("分页查看用户部门信息")
    @GetMapping
    public Result list(@Validated PageRequest pageRequest, UacUserOffice uacUserOffice) {
        return ResultGenerator.genSuccessResult(uacUserOfficeService.page(pageRequest, uacUserOffice));
    }

    // @PreAuthorize("@pms.hasPermission('uac:userOffice:edit')")
    @ApiOperation("通过id删除用户部门信息")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id) {
        int i = uacUserOfficeService.deleteById(id);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("deleteById失败");
    }

    //@PreAuthorize("@pms.hasPermission('uac:userOffice:edit')")
    @ApiOperation("通过ids批量删除用户部门信息")
    @DeleteMapping
    public Result deleteByIds(@RequestBody String idsStr) {
        int i = uacUserOfficeService.deleteByIds(idsStr);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("deleteList失败");
    }

    // @PreAuthorize("@pms.hasPermission('uac:userOffice:view')")
    @ApiOperation(value = "通过部门id查询其部门人员")
    @GetMapping("/all/{officeId}")
    public Result getAllUser(@PathVariable String officeId) {
        List<UacUserDto> userDtoList = uacUserOfficeService.listUserByOfficeId(officeId);
        return ResultGenerator.genSuccessResult(userDtoList);
    }

    // @PreAuthorize("@pms.hasPermission('uac:userOffice:edit')")
    @ApiOperation(value = "修改部门关联人员")
    @PostMapping("/all/{officeId}")
    public Result updateAllUser(@PathVariable String officeId, @RequestBody List<String> userIds) {
        boolean b = uacUserOfficeService.updateUserByOfficeId(officeId, userIds);
        return b ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("修改部门关联人员失败！");
    }
}
