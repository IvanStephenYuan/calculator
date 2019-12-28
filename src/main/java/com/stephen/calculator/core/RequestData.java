package com.stephen.calculator.core;

import com.stephen.calculator.entity.CalculatorHeader;
import com.stephen.calculator.entity.CalculatorLine;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author IvanStephen
 * @project calculator
 * @date 2019/11/4 9:26
 **/
@Data
@ApiModel(description = "计算请求对象")
public class RequestData {
    @ApiModelProperty(value = "报价头信息")
    private CalculatorHeader header;
    @ApiModelProperty(value = "报价行信息")
    private List<CalculatorLine> lines;
}
