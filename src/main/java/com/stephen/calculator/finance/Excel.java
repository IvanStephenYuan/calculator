package com.stephen.calculator.finance;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author IvanStephen
 * @project calculator
 * @date 2019/11/3 19:31
 **/
public class Excel {
    /**
     * pmt公式
     *
     * @param rate 每期利率
     * @param nper 贷款期数
     * @param pv   贷款金额
     * @return
     */
    public static double pmt(double rate, int nper, double pv) {
        MathContext round = new MathContext(2);
        double monthRate = rate;
        double powRate = Math.pow(1 + monthRate, nper);
        BigDecimal monthIncome = BigDecimal.valueOf(0);

        if (monthRate != 0) {
            monthIncome = BigDecimal.valueOf(pv)
                    .multiply(BigDecimal.valueOf(monthRate * powRate))
                    .divide(BigDecimal.valueOf(powRate - 1), 6, BigDecimal.ROUND_HALF_UP);
            monthIncome = monthIncome.setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            monthIncome = BigDecimal.valueOf(pv).divide(BigDecimal.valueOf(nper));
            monthIncome = monthIncome.setScale(2, BigDecimal.ROUND_HALF_UP);
        }

        return monthIncome.doubleValue();
    }

    /**
     * ipmt公式
     *
     * @param rate 每期利率
     * @param per  当期期数
     * @param nper 贷款期数
     * @param pv   贷款金额
     * @return
     */
    public static double ipmt(double rate, int per, int nper, double pv) {
        double monthRate = rate;
        double perRate = Math.pow(1 + monthRate, per - 1);
        double nperRate = Math.pow(1 + monthRate, nper);
        BigDecimal monthInterest = BigDecimal.valueOf(0);

        if (monthRate != 0) {
            BigDecimal multiply = BigDecimal.valueOf(pv).multiply(BigDecimal.valueOf(monthRate));
            BigDecimal sub = BigDecimal.valueOf(nperRate).subtract(BigDecimal.valueOf(perRate));
            monthInterest = multiply.multiply(sub).divide(BigDecimal.valueOf(nperRate - 1), 6, BigDecimal.ROUND_HALF_UP);
            monthInterest = monthInterest.setScale(2, BigDecimal.ROUND_HALF_UP);
        }

        return monthInterest.doubleValue();
    }

    public static void main(String args[]) {
        double rental = Excel.pmt(0, 6, 50000000);
        System.out.println("月供为：" + rental);

        double interest = Excel.ipmt(0, 3, 6, 50000000);
        System.out.println("利息为：" + interest);

        int i = 3;
        System.out.println("余值：" + i % 0);
    }
}
