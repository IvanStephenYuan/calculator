package com.stephen.calculator.repository;

import com.stephen.calculator.entity.CalculatorHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author IvanStephen
 * @project calculator
 * @date 2019/10/24 17:01
 **/
@Repository
public interface CalculatorHeaderRepository extends JpaRepository<CalculatorHeader, Long> {

}
