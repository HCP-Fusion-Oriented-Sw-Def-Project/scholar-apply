package com.example.gbdpuserac.controller;

import com.example.gbdpbootcore.httpResult.Result;
import com.example.gbdpbootcore.httpResult.ResultGenerator;
import com.example.gbdpbootcore.page.PageRequest;
import com.example.gbdpuserac.core.BaseController;
import com.example.gbdpuserac.dto.AverageProcessTimeDto;
import com.example.gbdpuserac.dto.ProportionDto;
import com.example.gbdpuserac.dto.StaticByTimeDto;
import com.example.gbdpuserac.entity.EventMessage;
import com.example.gbdpuserac.model.vo.StaticDeptVO;
import com.example.gbdpuserac.service.CoordinationEventDeptService;
import com.example.gbdpuserac.service.EventCoordinationRecordService;
import com.example.gbdpuserac.service.EventMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "StaticController",description = "统计controller")
@RestController
@RequestMapping("static")
public class StaticController extends BaseController {

    @Resource
    private EventMessageService eventMessageService;


    @Resource
    private EventCoordinationRecordService eventCoordinationRecordService;

    @Resource
    private CoordinationEventDeptService coordinationEventDeptService;


    @ApiOperation("风险信息提交总信息")
    @GetMapping("/riskAllInfo")
    public Result riskAllInfo(){
        Map<String, Object> result = new HashMap<>();
        Integer count1 = this.eventMessageService.count(new EventMessage());
        Integer count2 = this.eventMessageService.count(new EventMessage().setStatus(0));
        result.put("风险总数",count1-count2);

        Integer count3 = this.eventMessageService.count(new EventMessage().setStatus(1));
        result.put("已确认数",count1-count2-count3);

        result.put("待确认数",count3);

        Integer count4 = this.eventMessageService.count(new EventMessage().setStatus(-2));
        result.put("无需处理数",count4);

        Integer count5 = this.eventMessageService.count(new EventMessage().setStatus(2));
        Integer count6 = this.eventMessageService.count(new EventMessage().setStatus(3));
        Integer count7 = this.eventMessageService.count(new EventMessage().setStatus(-1));
        result.put("需要协同数",count5+count6+count7);

        result.put("正在协同数",count5+count6);
        result.put("协同完成数",count7);

        AverageProcessTimeDto timeDto = this.eventMessageService.averageProcessTime();
        Double resultPro = 0.0;
        if(timeDto.getTotalNum()>0)
            resultPro = timeDto.getTotalHour()/timeDto.getTotalNum();
        result.put("平均处理时间",resultPro);
        return ResultGenerator.genSuccessResult(result);
    }


    @ApiOperation("风险信息提交总数")
    @GetMapping("/riskTotal")
    public Result riskTotal(){
        Integer count1 = this.eventMessageService.count(new EventMessage());
        Integer count2 = this.eventMessageService.count(new EventMessage().setStatus(0));
        return ResultGenerator.genSuccessResult(count1-count2);
    }

    @ApiOperation("已经确认数目")
    @GetMapping("/confirmed")
    public Result hasConfirm(){
        Integer count1 = this.eventMessageService.count(new EventMessage());
        Integer count2 = this.eventMessageService.count(new EventMessage().setStatus(0));
        Integer count3 = this.eventMessageService.count(new EventMessage().setStatus(1));
        return ResultGenerator.genSuccessResult(count1-count2-count3);
    }


    @ApiOperation("无需处理信息数量")
    @GetMapping("/noRequired")
    public Result noRequired(){
        Integer count = this.eventMessageService.count(new EventMessage().setStatus(-2));
        return ResultGenerator.genSuccessResult(count);
    }


    @ApiOperation("需要协同处理信息总数量")
    @GetMapping("/requiredCoordinationTotal")
    public Result requiredCoordinationTotal(){
        Integer count1 = this.eventMessageService.count(new EventMessage().setStatus(2));
        Integer count2 = this.eventMessageService.count(new EventMessage().setStatus(3));
        Integer count3 = this.eventMessageService.count(new EventMessage().setStatus(-1));
        return ResultGenerator.genSuccessResult(count1+count2+count3);
    }


    @ApiOperation("正在协同数目")
    @GetMapping("/coordinating")
    public Result coordinating(){
        Integer count1 = this.eventMessageService.count(new EventMessage().setStatus(2));
        Integer count2 = this.eventMessageService.count(new EventMessage().setStatus(3));
        return ResultGenerator.genSuccessResult(count1+count2);
    }

    @ApiOperation("协同完成数据")
    @GetMapping("/coordinated")
    public Result coordinated(){
        Integer count3 = this.eventMessageService.count(new EventMessage().setStatus(-1));
        return ResultGenerator.genSuccessResult(count3);
    }

    @ApiOperation("平均处理时间")
    @GetMapping("/averageProcessTime")
    public Result averageProcessTime(){
        AverageProcessTimeDto timeDto = this.eventMessageService.averageProcessTime();
        Double result = 0.0;
        if(timeDto.getTotalNum()>0)
            result = timeDto.getTotalHour()/timeDto.getTotalNum();
        return ResultGenerator.genSuccessResult(result);
    }

    @ApiOperation("【总情况】1小时内，风险变化情况")
    @GetMapping("/oneHourSubmit")
    public Result oneHourSubmit(){
        List<StaticByTimeDto> hours = this.eventMessageService.staticByHour();
        return ResultGenerator.genSuccessResult(StaticByTimeDto.calHourList(hours));
    }

    @ApiOperation("【总情况】一天内，风险变化情况")
    @GetMapping("/oneDaySubmit")
    public Result oneDaySubmit(){
        List<StaticByTimeDto> hours = this.eventMessageService.staticByDay();
        return ResultGenerator.genSuccessResult(StaticByTimeDto.calDayList(hours));
    }

    @ApiOperation("【总情况】所有时间内，风险变化情况")
    @GetMapping("/allTimeSubmit")
    public Result allTimeSubmit() throws ParseException {
        List<StaticByTimeDto> hours = this.eventMessageService.staticAll();
        if(hours==null||hours.size()==0){
            return ResultGenerator.genSuccessResult(StaticByTimeDto.calAllList(hours,null));
        }
        return ResultGenerator.genSuccessResult(StaticByTimeDto.calAllList(hours,hours.get(0).getTime()));
    }

    @ApiOperation("dept")
    @GetMapping("/mostSubmitDept")
    public Result mostSubmitDept(){
        List<StaticDeptVO> result = this.eventMessageService.staticDept();
        return ResultGenerator.genSuccessResult(result);
    }

    @ApiOperation("信息泄露风险占比")
    @GetMapping("/disclosureRiskProportion")
    public Result disclosureRiskProportion(){
        int count = eventMessageService.count(new EventMessage());
        List<ProportionDto> dtos = eventMessageService.calProportion("disclosure_risk");
        dtos.forEach(tmp ->{
            tmp.setPercent(tmp.getNum()/((float)count));
        });
        return ResultGenerator.genSuccessResult(dtos);
    }

    @ApiOperation("数据完整性风险")
    @GetMapping("/dataIntegrityRiskProportion")
    public Result dataIntegrityRiskProportion(){
        int count = eventMessageService.count(new EventMessage());
        List<ProportionDto> dtos = eventMessageService.calProportion("data_integrity_risk");
        dtos.forEach(tmp ->{
            tmp.setPercent(tmp.getNum()/((float)count));
        });
        return ResultGenerator.genSuccessResult(dtos);
    }

    @ApiOperation("系统可用性风险")
    @GetMapping("/systemAvailabilityRiskProportion")
    public Result systemAvailabilityRiskProportion(){
        int count = eventMessageService.count(new EventMessage());
        List<ProportionDto> dtos = eventMessageService.calProportion("system_availability_risk");
        dtos.forEach(tmp ->{
            tmp.setPercent(tmp.getNum()/((float)count));
        });
        return ResultGenerator.genSuccessResult(dtos);
    }

    @ApiOperation("影响范围")
    @GetMapping("/influenceScopeProportion")
    public Result influenceScopeProportion(){
        int count = eventMessageService.count(new EventMessage());
        List<ProportionDto> dtos = eventMessageService.calProportion("influence_scope");
        dtos.forEach(tmp ->{
            tmp.setPercent(tmp.getNum()/((float)count));
        });
        return ResultGenerator.genSuccessResult(dtos);
    }

    @ApiOperation("修复难度")
    @GetMapping("/repairDifficultyProportion")
    public Result repairDifficultyProportion(){
        int count = eventMessageService.count(new EventMessage());
        List<ProportionDto> dtos = eventMessageService.calProportion("repair_difficulty");
        dtos.forEach(tmp ->{
            tmp.setPercent(tmp.getNum()/((float)count));
        });
        return ResultGenerator.genSuccessResult(dtos);
    }

    @ApiOperation("安全级别")
    @GetMapping("/securityLevelProportion")
    public Result securityLevelProportion(){
        int count = eventMessageService.count(new EventMessage());
        List<ProportionDto> dtos = eventMessageService.calProportion("security_level");
        dtos.forEach(tmp ->{
            tmp.setPercent(tmp.getNum()/((float)count));
        });
        return ResultGenerator.genSuccessResult(dtos);
    }

//    @ApiOperation("事件前10条")
//    @GetMapping("/events")
//    public Result listAll(PageRequest pageRequest, EventMessage eventMessage) {
//
//        return ResultGenerator.genSuccessResult(eventMessageService.page(pageRequest, eventMessage));
//    }
}
