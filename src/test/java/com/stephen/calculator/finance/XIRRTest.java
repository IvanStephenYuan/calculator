package com.stephen.calculator.finance;

import com.stephen.calculator.entity.CalculatorLine;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @author IvanStephen
 * @project calculator
 * @date 2019/10/31 15:47
 **/
public class XIRRTest {
    @Test
    public void xirr() {
        List<CalculatorLine> list = new ArrayList<CalculatorLine>() {{
            add(new CalculatorLine(LocalDate.parse("2008-01-01"), 0, -10000));
            add(new CalculatorLine(LocalDate.parse("2008-03-01"), 60, 2750));
            add(new CalculatorLine(LocalDate.parse("2008-10-30"), 303, 4250));
            add(new CalculatorLine(LocalDate.parse("2009-02-15"), 411, 3250));
            add(new CalculatorLine(LocalDate.parse("2009-04-01"), 456, 2750));
        }};

        for (CalculatorLine record : list) {
            System.out.println(record.getDueDate() + ", " + record.getCashflowIrr());
        }

        System.out.println(new IRR(list, 0.1D, true).compute());
    }
}