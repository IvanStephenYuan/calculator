package com.stephen.config.controller;

import com.stephen.config.entity.PriceList;
import com.stephen.config.service.IPriceListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.Cacheable;

import java.util.List;

/**
 * @author IvanStephen
 * @project calculator
 * @date 2019/10/24 11:43
 **/
@RestController
@RequestMapping(value = "/priceList")
@Api(description = "配置接口")
public class PriceListController {
    @Autowired
    IPriceListService priceListService;

    @PostMapping(value = "/save")
    @ApiOperation(value = "保存", notes = "新建和更新")
    public PriceList savePriceList(@RequestBody PriceList priceList) {
        return priceListService.batchUpdate(priceList);
    }

    @GetMapping(value = "/getOne")
    @ApiOperation(value = "通过价目代码获取产品信息", notes = "查询")
    public PriceList getPriceList(@RequestParam String priceList) {
        PriceList value = priceListService.getPriceList(priceList);
        System.out.println("loaded1=" + value.getPriceList() + "," + value.getPriceListName());
        PriceList value2 = priceListService.getPriceList(priceList);
        System.out.println("cached=" + value2.getPriceList() + "," + value2.getPriceListName());

        PriceList value3 = priceListService.getPriceList("NF_SP_TAX_Inc");
        System.out.println("loaded2=" + value3.getPriceList() + "," + value3.getPriceListName());

        return value;
    }

    @GetMapping(value = "/getAll")
    @ApiOperation(value = "获取所有产品信息", notes = "查询")
    public List<PriceList> getPriceListAll() {
        return priceListService.getPriceList();
    }

    @GetMapping(value = "deleteOne")
    @ApiOperation(value = "通过价目代码删除")
    public void deletePriceList(@RequestParam String priceList) {
        priceListService.deletePriceList(priceList);
    }
}
