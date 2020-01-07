package com.stephen.config.service.impl;

import com.stephen.config.entity.PriceList;
import com.stephen.config.repository.PriceListRepository;
import com.stephen.config.service.IPriceListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author IvanStephen
 * @project calculator
 * @date 2019/10/24 10:06
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class PriceListServiceImpl implements IPriceListService {
    @Autowired
    private PriceListRepository priceListRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 更新操作
     *
     * @param priceList
     * @return
     */
    @Override
    public PriceList batchUpdate(PriceList priceList) {
        if (!"".equalsIgnoreCase(priceList.getPriceList())) {
            return priceListRepository.save(priceList);
        } else {
            return null;
        }
    }

    /**
     * 通过产品名获取有效产品
     *
     * @param priceList
     * @return
     */
    @Override
    public List<PriceList> getPriceList(String priceList, String enabledFlag) {
        List<PriceList> priceLists = priceListRepository.findByPriceListAndEnabledFlag(priceList, enabledFlag);
        return priceLists;
    }

    /**
     * 获取所有产品
     *
     * @return
     */
    @Override
    public List<PriceList> getPriceList() {
        List<PriceList> priceLists = priceListRepository.findAll();
        return priceLists;
    }

    /**
     * 通过产品名获取产品
     *
     * @param priceList
     * @return
     */
    @Override
    //@Cacheable(value = "priceList", key = "#priceList", condition = "#priceList.contentEquals(\"TEST\")")
    public PriceList getPriceList(String priceList) {
        /*ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("keyOb", String.valueOf(Math.random()), 60);
        String value = valueOperations.get("keyOb");
        System.out.println("keyOb = " + value);*/

        //System.out.println("从数据库获取：" + priceList);
        return priceListRepository.getOne(priceList);
    }

    /**
     * 删除产品
     *
     * @param priceList
     */
    @Override
    //@CacheEvict(value = "priceList")
    public void deletePriceList(String priceList) {
        System.out.println("从数据库删除：" + priceList);
        priceListRepository.deleteById(priceList);
    }
}
