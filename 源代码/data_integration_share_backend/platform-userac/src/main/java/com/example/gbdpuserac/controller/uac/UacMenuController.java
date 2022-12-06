package com.example.gbdpuserac.controller.uac;

import com.example.gbdpbootcore.httpResult.Result;
import com.example.gbdpbootcore.httpResult.ResultCode;
import com.example.gbdpbootcore.httpResult.ResultGenerator;
import com.example.gbdpbootcore.page.PageRequest;
import com.example.gbdpbootcore.publicToolUtil.GyToolUtil;
import com.example.gbdpuserac.core.BaseController;
import com.example.gbdpuserac.model.UacMenu;
import com.example.gbdpuserac.model.UacRole;
import com.example.gbdpuserac.service.UacMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
 * UacMenu 控制器
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
@Api(value = "UacMenuController", description = "菜单维护")
@RestController
@RequestMapping("/uac/menu")
@Validated
public class UacMenuController extends BaseController {

    @Autowired
    private UacMenuService uacMenuService;

    // todo 缺的方法
    @ApiOperation(value = "通过id查询当前单个菜单接口")
    @GetMapping("/{id}")
    //@PreAuthorize("@pms.hasPermission('uac:menu:edit')")
    public Result get(@PathVariable String id) {
        log.info("UacMenuController get {}", id);
        UacMenu uacMenu = uacMenuService.getById(id);
        if (GyToolUtil.isNull(uacMenu)) {
            return ResultGenerator.genFailResult(ResultCode.RECORD_NOT_EXIST);
        }
        // 是否需要返回本身和其下级菜单？
        return ResultGenerator.genSuccessResult(uacMenu);
    }

    //@PreAuthorize("@pms.hasPermission('uac:menu:view')")
    @ApiOperation(value = "查询当前所有的菜单接口", notes = "查询当前所有的菜单接口")
    @GetMapping("/all")
    public Result listAll() {
        log.info("UacMenuController listAll");
        return ResultGenerator.genSuccessResult(uacMenuService.listAllMenu());
    }

    //@PreAuthorize("@pms.hasPermission('uac:menu:view')")
    @ApiOperation(value = "分页查询当前所有的菜单接口", notes = "分页查询当前所有的菜单接口")
    @GetMapping
    public Result list(@Valid PageRequest pageRequest, UacMenu uacMenu) {
        log.info("UacMenuController list {}", pageRequest);
        return ResultGenerator.genSuccessResult(uacMenuService.page(pageRequest, uacMenu));
    }

    //@PreAuthorize("@pms.hasPermission('uac:menu:view')")
    @ApiOperation(value = "查询树形的菜单接口（可传roleId，返回所有菜单，标记该角色对应的树形菜单）", notes = "角色")
    @GetMapping("/tree")
    public Result treeData(@RequestParam(value = "roleId", required = false) String roleId) {
        log.info("UacMenuController treeData roleId-{}", roleId);
        if (StringUtils.isEmpty(roleId)) {
            List<Object> objects = uacMenuService.menuTree();
            return ResultGenerator.genSuccessResult(objects);
        }
        List<Object> objects = uacMenuService.menuTreeByRoleId(roleId);
        return ResultGenerator.genSuccessResult(objects);
    }

    //@PreAuthorize("@pms.hasPermission('uac:menu:edit')")
    @ApiOperation(value = "删除单个菜单接口")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id) {
        log.info("UacMenuController delete {}", id);
        boolean hasChild = uacMenuService.hasChild(id);
        if (hasChild) {
            return ResultGenerator.genFailResult(ResultCode.HAVE_CHILD_CANT_DELETE);
        }
        int i = uacMenuService.deleteMenuById(id);
        return i > 0 ? ResultGenerator.genSuccessResult("删除成功") : ResultGenerator.genFailResult("删除失败");
    }

    //@PreAuthorize("@pms.hasPermission('uac:menu:edit')")
    @ApiOperation(value = "批量删除菜单接口(ids)")
    @DeleteMapping
    public Result deleteByIds(@Valid @NotBlank String ids) {
        log.info("UacMenuController deleteMany {}", ids);
        // 判断是否有子节点
        String[] idArr = ids.split(",");
        boolean hasChild = uacMenuService.hasChild(idArr);
        if (hasChild) {
            return ResultGenerator.genFailResult(ResultCode.HAVE_CHILD_CANT_DELETE);
        }
        return uacMenuService.deleteMenuByIds(ids) > 0 ?
                ResultGenerator.genSuccessResult("删除成功") : ResultGenerator.genFailResult("删除失败");
    }

    @ApiOperation(value = "菜单功能保存接口", notes = "菜单功能保存接口")
    @PostMapping
    //@PreAuthorize("@pms.hasPermission('uac:menu:edit')")
    public Result save(@RequestBody @Valid UacMenu uacMenu) {
        log.info("UacMenuController save {}", uacMenu);
        if (GyToolUtil.isNull(uacMenu.getIsShow())){
            // todo 参数校验，IsShow必填
            uacMenu.setIsShow("0");
        }
        UacMenu parentMenu = uacMenuService.getById(uacMenu.getParentId());
        if (GyToolUtil.isNull(parentMenu)) {
            return ResultGenerator.genFailResult(ResultCode.RECORD_NOT_EXIST, "父级菜单不存在");
        }
        boolean duplicateMenu = uacMenuService.checkDuplicateMenu(uacMenu);
        if (duplicateMenu) {
            return ResultGenerator.genFailResult(ResultCode.DUPLICATE_KEY, "名称，权限字段或url重复");
        }
        int i = uacMenuService.saveMenu(uacMenu);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("insert失败");
    }

    //@PreAuthorize("@pms.hasPermission('uac:menu:edit')")
    @ApiOperation(value = "菜单功能更新接口", notes = "菜单功能更新接口")
    @PutMapping
    public Result update(@RequestBody @Valid UacMenu uacMenu) {
        log.info("UacMenuController update {}", uacMenu);
        if (GyToolUtil.isNull(uacMenu.getId())) {
            return ResultGenerator.genFailResult("Mene表id字段为空");
        }
        UacMenu parentMenu = uacMenuService.getById(uacMenu.getParentId());
        if (GyToolUtil.isNull(parentMenu)) {
            return ResultGenerator.genFailResult(ResultCode.RECORD_NOT_EXIST, "父级菜单不存在");
        }
        boolean duplicateMenu = uacMenuService.checkDuplicateMenu(uacMenu);
        if (duplicateMenu) {
            return ResultGenerator.genFailResult(ResultCode.DUPLICATE_KEY, "名称，权限字段或url重复");
        }
        int i = uacMenuService.updateMenu(uacMenu);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("update失败");
    }

    //@PreAuthorize("@pms.hasPermission('uac:menu:view')")
    @ApiOperation(value = "查询当前权限下的菜单接口", notes = "查询当前权限下的菜单接口")
    @GetMapping("/role")
    public Result listByRoleId(@Valid PageRequest pageRequest,
                               @RequestParam("roleIds") @Valid @NotBlank(message = "roleIds不能为空") String roleIdStr) {
        List<String> roleIds = Arrays.asList(roleIdStr.split(","));
        log.info("UacMenuController listMenuByRoleIds [{}] , [{} : {}]", pageRequest, "roleIds的size", roleIds.size());
        if(GyToolUtil.isNull(roleIds)){
            roleIds= super.getUserInfo().getRoleList()
                    .stream().filter(Objects :: nonNull)
                    .map(UacRole::getId)
                    .collect(Collectors.toList());;
        }
        return ResultGenerator.genSuccessResult(uacMenuService.listByRoleId(pageRequest, roleIds));
    }

}
