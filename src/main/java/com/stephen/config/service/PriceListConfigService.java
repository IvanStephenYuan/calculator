package com.stephen.config.service;

import com.stephen.config.entity.PriceListConfig;
import com.stephen.config.repository.PriceListConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author IvanStephen
 * @project calculator
 * @date 2019/10/25 14:05
 **/
@Service
public class PriceListConfigService {
    @Autowired
    private PriceListConfigRepository priceListConfigRepository;

    /**
     * 批量更新
     * @param listConfigs
     * @return
     */
    public List<PriceListConfig> batchUpdate(List<PriceListConfig> listConfigs){
        return priceListConfigRepository.saveAll(listConfigs);
    }

    /**
     * 获取明细配置
     * @param priceList
     * @return
     */
    public List<PriceListConfig> getPriceListConfigs(String priceList){
        return priceListConfigRepository.findByPriceList(priceList);
    }
}
