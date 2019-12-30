package com.stephen.calculator.finance;

import com.stephen.calculator.entity.CalculatorLine;
import com.stephen.util.CommonConstUtil;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * @author IvanStephen
 * @project calculator
 * @date 2019/10/31 15:17
 **/
public class IRR {
    private List<CalculatorLine> lines;
    private MathContext round;
    private double guess;
    private boolean extend;
    private int loopNum = 2000;
    private BigDecimal minimum;

    public IRR(List<CalculatorLine> lines) {
        this.lines = lines;
        this.guess = 0.5D;
        this.extend = false;
        this.minimum = BigDecimal.valueOf(1.0E-6D);
        this.round = new MathContext(18);
    }

    public IRR(List<CalculatorLine> lines, double guess) {
        this.lines = lines;
        this.guess = guess;
        this.extend = false;
        this.minimum = BigDecimal.valueOf(1.0E-6D);
        this.round = new MathContext(18);
    }

    public IRR(List<CalculatorLine> lines, boolean extend) {
        this.lines = lines;
        this.guess = 0.5D;
        this.extend = extend;
        this.minimum = BigDecimal.valueOf(1.0E-6D);
        this.round = new MathContext(18);
    }

    public IRR(List<CalculatorLine> lines, double guess, boolean extend) {
        this.lines = lines;
        this.guess = guess;
        this.extend = extend;
        this.minimum = BigDecimal.valueOf(1.0E-6D);
        this.round = new MathContext(18);
    }

    /**
     * 计算NPV
     *
     * @param irr
     * @return
     */
    private BigDecimal npv(BigDecimal irr) {
        BigDecimal npv = new BigDecimal(0);
        if (!lines.isEmpty() && lines.size() > 0) {
            for (int i = 0; i < lines.size(); i++) {
                BigDecimal amount = BigDecimal.valueOf(lines.get(i).getCashflowIrr());
                BigDecimal rate = BigDecimal.valueOf(Math.pow(irr.add(BigDecimal.valueOf(1)).doubleValue(), i));
                BigDecimal income = amount.divide(rate, round);
                npv = npv.add(income);
            }
            return npv;
        } else {
            return BigDecimal.ZERO;
        }
    }

    /**
     * xirr计算npv
     *
     * @param xirr
     * @return
     */
    private BigDecimal xnpv(BigDecimal xirr) {
        BigDecimal npv = BigDecimal.valueOf(0);
        if (!lines.isEmpty() && lines.size() > 0) {
            for (int i = 0; i < lines.size(); i++) {
                BigDecimal amount = BigDecimal.valueOf(lines.get(i).getCashflowIrr());
                BigDecimal divisionDays = BigDecimal.valueOf(lines.get(i).getInterestPeriodDays());
                //根据天数计算年
                double divisionYears = divisionDays.divide(BigDecimal.valueOf(CommonConstUtil.DAYS_IN_YEAR), round).doubleValue();

                BigDecimal rate = BigDecimal.valueOf(Math.pow(xirr.add(BigDecimal.valueOf(1)).doubleValue(), divisionYears));
                BigDecimal income = amount.divide(rate, round);
                npv = npv.add(income);
            }
            return npv;
        } else {
            return BigDecimal.ZERO;
        }
    }

    /**
     * 计算IRR
     */
    public double compute() {
        BigDecimal irr = BigDecimal.valueOf(guess);
        BigDecimal tempAmont = (extend == true) ? xnpv(irr) : npv(irr);
        BigDecimal step = BigDecimal.valueOf(0.1);
        int count = 0, flag = -1;

        while (tempAmont.abs().compareTo(minimum) > 0 && count <= loopNum) {
            if (tempAmont.compareTo(BigDecimal.ZERO) > 0 && flag == -1) {
                step = step.divide(BigDecimal.valueOf(2), round);
                flag = 1;
            } else if (tempAmont.compareTo(BigDecimal.ZERO) < 0 && flag == 1) {
                step = step.divide(BigDecimal.valueOf(2), round);
                flag = -1;
            }

            if (flag == -1) {
                irr = irr.subtract(step);
            } else {
                irr = irr.add(step);
            }

            tempAmont = (extend == true) ? xnpv(irr) : npv(irr);
            count += 1;
        }

        if (tempAmont.abs().compareTo(minimum) < 0 & count <= loopNum) {
            return irr.doubleValue();
        } else {
            return Double.NaN;
        }
    }

    public double computeRealIrr(double financeAmount, double balloon, int leaseTimes) {
        //根据现金流封装实际数组
        List<CalculatorLine> cashflowLists = new ArrayList<CalculatorLine>();
        int time = 0;
        double irr = 0D;

        //处理0期，放款金额只处理融资额
        CalculatorLine zeroLine = new CalculatorLine();
        zeroLine.setTimes(time);
        zeroLine.setDueDate(this.lines.get(0).getDueDate());
        zeroLine.setCashflowIrr(-1 * financeAmount);
        cashflowLists.add(zeroLine);
        time += 1;

        //循环期数，在相邻期数间，插入现金流量为0
        for (int i = 1; i <= leaseTimes; i++) {
            LocalDate currentDate = this.lines.get(i).getDueDate();
            double currentRental = this.lines.get(i).getRental();
            LocalDate lastDate = cashflowLists.get(time - 1).getDueDate();
            LocalDate dueDate = lastDate;

            if(i == 1){
                dueDate = dueDate.plusDays(-1);
            }

            //处理间隔期
            while (dueDate.until(currentDate, ChronoUnit.MONTHS) > 1) {
                CalculatorLine record = new CalculatorLine();
                record.setTimes(time);
                record.setDueDate(dueDate);
                record.setCashflowIrr(0D);
                cashflowLists.add(record);

                time += 1;
                dueDate = dueDate.plusMonths(1);
            }

            //处理当期
            if(dueDate.compareTo(currentDate) <= 0){
                CalculatorLine record = new CalculatorLine();
                record.setTimes(time);
                record.setDueDate(currentDate);
                record.setCashflowIrr(currentRental + balloon);
                cashflowLists.add(record);

                time += 1;
            }
        }

        cashflowLists.forEach(item -> {
            System.out.println("第" + item.getTimes() + "期"+ item.getDueDate() +"，现金流量：" + item.getCashflowIrr());
        });

        IRR realIrr = new IRR(cashflowLists);
        double result = realIrr.compute();
        if(result == Double.NaN){
            result = 0.0D;
        }
        irr = result * 12;

        return irr;
    }
}
