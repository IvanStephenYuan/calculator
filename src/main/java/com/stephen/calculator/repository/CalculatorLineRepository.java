package com.stephen.calculator.repository;

import com.stephen.calculator.entity.CalculatorLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author IvanStephen
 * @project calculator
 * @date 2019/10/28 9:49
 **/
@Repository
public interface CalculatorLineRepository extends JpaRepository<CalculatorLine, Long> {
    /**
     * 根据头表ID获取报价行信息
     * @param headerId
     * @return
     */
    public List<CalculatorLine> findAllByHeaderId(Long headerId);

    /**
     * 根据头表ID删除报价行信息
     * @param headerId
     */
    public void deleteAllByHeaderId(Long headerId);
}
