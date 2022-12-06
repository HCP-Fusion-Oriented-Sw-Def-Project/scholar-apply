package com.example.gbdpuserac.controller;


import com.example.gbdpuserac.dto.UacUserDto;
import com.example.gbdpuserac.entity.CoordinationEventDept;
import com.example.gbdpuserac.entity.EventCoordinationRecord;
import com.example.gbdpuserac.entity.EventMessage;
import com.example.gbdpuserac.entity.RequestEventDept;
import com.example.gbdpuserac.model.UacOffice;
import com.example.gbdpuserac.model.vo.CoordinationVO;
import com.example.gbdpuserac.model.vo.DeptVO;
import com.example.gbdpuserac.service.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import com.example.gbdpbootcore.httpResult.Result;
import com.example.gbdpbootcore.httpResult.ResultGenerator;
import com.example.gbdpuserac.core.BaseController;
import com.example.gbdpbootcore.page.PageRequest;
import com.example.gbdpbootcore.page.PageResult;
import com.example.gbdpbootcore.validation.CreateGroup;
import com.example.gbdpbootcore.validation.UpdateGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * (EventMessage)表控制层
 *
 * @author makejava
 * @since 2021-09-06 18:50:20
 */
@Api(value = "EventMessageController",description = "事件controller")
@RestController
@RequestMapping("eventMessage")
public class EventMessageController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private EventMessageService eventMessageService;

    @Resource
    private UacOfficeService uacOfficeService;

    @Resource
    private EventCoordinationRecordService eventCoordinationRecordService;

    @Resource
    private CoordinationEventDeptService coordinationEventDeptService;

    @ApiOperation("添加接口")
    @PostMapping
    public Result save(@RequestBody @Validated(CreateGroup.class) EventMessage eventMessage) {
        eventMessage.setStatus(0);
        //todo 验证office是否存在
        int i = eventMessageService.save(eventMessage);
        if(i>0){
            EventMessage tmp = this.eventMessageService.getById(eventMessage.getId());
            tmp.genNo();
            this.eventMessageService.update(tmp);
//            if(eventMessage.getRequestDeptVOS()!=null&&eventMessage.getRequestDeptVOS().size()>0){
//                for(DeptVO vo:eventMessage.getRequestDeptVOS()){
//                    RequestEventDept requestEventDept = new RequestEventDept(eventMessage.getId(),vo.getId());
//                    requestEventDeptService.save(requestEventDept);
//                }
//            }
        }
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("insert失败");
    }

    @ApiOperation("根据id删除接口")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id) {
        EventMessage tmp = this.eventMessageService.getById(id);
        if(tmp==null||tmp.getStatus()!=0){
            return ResultGenerator.genFailResult("该记录无法删除");
        }
        int i = eventMessageService.deleteById(id);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("deleteById失败");
    }

    @ApiOperation("批量删除接口")
    @DeleteMapping
    public Result deleteByIds(@RequestBody String idsStr) {
        String[] ids = idsStr.split(",");
        for(String id:ids){
            EventMessage tmp = this.eventMessageService.getById(id);
            if(tmp!=null&&tmp.getStatus()==0){
                eventMessageService.deleteById(id);
            }
        }
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation("修改接口")
    @PutMapping
    public Result update(@RequestBody @Validated(UpdateGroup.class) EventMessage eventMessage) {
        EventMessage old = eventMessageService.getById(eventMessage.getId());
        if(old==null||old.getStatus()!=0)
            return ResultGenerator.genFailResult("该事件不支持修改");
        int i = eventMessageService.saveOrUpdate(eventMessage);
        if(i>0){
//            requestEventDeptService.deleteByEventId(eventMessage.getId());
//            if(eventMessage.getRequestDeptVOS()!=null&&eventMessage.getRequestDeptVOS().size()>0){
//                for(DeptVO vo:eventMessage.getRequestDeptVOS()){
//                    RequestEventDept requestEventDept = new RequestEventDept(eventMessage.getId(),vo.getId());
//                    requestEventDeptService.save(requestEventDept);
//                }
//            }
        }
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("update失败");
    }

    @ApiOperation("根据id查询信息接口")
    @GetMapping("/{id}")
    public Result get(@PathVariable String id) {
        EventMessage eventMessage = eventMessageService.getById(id);
//        List<RequestEventDept> list = requestEventDeptService.list(new RequestEventDept().setEventId(id));
//        if(list!=null){
//            eventMessage.setRequestDeptVOS(new ArrayList<>());
//            for(RequestEventDept requestEventDept:list){
//                UacOffice office = uacOfficeService.getById(requestEventDept.getDeptId());
//                if(office!=null){
//                    eventMessage.getRequestDeptVOS().add(
//                            new DeptVO(office.getId(),office.getName())
//                    );
//                }
//            }
//        }
        return ResultGenerator.genSuccessResult(eventMessage);
    }

    @ApiOperation("列表查询接口")
    @GetMapping
    public Result list(PageRequest pageRequest, EventMessage eventMessage) {
        return ResultGenerator.genSuccessResult(eventMessageService.page(pageRequest, eventMessage));
    }

    @ApiOperation("我的事件列表查询接口")
    @GetMapping("/my")
    public Result listMy(PageRequest pageRequest, EventMessage eventMessage) {
        UacUserDto userDto = getUserInfo();
        List<UacOffice> uacOffices = userDto.getOfficeList();
        if(uacOffices==null||uacOffices.size()==0)
            return ResultGenerator.genFailResult("用户机构信息有误");
        eventMessage.setDeptId(uacOffices.get(0).getId());
        PageResult<EventMessage> result = eventMessageService.pageMy(pageRequest, eventMessage);
        result.getList().stream().forEach(tmp ->{
            if(tmp.getStatus()==3){
                List<EventCoordinationRecord> count = this.eventCoordinationRecordService.list(new EventCoordinationRecord().setEventId(tmp.getId()).setDeptId(uacOffices.get(0).getId()));
                if(count.size()>0){
                    for(EventCoordinationRecord c:count){
                        if(c.getProcessStatus()==1)
                            tmp.setStatus(-1);
                    }
                }else{
                    tmp.setStatus(2);
                }
            }
        });
        return ResultGenerator.genSuccessResult(result);
    }

    @ApiOperation("全部事件列表查询接口")
    @GetMapping("/all")
    public Result listAll(PageRequest pageRequest, EventMessage eventMessage) {
        return ResultGenerator.genSuccessResult(eventMessageService.page(pageRequest, eventMessage));
    }

    @ApiOperation("提交")
    @PutMapping("/submit/{id}")
    public Result submit(@PathVariable String id) {
        EventMessage eventMessage = eventMessageService.getById(id);
        if(eventMessage.getStatus()!=0)
            return ResultGenerator.genFailResult("该事件不支持提交");
        eventMessage.setStatus(1);
        eventMessage.setSubmitTime(new Date());
        eventMessageService.saveOrUpdate(eventMessage);
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation("协同按钮")
    @PostMapping("/coordination")
    public Result coordination(@RequestBody @Validated(CreateGroup.class)EventCoordinationRecord eventCoordinationRecord) {
        UacUserDto userDto = getUserInfo();
        List<UacOffice> uacOffices = userDto.getOfficeList();
        if(uacOffices==null||uacOffices.size()==0)
            return ResultGenerator.genFailResult("用户机构信息有误");

        List<CoordinationEventDept> coordinationEventDepts = this.coordinationEventDeptService.list(
                new CoordinationEventDept().setEventId(eventCoordinationRecord.getEventId()).setDeptId(uacOffices.get(0).getId())
        );
        if(coordinationEventDepts == null || coordinationEventDepts.size() != 1)
            return ResultGenerator.genFailResult("协同信息有误");
        eventCoordinationRecord.setDeptId(coordinationEventDepts.get(0).getDeptId());
        int i = eventCoordinationRecordService.saveOrUpdate(eventCoordinationRecord);
        if(i>0){
            if(eventCoordinationRecord.getProcessStatus()==0){
                this.coordinationEventDeptService.update(
                        coordinationEventDepts.get(0).setStatus(0)
                );
                EventMessage event = eventMessageService.getById(eventCoordinationRecord.getEventId());
                event.setStatus(3);
                this.eventMessageService.saveOrUpdate(event);
            }else{
                this.coordinationEventDeptService.update(
                        coordinationEventDepts.get(0).setStatus(1)
                );
                List<CoordinationEventDept> all = this.coordinationEventDeptService.list(new CoordinationEventDept().setEventId(eventCoordinationRecord.getEventId()));
                boolean processed = true;
                for(CoordinationEventDept tmp:all){
                    if(tmp.getStatus()==0){
                        processed = false;
                        break;
                    }
                }
                if(processed){
                    EventMessage event = eventMessageService.getById(eventCoordinationRecord.getEventId());
                    event.setStatus(-1);
                    this.eventMessageService.saveOrUpdate(event);
                }

            }
        }
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation("全部事件/处理按钮")
    @PostMapping("/coordinationProcess")
    public Result coordinationProcess(@RequestBody CoordinationVO coordinationVO) {
        EventMessage event = eventMessageService.getById(coordinationVO.getEventId());
        if(coordinationVO.getProcessType()!=1){
            return ResultGenerator.genFailResult("该事件无法处理");
        }
        if(coordinationVO.getProcessType()==0){
            event.setStatus(-2);
            this.eventMessageService.saveOrUpdate(event);
        }else{
            event.setStatus(2);
            this.eventMessageService.saveOrUpdate(event);
            Set<String> deptIds = new HashSet<>(coordinationVO.getDeptIds());
            for(String deptId:deptIds){
                CoordinationEventDept dept = new CoordinationEventDept(
                        coordinationVO.getEventId(),
                        deptId,
                        0
                );
                this.coordinationEventDeptService.saveOrUpdate(dept);
            }
        }
        return ResultGenerator.genSuccessResult();
    }


    @ApiOperation("协同详情")
    @GetMapping("/coordination/{id}")
    public Result coordinationList(PageRequest pageRequest, @PathVariable("id") String eventId) {
        List<CoordinationEventDept> coordinationEventDepts = this.coordinationEventDeptService.list(
                new CoordinationEventDept().setEventId(eventId)
        );
        if(coordinationEventDepts==null||coordinationEventDepts.size()==0)
            return ResultGenerator.genFailResult("该事件未要求协同");
        Date issueDate = coordinationEventDepts.get(0).getCreateDate();
        PageResult<EventCoordinationRecord> result = eventCoordinationRecordService.page(pageRequest,
                new EventCoordinationRecord().setEventId(eventId));
        result.getList().forEach(tmp -> tmp.setIssueTime(issueDate));
        return ResultGenerator.genSuccessResult(result);
    }

    @ApiOperation("协同详情(我的事件）")
    @GetMapping("/coordinationMy/{id}")
    public Result coordinationListMy(PageRequest pageRequest, @PathVariable("id") String eventId) {
        UacUserDto userDto = getUserInfo();
        List<UacOffice> uacOffices = userDto.getOfficeList();
        if(uacOffices==null||uacOffices.size()==0)
            return ResultGenerator.genFailResult("用户机构信息有误");


        List<CoordinationEventDept> coordinationEventDepts = this.coordinationEventDeptService.list(
                new CoordinationEventDept().setEventId(eventId).setDeptId(uacOffices.get(0).getId())
        );
        if(coordinationEventDepts==null||coordinationEventDepts.size()==0)
            return ResultGenerator.genFailResult("该事件未要求协同");
        Date issueDate = coordinationEventDepts.get(0).getCreateDate();
        PageResult<EventCoordinationRecord> result = eventCoordinationRecordService.page(pageRequest,
                new EventCoordinationRecord().setEventId(eventId).setDeptId(uacOffices.get(0).getId()));
        result.getList().forEach(tmp -> tmp.setIssueTime(issueDate));
        return ResultGenerator.genSuccessResult(result);
    }

}

