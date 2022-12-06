package com.example.gbdpuserac.controller.uac;

import com.example.gbdpbootcore.httpResult.Result;
import com.example.gbdpbootcore.httpResult.ResultCode;
import com.example.gbdpbootcore.httpResult.ResultGenerator;
import com.example.gbdpbootcore.page.PageRequest;
import com.example.gbdpbootcore.page.PageResult;
import com.example.gbdpbootcore.publicToolUtil.GyToolUtil;
import com.example.gbdpuserac.core.BaseController;
import com.example.gbdpuserac.core.BaseService;
import com.example.gbdpuserac.dto.UacRoleDto;
import com.example.gbdpuserac.model.UacOffice;
import com.example.gbdpuserac.model.UacRole;
import com.example.gbdpuserac.model.UacRoleMenu;
import com.example.gbdpuserac.service.UacOfficeService;
import com.example.gbdpuserac.service.UacRoleMenuService;
import com.example.gbdpuserac.service.UacRoleService;
import com.example.gbdpuserac.service.UacUserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * UacRole 控制器
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
@RestController
@RequestMapping("/uac/role")
@Api(value = "UacRoleController", description = "角色维护")
@Validated
public class UacRoleController extends BaseController {

    @Autowired
    private UacRoleService uacRoleService;
    @Autowired
    private UacUserService uacUserService;
    @Autowired
    private UacRoleMenuService uacRoleMenuService;
    @Autowired
    private UacOfficeService uacOfficeService;

    // @PreAuthorize("@pms.hasPermission('uac:role:edit')")
    @ApiOperation(value = "角色保存接口", notes = "角色功能保存接口")
    @PostMapping
    public Result save(@RequestBody @Valid UacRole uacRole) {
        log.info("UacRoleController save {}", uacRole);
        if (GyToolUtil.isNull(uacRole)) {
            return ResultGenerator.genFailResult(ResultCode.MISSING_PARAMETERS);
        }
        boolean duplicateRole = uacRoleService.checkDuplicateRole(uacRole);
        if (duplicateRole) {
            return ResultGenerator.genFailResult(ResultCode.DUPLICATE_KEY, "记录重复(名称或者编码)");
        }
        boolean save = uacRoleService.saveOrUpdateRole(uacRole);
        return save ? ResultGenerator.genSuccessResult("新建角色成功") : ResultGenerator.genFailResult("新建角色失败");
    }

    //  @PreAuthorize("@pms.hasPermission('uac:role:edit')")
    @ApiOperation(value = "角色功能更新接口", notes = "角色功能更新接口")
    @PutMapping
    public Result update(@RequestBody @Valid UacRole uacRole) {
        log.info("UacRoleController update {}", uacRole);
        // todo Validated 没生效
        if (GyToolUtil.isNull(uacRole) || GyToolUtil.isNull(uacRole.getId())) {
            return ResultGenerator.genFailResult(ResultCode.MISSING_PARAMETERS);
        }
        boolean duplicateRole = uacRoleService.checkDuplicateRole(uacRole);
        if (duplicateRole) {
            return ResultGenerator.genFailResult(ResultCode.DUPLICATE_KEY, "记录重复(名称或者编码)");
        }
        boolean update = uacRoleService.saveOrUpdateRole(uacRole);
        return update ? ResultGenerator.genSuccessResult("更新角色成功") : ResultGenerator.genFailResult("更新角色失败");
    }

    //@PreAuthorize("@pms.hasPermission('uac:role:view')")
    @ApiOperation(value = "获取角色信息(带树形菜单)")
    @GetMapping("/{id}")
    public Result get(@PathVariable String id) {
        UacRoleDto uacRole = uacRoleService.getRoleWithMenu(id);
        if (GyToolUtil.isNull(uacRole)) {
            return ResultGenerator.genFailResult(ResultCode.RECORD_NOT_EXIST);
        }
        return ResultGenerator.genSuccessResult(uacRole);
    }

    // @PreAuthorize("@pms.hasPermission('uac:role:view')")
    @ApiOperation(value = "分页查询角色接口", notes = "分页查询角色接口")
    @GetMapping
    public Result list(@Validated PageRequest pageRequest, UacRole uacRole) {
        log.info("UacRoleController list {}", pageRequest);
        // 数据权限
        uacRole.getSqlMap().put("dsSql", BaseService.dataScopeFilter(getUserInfo(), "uo", "uu"));
        PageResult<UacRole> page = uacRoleService.page(pageRequest, uacRole);
        List<UacRole> list = page.getList().stream().filter(Objects::nonNull).map(
                role -> {
                    // 查询当前角色对应菜单
                    UacRoleMenu uacRoleMenu = new UacRoleMenu();
                    uacRoleMenu.setRoleId(role.getId());
                    List<UacRoleMenu> select = uacRoleMenuService.list(uacRoleMenu);
                    List<String> allMenuIdByRoleId = select.stream().map(UacRoleMenu::getMenuId).collect(Collectors.toList());
                    role.setUacActions(allMenuIdByRoleId);
                    return role;
                }
        ).collect(Collectors.toList());
        page.setList(list);
        return ResultGenerator.genSuccessResult(page);    }

    // @PreAuthorize("@pms.hasPermission('uac:role:view')")
    @ApiOperation(value = "查询全部角色接口", notes = "查询全部角色接口")
    @GetMapping("/all")
    public Result all(UacRole uacRole) {
        return ResultGenerator.genSuccessResult(uacRoleService.list(uacRole));
    }

    // @PreAuthorize("@pms.hasPermission('uac:role:view')")
    @ApiOperation(value = "通过用户id查询所有角色接口", notes = "通过用户id查询所有角色接口")
    @GetMapping("/listRoleByUserId")
    public Result listRoleByUserId(@Valid PageRequest pageRequest,
                                   @RequestParam @NotBlank(message = "userId不能为空") String userId) {
        log.info("UacRoleController list {}", pageRequest);
        return ResultGenerator.genSuccessResult(uacRoleService.listRolesByUserId(pageRequest, userId));
    }

    // @PreAuthorize("@pms.hasPermission('uac:role:edit')")
    @ApiOperation(value = "通过id删除角色接口")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable @NotBlank String id) {
        int i = uacRoleService.deleteRoleById(id);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("deleteById失败");
    }

    // @PreAuthorize("@pms.hasPermission('uac:role:edit')")
    @ApiOperation(value = "通过id批量删除角色接口")
    @DeleteMapping
    public Result deleteByIds(@RequestBody @NotBlank String idsStr) {
        int i = uacRoleService.deleteRoleByIds(idsStr);
        return i > 0 ? ResultGenerator.genSuccessResult("批量删除角色成功") : ResultGenerator.genFailResult("批量删除角色失败");
    }

    @ApiOperation(value = "保存角色与菜单映射")
    @PostMapping("/saveMenu/{roleId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuIds", value = "menuIds,以逗号分割，例如1,2,3", required = true, example = "1,2,3"),
    })
    public Result saveRoleAndMenu(@PathVariable String roleId,
                                  @RequestParam @Valid @NotBlank(message = "menuIds字段不能为空") String menuIds) {

        List<String> menuIdList = Arrays.asList(menuIds.split(","));
        boolean b = uacRoleService.saveRoleAndMenu(roleId, menuIdList);
        return b ? ResultGenerator.genSuccessResult("保存角色与菜单映射成功") : ResultGenerator.genFailResult("保存角色与菜单映射失败");
    }

    @ApiOperation(value = "保存角色与机构映射")
    @PostMapping("/saveOffice/{roleId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "officeIds", value = "OfficeIds,以逗号分割，例如1,2,3", required = true, example = "1,2,3"),
    })
    public Result saveRoleAndOffice(@PathVariable String roleId,
                                    @RequestParam String officeIds) {
        // 校验Office是否可用
        List<String> officeIdList = uacOfficeService.filterDisableOffice(Arrays.asList(officeIds.split(",")));
        boolean b = uacRoleService.saveRoleAndOffice(roleId, officeIdList);
        return b ? ResultGenerator.genSuccessResult("保存角色与机构映射成功") : ResultGenerator.genFailResult("保存角色与机构映射失败");
    }

    @ApiOperation(value = "保存角色与用户映射")
    @PostMapping("/saveUser/{roleId}")
    public Result saveRoleAndUser(@PathVariable String roleId,
                                  @RequestParam @Valid @NotBlank(message = "userId不能为空") String userIds) {
        String[] userIdArr = userIds.split(",");
        // 参数合法性校验
        if (!uacRoleService.checkRoleExist(roleId) ||
                !uacUserService.checkUserExist(userIdArr)) {
            return ResultGenerator.genFailResult(ResultCode.RECORD_NOT_EXIST);
        }
        boolean b = uacRoleService.saveRoleAndUser(roleId, userIdArr);
        return b ? ResultGenerator.genSuccessResult("保存角色与用户映射成功") : ResultGenerator.genFailResult("保存角色与用户映射失败");
    }

    @ApiOperation(value = "删除角色与用户映射")
    @DeleteMapping("/deleteUser/{roleId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userIds", value = "userIds", type = "query",required = true, example = "123"),
    })
    public Result deleteRoleAndUser(@PathVariable String roleId, @RequestParam @Valid @NotBlank(message = "userId不能为空") String userIds) {
        String[] userIdArr = userIds.split(",");
        // 参数合法性校验
        if (!uacRoleService.checkRoleExist(roleId) ||
                !uacUserService.checkUserExist(userIdArr)) {
            return ResultGenerator.genFailResult(ResultCode.RECORD_NOT_EXIST);
        }
        boolean b = uacRoleService.deleteRoleAndUser(roleId, userIdArr);
        return b ? ResultGenerator.genSuccessResult("删除角色与用户映射成功") : ResultGenerator.genFailResult("删除角色与用户映射失败");
    }
}
