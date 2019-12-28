package com.stephen.calculator.core;

import com.stephen.calculator.entity.CalculatorHeader;
import com.stephen.calculator.entity.CalculatorLine;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author IvanStephen
 * @project calculator
 * @date 2019/10/28 13:03
 **/
@Data
@ApiModel(description = "计算响应对象")
public class ResponseData implements Serializable {
    private static final long serialVersionUID = -7358824864870847815L;

    @ApiModelProperty(value = "响应编码")
    private String responseCode;
    @ApiModelProperty(value = "响应消息")
    private String responseMessage;
    @ApiModelProperty(value = "报价头信息")
    private CalculatorHeader header;
    @ApiModelProperty(value = "报价行信息")
    private List<CalculatorLine> lines;
}
