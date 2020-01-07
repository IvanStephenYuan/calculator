package com.stephen.config.repository;

import com.stephen.config.entity.PriceList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author IvanStephen
 * @project calculator
 * @date 2019/10/23 18:04
 **/
@Repository
public interface PriceListRepository extends JpaRepository<PriceList, String> {
    /**
     * 根据产品名获取产品信息
     *
     * @param priceList
     * @param enabledFlag
     * @return
     */
    public List<PriceList> findByPriceListAndEnabledFlag(String priceList, String enabledFlag);
}
