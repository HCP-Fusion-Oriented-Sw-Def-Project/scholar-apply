package com.example.gbdpbootcore.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import java.util.HashMap;
import java.util.Map;

/**
 * 分页请求封装
 * @author kongweichang
 */
@Data
@ApiModel("参数请求bean")
public class PageRequest {

    @ApiModelProperty(value = "页数", example = "1")
    private int pageNum = 1;
    @ApiModelProperty(value = "每页个数", example = "10")
    @Max(value = 100, message = "单页记录最多100条")
    private int pageSize = 10;
    @ApiModelProperty(value = "排序字段", example = "create_date")
    private String orderBy = "create_date";
    /**
     * 排序规律
     */
    @ApiModelProperty(value = "排序规律", example = "desc")
    private String orderRule = "desc";
}