package com.stephen.calculator.controller;

import com.stephen.calculator.core.RequestData;
import com.stephen.calculator.entity.CalculatorHeader;
import com.stephen.calculator.entity.CalculatorLine;
import com.stephen.calculator.core.ResponseData;
import com.stephen.calculator.service.CalculatorHeaderService;
import com.stephen.calculator.service.CalculatorLineService;
import com.stephen.config.entity.PriceList;
import com.stephen.config.service.IPriceListService;
import com.stephen.util.CommonConstUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author IvanStephen
 * @project calculator
 * @date 2019/10/28 10:34
 **/
@RestController
@RequestMapping(value = "/calculator")
@Api(description = "报价计算接口")
public class CalculatorHeaderController {
    @Autowired
    private CalculatorHeaderService calculatorHeaderService;

    @Autowired
    private CalculatorLineService calculatorLineService;

    @Autowired
    private IPriceListService priceListService;

    @PostMapping(value = "/compute")
    @ApiOperation(value = "计算", notes = "自动保存后计算")
    public ResponseData computeProduct(@RequestBody RequestData request){
        CalculatorHeader calculatorHeader = request.getHeader();
        ResponseData responseData = new ResponseData();
        PriceList priceList = null;

        //基础校验
        if("".equalsIgnoreCase(calculatorHeader.getPriceList())){
            responseData.setResponseCode(CommonConstUtil.RESPONSE_ERROR);
            responseData.setResponseMessage("请求错误，产品不能为空");
            return responseData;
        }else{
            priceList = priceListService.getPriceList(calculatorHeader.getPriceList());
            if(CommonConstUtil.NO.equalsIgnoreCase(priceList.getEnabledFlag())){
                responseData.setResponseCode(CommonConstUtil.RESPONSE_ERROR);
                responseData.setResponseMessage("请求错误，产品被禁用");
                return responseData;
            }
        }

        //调用核心报价
        List<CalculatorLine> lists = calculatorHeaderService.compute(priceList, calculatorHeader);
        responseData.setResponseCode(CommonConstUtil.RESPONSE_SUCCESS);
        responseData.setResponseMessage("请求成功");
        responseData.setHeader(calculatorHeader);
        responseData.setLines(lists);

        return responseData;
    }

    @PostMapping(value = "/recompute")
    @ApiOperation(value = "重算", notes = "根据行表重选")
    public ResponseData recomputeProduct(@RequestBody RequestData request){
        CalculatorHeader header = request.getHeader();
        List<CalculatorLine> lines = request.getLines();

        //判断必输字段
        Long headerId = lines.get(0).getHeaderId();
        ResponseData responseData = new ResponseData();
        PriceList priceList = null;

        //基础校验
        if("".equalsIgnoreCase(header.getPriceList())){
            responseData.setResponseCode(CommonConstUtil.RESPONSE_ERROR);
            responseData.setResponseMessage("请求错误，产品不能为空");
            return responseData;
        }else{
            priceList = priceListService.getPriceList(header.getPriceList());
            if(CommonConstUtil.NO.equalsIgnoreCase(priceList.getEnabledFlag())){
                responseData.setResponseCode(CommonConstUtil.RESPONSE_ERROR);
                responseData.setResponseMessage("请求错误，产品被禁用");
                return responseData;
            }
        }

        for(CalculatorLine line : lines){
            if(line.getHeaderId() != headerId){
                responseData.setResponseCode(CommonConstUtil.RESPONSE_ERROR);
                responseData.setResponseMessage("请求参数错误，头行关系不一致");
                return responseData;
            }
        }

        calculatorHeaderService.recompute(priceList, header, lines);
        responseData.setResponseCode(CommonConstUtil.RESPONSE_SUCCESS);
        responseData.setResponseMessage("请求成功");
        responseData.setHeader(header);
        responseData.setLines(lines);

        return responseData;
    }
}
