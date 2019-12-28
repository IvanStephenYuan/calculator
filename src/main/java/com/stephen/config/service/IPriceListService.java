package com.stephen.config.service;

import com.stephen.config.entity.PriceList;
import com.stephen.config.repository.PriceListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.Cacheable;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author IvanStephen
 * @project calculator
 * @date 2019/10/24 10:06
 **/
public interface IPriceListService {
    /**
     * 更新操作
     * @param priceList
     * @return
     */
    public PriceList batchUpdate(PriceList priceList);

    /**
     * 通过产品名获取有效产品
     * @param priceList
     * @return
     */
    public List<PriceList> getPriceList(String priceList, String enabledFlag);

    /**
     * 获取所有产品
     * @return
     */
    public List<PriceList> getPriceList();

    /**
     * 通过产品名获取产品
     * @param priceList
     * @return
     */
    public PriceList getPriceList(String priceList);

    /**
     * 删除产品
     * @param priceList
     */
    public void deletePriceList(String priceList);
}
