package com.stephen.calculator.service;

import com.stephen.calculator.entity.CalculatorHeader;
import com.stephen.calculator.entity.CalculatorLine;
import com.stephen.calculator.repository.CalculatorHeaderRepository;
import com.stephen.calculator.repository.CalculatorLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author IvanStephen
 * @project calculator
 * @date 2019/10/28 10:22
 **/
@Service
public class CalculatorLineService {
    @Autowired
    private CalculatorHeaderRepository calculatorHeaderRepository;

    @Autowired
    private CalculatorLineRepository calculatorLineRepository;

    /**
     * 获取报价行信息
     *
     * @param headerId
     * @return
     */
    public List<CalculatorLine> getCalculatorLines(Long headerId) {
        CalculatorHeader header = calculatorHeaderRepository.getOne(headerId);
        if (header != null) {
            return calculatorLineRepository.findAllByHeaderId(headerId);
        } else {
            return null;
        }
    }
}
