package com.stephen.config.controller;

import com.stephen.config.entity.PriceListConfig;
import com.stephen.config.service.PriceListConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author IvanStephen
 * @project calculator
 * @date 2019/10/25 14:27
 **/
@RestController
@RequestMapping(value = "/priceListConfig")
@Api(description = "配置明细接口")
public class PriceListConfigController {
    @Autowired
    private PriceListConfigService priceListConfigService;

    @PostMapping(value = "/save")
    @ApiOperation(value = "保存", notes = "新建和更新")
    public List<PriceListConfig> savePriceList(@RequestBody List<PriceListConfig> priceListConfigs) {
        return priceListConfigService.batchUpdate(priceListConfigs);
    }

    @GetMapping(value = "/getAll")
    @ApiOperation(value = "通过价目代码获取明细配置信息", notes = "查询")
    public List<PriceListConfig> getPriceList(@RequestParam String priceList) {
        return priceListConfigService.getPriceListConfigs(priceList);
    }
}
