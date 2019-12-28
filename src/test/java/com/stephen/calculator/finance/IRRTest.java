package com.stephen.calculator.finance;

import com.stephen.calculator.entity.CalculatorLine;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author IvanStephen
 * @project calculator
 * @date 2019/10/31 15:45
 **/
public class IRRTest {

    @Test
    public void irr() {
        Double[] arrays = {-700000.0, 120000.0, 150000.0, 180000.0, 210000.0, 260000.0};

        List<CalculatorLine> list = Arrays.asList(arrays).stream().map(x -> {
            CalculatorLine line = new CalculatorLine();
            line.setCashflowIrr(x);
            return line;
        }).collect(Collectors.toList());

        System.out.println(new IRR(list).compute());
    }
}