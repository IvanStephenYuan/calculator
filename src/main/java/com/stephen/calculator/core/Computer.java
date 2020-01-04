package com.stephen.calculator.core;

import com.stephen.calculator.entity.CalculatorHeader;
import com.stephen.calculator.entity.CalculatorLine;
import com.stephen.calculator.finance.Excel;
import com.stephen.calculator.finance.IRR;
import com.stephen.util.CommonConstUtil;
import lombok.Data;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * @author IvanStephen
 * @project calculator
 * @date 2019/10/28 15:37
 **/
@Data
public class Computer {
    private String calcType;
    private int backflowCycle;
    private double financeAmount;
    private double rate;
    private double balloonInterest;
    private CalculatorHeader calculatorHeader;
    private List<CalculatorLine> lines;

    public Computer(String calcType, int backflowCycle, CalculatorHeader calculatorHeader) {
        this.backflowCycle = backflowCycle;
        this.calcType = calcType;
        this.calculatorHeader = calculatorHeader;
        this.lines = new ArrayList<CalculatorLine>(this.calculatorHeader.getLeaseTimes() + 1);

        //设置默认值
        if (this.calculatorHeader.getExchangeRate() == 0) {
            this.calculatorHeader.setExchangeRate(1);
        }

        //初始设置计算成功标志为N
        this.calculatorHeader.setCalcSuccessful(CommonConstUtil.NO);
    }

    public Computer(String calcType, int backflowCycle, CalculatorHeader calculatorHeader, List<CalculatorLine> lines) {
        this.backflowCycle = backflowCycle;
        this.calcType = calcType;
        this.calculatorHeader = calculatorHeader;
        this.lines = lines;

        //设置默认值
        if (this.calculatorHeader.getExchangeRate() == 0) {
            this.calculatorHeader.setExchangeRate(1);
        }

        //初始设置计算成功标志为N
        this.calculatorHeader.setCalcSuccessful(CommonConstUtil.NO);
    }

    /**
     * 按基础数据生成附加信息
     */
    private void processHeader() {
        //计算还款期限=期数*12/支付频率
        if (calculatorHeader.getAnnualPayTimes() != 0) {
            calculatorHeader.setLeaseTerm((calculatorHeader.getLeaseTimes() * 12) / calculatorHeader.getAnnualPayTimes());
        }

        //计算结束日期
        LocalDate startDate = calculatorHeader.getLeaseStartDate();
        LocalDate endDate = startDate.plusMonths(calculatorHeader.getLeaseTerm());
        endDate = endDate;
        calculatorHeader.setLeaseEndDate(endDate);

        //租赁物价款-首付款
        financeAmount = calculatorHeader.getLeaseAmount() - calculatorHeader.getDownPayment();
        //每期利率=年利率/支付频率
        rate = calculatorHeader.getIntRate() / calculatorHeader.getAnnualPayTimes();
        //尾款利息=尾款*利率/期数；
        balloonInterest = (double) Math.round(calculatorHeader.getBalloon() * rate * 100) / 100;
    }

    /**
     * 根据行表反写头表信息
     */
    private void feedbackHeader() {
        double contractAmount = 0, totalRent = 0, totalInterest = 0, netTotalInterest = 0, vatTotalInterest = 0,
                totalFee = 0, netTotalFee = 0, vatTotalFee = 0, financeIncome = 0, netFinanceIncome = 0,
                vatFinanceIncome = 0, netLeaseCharge = 0, vatLeaseCharge = 0, leaseItemAmount = 0;

        //处理第0期数据
        CalculatorLine firstLine = lines.get(0);
        netLeaseCharge = firstLine.getNetLeaseCharge();
        vatLeaseCharge = firstLine.getVatLeaseCharge();
        totalFee = firstLine.getOtherFee() + firstLine.getOtherFee2() + firstLine.getOtherFee3();
        netTotalFee = firstLine.getNetOtherFee() + firstLine.getNetOtherFee2() + firstLine.getNetOtherFee3();
        vatTotalFee = firstLine.getVatOtherFee() + firstLine.getVatOtherFee2() + firstLine.getVatOtherFee3();

        for (int i = 1; i < lines.size(); i++) {
            CalculatorLine line = lines.get(i);
            totalRent += line.getRental();
            totalInterest += line.getInterest();
            netTotalInterest += line.getNetInterest();
            vatTotalInterest += line.getVatInterest();
            leaseItemAmount += line.getLeaseItemAmount();
        }

        totalRent = (double) Math.round(totalRent * 100) / 100;
        totalFee = (double) Math.round(totalFee * 100) / 100;
        totalInterest = (double) Math.round(totalInterest * 100) / 100;
        netTotalInterest = (double) Math.round(netTotalInterest * 100) / 100;
        vatTotalInterest = (double) Math.round(vatTotalInterest * 100) / 100;
        leaseItemAmount = (double) Math.round(leaseItemAmount * 100 / CommonConstUtil.DAYS_IN_YEAR) / 100;

        financeIncome = totalInterest + totalFee + firstLine.getLeaseCharge();
        netFinanceIncome = netTotalInterest + netTotalFee + netLeaseCharge;
        vatFinanceIncome = vatTotalInterest + vatTotalFee + vatLeaseCharge;
        contractAmount = firstLine.getLeaseAmount() + financeIncome;

        financeIncome = (double) Math.round(financeIncome * 100) / 100;
        netFinanceIncome = (double) Math.round(netFinanceIncome * 100) / 100;
        vatFinanceIncome = (double) Math.round(vatFinanceIncome * 100) / 100;
        contractAmount = (double) Math.round(contractAmount * 100) / 100;

        //更新数据
        calculatorHeader.setContractAmount(contractAmount);
        calculatorHeader.setTotalRental(totalRent);
        calculatorHeader.setTotalInterest(totalInterest);
        calculatorHeader.setNetTotalInterest(netTotalInterest);
        calculatorHeader.setVatTotalInterest(vatTotalInterest);
        calculatorHeader.setTotalFee(totalFee);
        calculatorHeader.setNetTotalFee(netTotalFee);
        calculatorHeader.setVatTotalFee(vatTotalFee);
        calculatorHeader.setFinanceIncome(financeIncome);
        calculatorHeader.setNetFinanceIncome(netFinanceIncome);
        calculatorHeader.setVatFinanceIncome(vatFinanceIncome);
        calculatorHeader.setNetLeaseCharge(netLeaseCharge);
        calculatorHeader.setVatLeaseCharge(vatLeaseCharge);
        calculatorHeader.setLeaseItemAmount(leaseItemAmount);

        //计算IRR
        IRR irr = new IRR(lines);
        calculatorHeader.setIrr(irr.compute() * calculatorHeader.getAnnualPayTimes());
        calculatorHeader.setRealIrr(irr.computeRealIrr(calculatorHeader.getLeaseTimes()));

        //计算XIRR
        IRR xirr = new IRR(lines, true);
        calculatorHeader.setXirr(xirr.compute());
    }

    /**
     * 保存报价行信息
     *
     * @param time
     * @param rent
     * @param principal
     * @param interest
     */
    private void saveCalculatorLine(int time, double rent, double principal, double interest) {
        CalculatorLine line = new CalculatorLine();
        line.setHeaderId(calculatorHeader.getHeaderId());
        line.setTimes(time);
        line.setRental(rent);
        line.setPrincipal(principal);
        line.setInterest(interest);

        //第0期处理
        if (time == 0) {
            //首付款+咨询费+保证金
            line.setLeaseAmount(calculatorHeader.getLeaseAmount());
            line.setDownPayment(calculatorHeader.getDownPayment());
            line.setLeaseCharge(calculatorHeader.getLeaseCharge());
            line.setDeposit(calculatorHeader.getDeposit());
            line.setPreInterest(calculatorHeader.getPreInterest());

            line.setCalcDate(calculatorHeader.getLeaseStartDate());
            line.setDueDate(calculatorHeader.getLeaseStartDate());

            //其他费用
            line.setOtherFee(calculatorHeader.getOtherFee());
            line.setOtherFee2(calculatorHeader.getOtherFee2());
            line.setOtherFee3(calculatorHeader.getOtherFee3());

            //剩余本金
            line.setOutstandingPrincipal(financeAmount);
            line.setLeaseItemAmount(0.0);

            //处理费用税额
            //if (calculatorHeader.getChargeVatRate() != 0) {
            line.setNetLeaseCharge((double) Math.round(line.getLeaseCharge() * 100 / (1 + calculatorHeader.getChargeVatRate())) / 100);
            line.setVatLeaseCharge((double) Math.round((line.getLeaseCharge() - line.getNetLeaseCharge()) * 100) / 100);

            line.setNetOtherFee((double) Math.round(line.getOtherFee() * 100 / (1 + calculatorHeader.getChargeVatRate())) / 100);
            line.setVatOtherFee((double) Math.round((line.getOtherFee() - line.getNetOtherFee()) * 100) / 100);

            line.setNetOtherFee2((double) Math.round(line.getOtherFee2() * 100 / (1 + calculatorHeader.getChargeVatRate())) / 100);
            line.setVatOtherFee2((double) Math.round((line.getOtherFee2() - line.getNetOtherFee2()) * 100) / 100);

            line.setNetOtherFee3((double) Math.round(line.getOtherFee3() * 100 / (1 + calculatorHeader.getChargeVatRate())) / 100);
            line.setVatOtherFee3((double) Math.round((line.getOtherFee3() - line.getNetOtherFee3()) * 100) / 100);
            //}

            //处理IRR现金流
            line.setCashflowIrr(-1 * (double) Math.round((financeAmount - line.getLeaseCharge() - line.getDeposit() - line.getPreInterest() - line.getOtherFee() - line.getOtherFee2() - line.getOtherFee3()) * 100) / 100);
        } else {
            CalculatorLine lastLine = lines.get(time - 1);
            //剩余本金
            double oustandingPrincipal = (double) Math.round((lastLine.getOutstandingPrincipal() - lastLine.getPrincipal()) * 100) / 100;
            line.setOutstandingPrincipal(oustandingPrincipal);

            //还款日期
            LocalDate dueDate = null;
            LocalDate startDate = calculatorHeader.getLeaseStartDate();
            if (CommonConstUtil.AD_PAY_TYPE.equalsIgnoreCase(calculatorHeader.getPayType())) {
                //先付
                if (time == 1) {
                    dueDate = startDate;
                } else {
                    dueDate = startDate.plusMonths((12 / calculatorHeader.getAnnualPayTimes()) * (time - 1));
                }
            } else if (CommonConstUtil.AT_PAY_TYPE.equalsIgnoreCase(calculatorHeader.getPayType())) {
                //后付
                dueDate = startDate.plusMonths((12 / calculatorHeader.getAnnualPayTimes()) * (time));
            }

            line.setCalcDate(dueDate);
            line.setDueDate(dueDate);

            //占用天数
            int interestPeriodDays = (int) DAYS.between(calculatorHeader.getLeaseStartDate(), dueDate);
            line.setInterestPeriodDays(interestPeriodDays);
            //先息后本需按天计算利息
            if (CommonConstUtil.APPEASE.equalsIgnoreCase(calcType)) {
                //计算计息天数
                int leaseDays = (int) DAYS.between(lastLine.getDueDate(), dueDate);
                double eachInterest = (double) Math.round(interest * leaseDays * 100) / 100;
                double eachRental = eachInterest + principal;
                line.setRental(eachRental);
                line.setInterest(eachInterest);
            }

            //项目期总余额
            line.setLeaseItemAmount((double) Math.round(principal * interestPeriodDays * 100) / 100);

            //末期处理
            if (time == calculatorHeader.getLeaseTimes()) {
                line.setDeposit(-1 * calculatorHeader.getDeposit());
                line.setResidualValue(calculatorHeader.getResidualValue());
                line.setBalloon(calculatorHeader.getBalloon());
                line.setCashflowIrr((double) Math.round((line.getRental() + line.getBalloon() + line.getResidualValue() + line.getDeposit()) * 100) / 100);
            } else {
                line.setCashflowIrr(line.getRental());
            }

            //处理利息税额
            //if (calculatorHeader.getVatRate() != 0) {
            //利息
            line.setNetInterest((double) Math.round(line.getInterest() * 100 / (1 + calculatorHeader.getVatRate())) / 100);
            line.setVatInterest((double) Math.round((line.getInterest() - line.getNetInterest()) * 100) / 100);


            //本金 ：正租拆税，回租不拆税
            if (CommonConstUtil.BUSINESS_TYPE_LEASE.equalsIgnoreCase(calculatorHeader.getBusinessType())) {
                line.setNetPrincipal((double) Math.round(line.getPrincipal() * 100 / (1 + calculatorHeader.getVatRate())) / 100);
                line.setVatPrincipal((double) Math.round((line.getPrincipal() - line.getNetPrincipal()) * 100) / 100);
            } else {
                line.setNetPrincipal(line.getPrincipal());
                line.setVatPrincipal(0.0);
            }

            //租金
            line.setNetRental((double) Math.round((line.getNetInterest() + line.getNetPrincipal()) * 100) / 100);
            line.setVatRental((double) Math.round((line.getVatInterest() + line.getVatPrincipal()) * 100) / 100);
            //}
        }

        //注入数据
        lines.add(line);
    }

    /**
     * 等额本金
     *
     * @return
     */
    public List<CalculatorLine> computeEqualPrincipal() {
        //预处理头信息
        this.processHeader();

        //获取基础数据
        int times = calculatorHeader.getLeaseTimes();
        //每月本金=融资金额-尾款
        double eachPrincipal = (double) Math.round((financeAmount - calculatorHeader.getBalloon()) * 100 / times) / 100;
        double usePrincipal = 0;
        //处理第0期
        saveCalculatorLine(0, 0.0, 0.0, 0.0);

        //循环计算每期利息
        for (int i = 1; i <= times; i++) {
            double interest = (double) Math.round((financeAmount - usePrincipal) * 100 * rate) / 100;
            double principal = 0.0;
            double rent = 0.0;

            if (i < times) {
                principal = eachPrincipal;
            } else {
                principal = (double) Math.round((financeAmount - usePrincipal - calculatorHeader.getBalloon()) * 100) / 100;
            }

            rent = principal + interest;
            saveCalculatorLine(i, rent, principal, interest);
            usePrincipal += principal;
        }

        //回写头信息
        this.feedbackHeader();
        calculatorHeader.setCalcSuccessful(CommonConstUtil.YES);
        return this.lines;
    }

    /**
     * 等额本息
     *
     * @return
     */
    public List<CalculatorLine> computeEqualPayment() {
        //预处理头信息
        this.processHeader();

        //获取基础数据
        int times = calculatorHeader.getLeaseTimes();

        //每期租金
        double financeValue = financeAmount - calculatorHeader.getBalloon();
        double eachRental = Excel.pmt(rate, times, financeValue);
        double eachInterest = 0;
        double principal = 0;
        double rental = 0;
        double interest = 0;
        double usePrincipal = 0;
        //处理第0期
        saveCalculatorLine(0, 0.0, 0.0, 0.0);

        //循环计算每期利息
        for (int i = 1; i <= times; i++) {
            eachInterest = Excel.ipmt(rate, i, times, financeValue);
            if (i < times) {
                principal = (double) Math.round((eachRental - eachInterest) * 100) / 100;
            } else {
                principal = (double) Math.round((financeValue - usePrincipal) * 100) / 100;
                if (rate != 0) {
                    eachInterest = (double) Math.round((eachRental - principal) * 100) / 100;
                } else {
                    eachInterest = 0;
                }
            }

            interest = eachInterest + balloonInterest;
            rental = interest + principal;

            saveCalculatorLine(i, rental, principal, interest);
            usePrincipal += principal;
        }

        //回写头信息
        this.feedbackHeader();
        calculatorHeader.setCalcSuccessful(CommonConstUtil.YES);
        return this.lines;
    }

    /**
     * 平息法
     *
     * @return
     */
    public List<CalculatorLine> computeEqualInterest() {
        //预处理头信息
        this.processHeader();

        //获取基础数据
        int times = calculatorHeader.getLeaseTimes();
        double financeValue = financeAmount - calculatorHeader.getBalloon();

        //每期租金
        double eachRental = 0;
        double eachInterest = (double) Math.round(financeAmount * rate * 100) / 100;
        double eachPrincipal = (double) Math.round(financeValue * 100 / times) / 100;
        double usePrincipal = 0;
        //处理第0期
        saveCalculatorLine(0, 0.0, 0.0, 0.0);

        //循环计算每期利息
        for (int i = 1; i <= times; i++) {
            if (backflowCycle == 0) {
                if (i != times) {
                    eachPrincipal = (double) Math.round((financeValue - usePrincipal) * 100) / 100;
                }
            } else {
                if (i % backflowCycle == 0) {
                    if (i != times) {
                        eachPrincipal = (double) Math.round(financeValue * 100 / Math.ceil(times / backflowCycle)) / 100;
                    } else {
                        eachPrincipal = (double) Math.round((financeValue - usePrincipal) * 100) / 100;
                    }
                } else {
                    if (i != times) {
                        eachPrincipal = 0;
                    } else {
                        eachPrincipal = (double) Math.round((financeValue - usePrincipal) * 100) / 100;
                    }
                }

                eachInterest = (double) Math.round((financeAmount - usePrincipal) * rate * 100) / 100;
            }

            eachRental = eachPrincipal + eachInterest;
            usePrincipal += eachPrincipal;
            saveCalculatorLine(i, eachRental, eachPrincipal, eachInterest);
        }

        //回写头信息
        this.feedbackHeader();
        calculatorHeader.setCalcSuccessful(CommonConstUtil.YES);
        return this.lines;
    }

    /**
     * 先息后本
     *
     * @return
     */
    public List<CalculatorLine> computeAppEase() {
        //预处理头信息
        this.processHeader();

        //获取基础数据
        int times = calculatorHeader.getLeaseTimes();
        double financeValue = financeAmount - calculatorHeader.getBalloon();

        //每期租金
        double eachRental = 0;
        double eachInterest = financeAmount * calculatorHeader.getIntRate() / calculatorHeader.getCalculateDays();
        double eachPrincipal = 0;
        double usePrincipal = 0;
        //处理第0期
        saveCalculatorLine(0, 0.0, 0.0, 0.0);

        //循环计算每期利息
        for (int i = 1; i <= times; i++) {
            if (backflowCycle == 0) {
                if (i != times) {
                    eachPrincipal = 0;
                } else {
                    eachPrincipal = financeValue;
                }
            } else {
                if (i % backflowCycle == 0) {
                    if (i != times) {
                        eachPrincipal = (double) Math.round(financeValue * 100 / Math.ceil(times / backflowCycle)) / 100;
                    } else {
                        eachPrincipal = (double) Math.round((financeValue - usePrincipal) * 100) / 100;
                    }
                } else {
                    if (i != times) {
                        eachPrincipal = 0;
                    } else {
                        eachPrincipal = (double) Math.round((financeValue - usePrincipal) * 100) / 100;
                    }
                }

                eachInterest = (financeAmount - usePrincipal) * calculatorHeader.getIntRate() * 100 / calculatorHeader.getCalculateDays();
            }

            eachRental = eachPrincipal + eachInterest;
            usePrincipal += eachPrincipal;
            saveCalculatorLine(i, eachRental, eachPrincipal, eachInterest);
        }

        //回写头信息
        this.feedbackHeader();
        calculatorHeader.setCalcSuccessful(CommonConstUtil.YES);
        return this.lines;
    }

    /**
     * 保存报价行信息
     */
    private void resaveCalculatorLine() {
        double eachInterest = 0;
        double leaseDays = 0;

        //通过还款计划，反算利率
        boolean reSplit = false;
        //构建均摊irr现金流量，0
        List<CalculatorLine> shareLines = new ArrayList<CalculatorLine>();
        CalculatorLine firstShareLine = lines.get(0);
        firstShareLine.setCashflowIrr(-1 * financeAmount);
        shareLines.add(firstShareLine);

        int divideMonths = (int) (ChronoUnit.MONTHS.between(lines.get(0).getDueDate(), lines.get(lines.size() - 1).getDueDate()) / (lines.size() - 1));
        //默认还款计划当期租金不为0的行数为期数
        int leaseTimes = 0;
        for (int t = 1; t < lines.size(); t++) {
            CalculatorLine line = lines.get(t);
            if (line.getPrincipal() + line.getInterest() != line.getRental() || line.getLeaseCharge() > 0 || line.getDeposit() > 0 || line.getOtherFee() > 0 || line.getOtherFee2() > 0 || line.getOtherFee3() > 0) {
                reSplit = true;
            }

            ////构建均摊irr现金流量，i>=1
            if (line.getRental() != 0) {
                leaseTimes += 1;
                line.setCashflowIrr(line.getRental());
                shareLines.add(line);
            }
        }
        //设置期数
        calculatorHeader.setLeaseTimes(leaseTimes);
        //通过shareLines得到均摊IRR
        IRR shareIrr = new IRR(shareLines);
        double shareRate = shareIrr.compute();
        this.rate = shareRate;

        //拆分标志为true，说明需要反算
        if (reSplit) {
            calculatorHeader.setAnnualPayTimes(12 / divideMonths);
            calculatorHeader.setLeaseTerm(calculatorHeader.getLeaseTimes() * divideMonths);

            //处理剩余本金
            CalculatorLine firstLine = lines.get(0);
            firstLine.setRental(0);
            firstLine.setPrincipal(0);
            firstLine.setInterest(0);
            firstLine.setOutstandingPrincipal(financeAmount);
            firstLine.setCashflowIrr(-1 * (double) Math.round((financeAmount - firstLine.getLeaseCharge() - firstLine.getDeposit() - firstLine.getPreInterest() - firstLine.getOtherFee() - firstLine.getOtherFee2() - firstLine.getOtherFee3()) * 100) / 100);
            lines.set(0, firstLine);

            //处理首期费用
            //设置利率=均摊IRR
            calculatorHeader.setIntRate(shareRate * calculatorHeader.getAnnualPayTimes());
            //首付款+咨询费+保证金
            calculatorHeader.setDownPayment(firstLine.getDownPayment());
            calculatorHeader.setLeaseCharge(firstLine.getLeaseCharge());
            calculatorHeader.setDeposit(firstLine.getDeposit());
            calculatorHeader.setPreInterest(firstLine.getPreInterest());

            //其他费用
            calculatorHeader.setOtherFee(firstLine.getOtherFee());
            calculatorHeader.setOtherFee2(firstLine.getOtherFee2());
            calculatorHeader.setOtherFee3(firstLine.getOtherFee3());
        }


        for (int i = 1; i < lines.size(); i++) {
            CalculatorLine line = lines.get(i);
            CalculatorLine lastLine = lines.get(i - 1);
            eachInterest = 0;
            leaseDays = 0;

            //剩余本金
            double oustandingPrincipal = (double) Math.round((lastLine.getOutstandingPrincipal() - lastLine.getPrincipal()) * 100) / 100;
            line.setOutstandingPrincipal(oustandingPrincipal);

            //占用天数
            LocalDate dueDate = line.getDueDate();
            int interestPeriodDays = (int) DAYS.between(calculatorHeader.getLeaseStartDate(), dueDate);
            line.setInterestPeriodDays(interestPeriodDays);

            //如果本金利息都为空，手工计算
            if (reSplit) {
                if (CommonConstUtil.EQUAL_PAYMENT.equalsIgnoreCase(calcType)) {
                    //等额本息
                    double financeValue = financeAmount - calculatorHeader.getBalloon();
                    eachInterest = Excel.ipmt(rate, i, calculatorHeader.getLeaseTimes(), financeValue);

                    if (i == calculatorHeader.getLeaseTimes()) {
                        line.setPrincipal(oustandingPrincipal - calculatorHeader.getBalloon());
                        line.setInterest((double) Math.round((line.getRental() - line.getPrincipal()) * 100) / 100);
                    } else {
                        line.setInterest(eachInterest);
                        line.setPrincipal((double) Math.round((line.getRental() - line.getInterest()) * 100) / 100);
                    }
                } else if (CommonConstUtil.EQUAL_PRINCIPAL.equalsIgnoreCase(calcType)) {
                    //等额本金
                    eachInterest = (double) Math.round(oustandingPrincipal * 100 * rate) / 100;

                    if (i == calculatorHeader.getLeaseTimes()) {
                        line.setPrincipal(oustandingPrincipal - calculatorHeader.getBalloon());
                        line.setInterest((double) Math.round((line.getRental() - line.getPrincipal()) * 100) / 100);
                    } else {
                        line.setInterest(eachInterest);
                        line.setPrincipal((double) Math.round((line.getRental() - line.getInterest()) * 100) / 100);
                    }
                } else if (CommonConstUtil.EQUAL_INTEREST.equalsIgnoreCase(calcType) || CommonConstUtil.FREE_CASHFLOW.equalsIgnoreCase(calcType)) {
                    //平息法
                    double financeValue = financeAmount - calculatorHeader.getBalloon();
                    double eachPrincipal = (double) Math.round(financeValue * 100 / calculatorHeader.getLeaseTimes()) / 100;
                    if (backflowCycle == 0) {
                        if (i == calculatorHeader.getLeaseTimes()) {
                            eachPrincipal = (double) Math.round((oustandingPrincipal - calculatorHeader.getBalloon()) * 100) / 100;
                        }
                    } else {
                        if (i % backflowCycle == 0) {
                            if (i != calculatorHeader.getLeaseTimes()) {
                                eachPrincipal = (double) Math.round(financeValue * 100 / Math.ceil(calculatorHeader.getLeaseTimes() / backflowCycle)) / 100;
                            } else {
                                eachPrincipal = (double) Math.round((oustandingPrincipal - calculatorHeader.getBalloon()) * 100) / 100;
                            }
                        } else {
                            if (i != calculatorHeader.getLeaseTimes()) {
                                eachPrincipal = 0;
                            } else {
                                eachPrincipal = (double) Math.round((oustandingPrincipal - calculatorHeader.getBalloon()) * 100) / 100;
                            }
                        }
                    }

                    line.setPrincipal(eachPrincipal);
                    line.setInterest((double) Math.round((line.getRental() - line.getPrincipal()) * 100) / 100);
                } else if (CommonConstUtil.APPEASE.equalsIgnoreCase(calcType)) {
                    //计算计息天数
                    leaseDays = (int) DAYS.between(lastLine.getDueDate(), dueDate);
                    eachInterest = (double) Math.round(financeAmount * calculatorHeader.getIntRate() * leaseDays * 100 / calculatorHeader.getCalculateDays()) / 100;

                    if (i == calculatorHeader.getLeaseTimes()) {
                        line.setPrincipal((double) Math.round((oustandingPrincipal - calculatorHeader.getBalloon()) * 100) / 100);
                        line.setInterest((double) Math.round((line.getRental() - line.getPrincipal()) * 100) / 100);
                    } else {
                        line.setInterest(eachInterest);
                        line.setPrincipal((double) Math.round((line.getRental() - line.getInterest()) * 100) / 100);
                    }
                }
            } else {
                //如果是先息后本需重算利息
                if (CommonConstUtil.APPEASE.equalsIgnoreCase(calcType)) {
                    //计算计息天数
                    leaseDays = (int) DAYS.between(lastLine.getDueDate(), dueDate);
                    eachInterest = (double) Math.round(financeAmount * calculatorHeader.getIntRate() * leaseDays * 100 / calculatorHeader.getCalculateDays()) / 100;

                    if (i == calculatorHeader.getLeaseTimes()) {
                        line.setPrincipal((double) Math.round((oustandingPrincipal - calculatorHeader.getBalloon()) * 100) / 100);
                        line.setInterest(eachInterest);
                        line.setRental(line.getPrincipal() + line.getInterest());
                    } else {
                        line.setInterest(eachInterest);
                        line.setRental(line.getPrincipal() + line.getInterest());
                    }
                }
            }

            //项目期总余额
            double principal = line.getPrincipal();
            line.setLeaseItemAmount((double) Math.round(principal * interestPeriodDays * 100) / 100);

            //末期处理
            if (i == calculatorHeader.getLeaseTimes()) {
                line.setCashflowIrr(line.getRental() + line.getBalloon() + line.getResidualValue() + line.getDeposit());
                //回写结束日期
                calculatorHeader.setLeaseEndDate(line.getDueDate());
            } else {
                line.setCashflowIrr(line.getRental() + line.getLeaseCharge() + line.getDeposit() + line.getOtherFee() + line.getOtherFee2() + line.getOtherFee3());
            }

            //处理利息税额
            if (calculatorHeader.getVatRate() != 0) {
                //利息
                line.setNetInterest((double) Math.round(line.getInterest() * 100 / (1 + calculatorHeader.getVatRate())) / 100);
                line.setVatInterest((double) Math.round((line.getInterest() - line.getNetInterest()) * 100) / 100);
                //本金
                line.setNetPrincipal(line.getPrincipal());
                line.setVatPrincipal(0.0);
                //租金
                line.setNetRental((double) Math.round((line.getNetInterest() + line.getNetPrincipal()) * 100) / 100);
                line.setVatRental((double) Math.round((line.getVatInterest() + line.getVatPrincipal()) * 100) / 100);
            }

            lines.set(i, line);
        }
    }

    /**
     * 重算头表信息
     */
    public void recomputer() {
        //处理头信息
        processHeader();

        //重算行信息
        resaveCalculatorLine();

        //回写头信息
        this.feedbackHeader();
        calculatorHeader.setCalcSuccessful(CommonConstUtil.YES);
    }
}
