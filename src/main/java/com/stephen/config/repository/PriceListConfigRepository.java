package com.stephen.config.repository;

import com.stephen.config.entity.PriceListConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author IvanStephen
 * @project calculator
 * @date 2019/10/24 9:11
 **/
@Repository
public interface PriceListConfigRepository extends JpaRepository<PriceListConfig, Long> {
    /**
     * 通过产品名获取配置明细
     * @param priceList
     * @param enabledFlag
     * @return
     */
    public List<PriceListConfig> findByPriceListAndEnabledFlag(String priceList, String enabledFlag);

    /**
     * 通过产品名获取明细配置
     * @param priceList
     * @return
     */
    public List<PriceListConfig> findByPriceList(String priceList);
}
