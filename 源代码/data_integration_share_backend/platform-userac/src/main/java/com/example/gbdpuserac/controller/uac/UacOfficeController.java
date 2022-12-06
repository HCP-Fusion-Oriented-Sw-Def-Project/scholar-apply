package com.example.gbdpuserac.controller.uac;

import com.example.gbdpbootcore.httpResult.Result;
import com.example.gbdpbootcore.httpResult.ResultCode;
import com.example.gbdpbootcore.httpResult.ResultGenerator;
import com.example.gbdpbootcore.page.PageRequest;
import com.example.gbdpbootcore.page.PageResult;
import com.example.gbdpbootcore.publicToolUtil.GyToolUtil;
import com.example.gbdpuserac.core.BaseController;
import com.example.gbdpuserac.core.BaseService;
import com.example.gbdpuserac.dto.UacOfficeDto;
import com.example.gbdpuserac.model.UacOffice;
import com.example.gbdpuserac.service.UacOfficeService;
import com.example.gbdpuserac.service.UacUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * <p>
 * UacOffice 控制器
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
@RestController
@RequestMapping("/uac/office")
@Api(value = "UacOfficeController", description = "部门维护")
@Validated
public class UacOfficeController extends BaseController {

    @Autowired
    private UacOfficeService uacOfficeService;
    @Autowired
    private UacUserService uacUserService;

    //@PreAuthorize("@pms.hasPermission('uac:office:edit')")
    @ApiOperation(value = "部门保存接口")
    @PostMapping
    public Result save(@RequestBody @Valid UacOffice uacOffice) {
        log.info("UacOfficeController save {}", uacOffice);
        if (GyToolUtil.isNull(uacOffice)) {
            return ResultGenerator.genFailResult(ResultCode.MISSING_PARAMETERS);
        }
        UacOffice parentOffice = uacOfficeService.getById(uacOffice.getParentId());
        if (GyToolUtil.isNull(parentOffice)) {
            return ResultGenerator.genFailResult(ResultCode.RECORD_NOT_EXIST, "父级机构不存在");
        }
        boolean duplicateOffice = uacOfficeService.checkDuplicateOffice(uacOffice);
        if (duplicateOffice) {
            return ResultGenerator.genFailResult(ResultCode.DUPLICATE_KEY, "记录重复(名称和父级机构)");
        }
        String deputyPersonId = uacOffice.getDeputyPersonId();
        String primaryPersonId = uacOffice.getPrimaryPersonId();
        if (StringUtils.isNotEmpty(deputyPersonId)) {
            boolean deputyPersonExist = uacUserService.checkUserExist(deputyPersonId);
            if (!deputyPersonExist) {
                return ResultGenerator.genFailResult(ResultCode.RECORD_NOT_EXIST, "副负责人用户不存在！");
            }
        }
        if (StringUtils.isNotEmpty(primaryPersonId)) {
            boolean primaryPersonExist = uacUserService.checkUserExist(primaryPersonId);
            if (!primaryPersonExist) {
                return ResultGenerator.genFailResult(ResultCode.RECORD_NOT_EXIST, "主负责人用户不存在！");
            }
        }
        boolean b = uacOfficeService.saveOffice(uacOffice);
        return b ? ResultGenerator.genSuccessResult("新建成功") : ResultGenerator.genFailResult("新建失败");
    }

    //@PreAuthorize("@pms.hasPermission('uac:office:edit')")
    @ApiOperation(value = "部门功能更新接口")
    @PutMapping
    public Result update(@RequestBody @Valid UacOffice uacOffice) {
        log.info("UacOfficeController update {}", uacOffice);
        // todo Validated 验证失败
        if (GyToolUtil.isNull(uacOffice) || StringUtils.isEmpty(uacOffice.getId())) {
            return ResultGenerator.genFailResult(ResultCode.MISSING_PARAMETERS, "OFFICE ID 不能为空");
        }
        UacOffice parentOffice = uacOfficeService.getById(uacOffice.getParentId());
        if (GyToolUtil.isNull(parentOffice)) {
            return ResultGenerator.genFailResult(ResultCode.RECORD_NOT_EXIST, "父级机构不存在");
        }
        boolean duplicateOffice = uacOfficeService.checkDuplicateOffice(uacOffice);
        if (duplicateOffice) {
            return ResultGenerator.genFailResult(ResultCode.DUPLICATE_KEY, "记录重复(名称和父级机构)");
        }
        String deputyPersonId = uacOffice.getDeputyPersonId();
        String primaryPersonId = uacOffice.getPrimaryPersonId();
        if (StringUtils.isNotEmpty(deputyPersonId)) {
            boolean deputyPersonExist = uacUserService.checkUserExist(deputyPersonId);
            if (!deputyPersonExist) {
                return ResultGenerator.genFailResult(ResultCode.RECORD_NOT_EXIST, "副负责人用户不存在！");
            }
        }
        if (StringUtils.isNotEmpty(primaryPersonId)) {
            boolean primaryPersonExist = uacUserService.checkUserExist(primaryPersonId);
            if (!primaryPersonExist) {
                return ResultGenerator.genFailResult(ResultCode.RECORD_NOT_EXIST, "主负责人用户不存在！");
            }
        }
        boolean b = uacOfficeService.updateOffice(uacOffice);
        return b ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("UacOffice表update失败");
    }

    //@PreAuthorize("@pms.hasPermission('uac:office:view')")
    @ApiOperation(value = "通过id获取部门信息")
    @GetMapping("/{id}")
    public Result get(@PathVariable String id) {
        UacOfficeDto uacOffice = uacOfficeService.getOfficeWithRole(id);
        if (GyToolUtil.isNull(uacOffice)) {
            return ResultGenerator.genFailResult(ResultCode.RECORD_NOT_EXIST);
        }
        return ResultGenerator.genSuccessResult(uacOffice);
    }

    @ApiOperation(value = "分页查询所有部门的接口", notes = "分页查询所有部门的接口")
    @GetMapping
    public Result list(@Valid PageRequest pageRequest, UacOffice uacOffice) {
        log.info("UacOfficeController list {}", uacOffice);
        // 查询office数据权限过滤
        uacOffice.getSqlMap().put("dsSql", BaseService.dataScopeFilter(getUserInfo(), "t", "uu,pp,dp"));
        return ResultGenerator.genSuccessResult(uacOfficeService.page(pageRequest, uacOffice));
    }

    // @PreAuthorize("@pms.hasPermission('uac:user:view')")
    @ApiOperation(value = "通过角色id查看所有机构")
    @GetMapping("/role/{roleId}")
    public Result getUserByRoleId(@PathVariable String roleId, PageRequest pageRequest, UacOffice uacOffice) {
        if (StringUtils.isEmpty(roleId)) {
            return ResultGenerator.genFailResult(ResultCode.MISSING_PARAMETERS);
        }
        // 查询office数据权限过滤
        uacOffice.getSqlMap().put("dsSql", BaseService.dataScopeFilter(getUserInfo(), "", "uu,pp,dp"));
        PageResult officeByRoleId = uacOfficeService.getOfficeByRoleId(roleId, uacOffice, pageRequest);
        return ResultGenerator.genSuccessResult(officeByRoleId);
    }

    //@PreAuthorize("@pms.hasPermission('uac:office:view')")
    @ApiOperation(value = "查询所有部门的接口")
    @GetMapping("/all")
    public Result listAll() {
        log.info("UacOfficeController listAll");
        return ResultGenerator.genSuccessResult(uacOfficeService.listAllOffice());
    }

    //@PreAuthorize("@pms.hasPermission('uac:menu:view')")
    @ApiOperation(value = "查询树形的机构接口（可传roleId，返回所有机构，标记该角色对应的机构菜单", notes = "查询树形的机构接口")
    @GetMapping("/tree")
    public Result treeData(@RequestParam(value = "roleId", required = false) String roleId) {
        log.info("UacOfficeController treeData roleId-{}", roleId);
        if (StringUtils.isEmpty(roleId)) {
            List<Object> objects = uacOfficeService.officeTree();
            return ResultGenerator.genSuccessResult(objects);
        } else {
            List<Object> objects = uacOfficeService.officeTreeByRoleId(roleId);
            return ResultGenerator.genSuccessResult(objects);
        }

    }
    
    //@PreAuthorize("@pms.hasPermission('uac:office:edit')")
    @ApiOperation(value = "通过id删除部门的接口")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id) {
        boolean hasChild = uacOfficeService.hasChild(id);
        if (hasChild) {
            return ResultGenerator.genFailResult(ResultCode.HAVE_CHILD_CANT_DELETE);
        }
        int i = uacOfficeService.deleteOfficeById(id);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("deleteById失败");
    }

    //@PreAuthorize("@pms.hasPermission('uac:office:edit')")
    @ApiOperation(value = "通过id批量删除部门的接口")
    @DeleteMapping
    public Result deleteByIds(@NotBlank String idsStr) {
        // 判断是否有子节点
        String[] idArr = idsStr.split(",");
        boolean hasChild = uacOfficeService.hasChild(idArr);
        if (hasChild) {
            return ResultGenerator.genFailResult(ResultCode.HAVE_CHILD_CANT_DELETE);
        }
        int i = uacOfficeService.deleteOfficeByIds(idsStr);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("deleteList失败");
    }
}
