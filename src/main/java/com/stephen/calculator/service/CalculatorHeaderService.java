package com.stephen.calculator.service;

import com.stephen.calculator.core.Computer;
import com.stephen.calculator.entity.CalculatorHeader;
import com.stephen.calculator.entity.CalculatorLine;
import com.stephen.calculator.repository.CalculatorHeaderRepository;
import com.stephen.calculator.repository.CalculatorLineRepository;
import com.stephen.config.entity.PriceList;
import com.stephen.exception.CalcErrorCodeEnum;
import com.stephen.exception.ComputeException;
import com.stephen.util.CommonConstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author IvanStephen
 * @project calculator
 * @date 2019/10/28 10:22
 * @apiNote 核心计算
 **/
@Service
@Transactional
public class CalculatorHeaderService {
    @Autowired
    private CalculatorHeaderRepository calculatorHeaderRepository;

    @Autowired
    private CalculatorLineRepository calculatorLineRepository;

    private boolean checkCalculatorHeader(CalculatorHeader calculatorHeader) {
        //组成报价至少需要租赁物价款、利率、支付频率、期数、税率、租赁开始日
        if (calculatorHeader.getLeaseAmount() == 0 || calculatorHeader.getIntRate() < 0 ||
                calculatorHeader.getAnnualPayTimes() == 0 || calculatorHeader.getLeaseTimes() == 0 ||
                calculatorHeader.getVatRate() < 0 || calculatorHeader.getLeaseStartDate() == null) {
            return false;
        }

        //先付后付不能为空
        if("".equalsIgnoreCase(calculatorHeader.getPayType())){
            return false;
        }

        return true;
    }

    private boolean checkCalculatorLine(CalculatorHeader calculatorHeader, List<CalculatorLine> lines) {
        CalculatorLine firstLine = lines.get(0);
        CalculatorLine lastLine = new CalculatorLine();

        //期数不对
        if (lines.size() != calculatorHeader.getLeaseTimes() + 1) {
            return false;
        } else {
            lastLine = lines.get(calculatorHeader.getLeaseTimes());
        }

        //头行金额不对
        if (firstLine.getDownPayment() != calculatorHeader.getDownPayment() || firstLine.getDeposit() != calculatorHeader.getDeposit()
                || lastLine.getBalloon() != calculatorHeader.getBalloon() || lastLine.getResidualValue() != calculatorHeader.getResidualValue()) {
            return false;
        }

        //本金不对
        double totalPrincial = firstLine.getDownPayment() + lastLine.getBalloon();
        for (int i = 1; i < lines.size(); i++) {
            CalculatorLine line = lines.get(i);
            double rent = (double) Math.round((line.getPrincipal() + line.getInterest())*100)/100;
            if(rent != line.getRental()){
                return false;
            }
            totalPrincial += line.getPrincipal();
        }
        totalPrincial = (double) Math.round(totalPrincial * 100) / 100;
        if (totalPrincial != calculatorHeader.getLeaseAmount()) {
            return false;
        }

        return true;
    }

    /**
     * 获取报价头信息
     *
     * @param headerId
     * @return
     */
    public CalculatorHeader getCalculatorHeader(Long headerId) {
        return calculatorHeaderRepository.getOne(headerId);
    }

    /**
     * 计算
     *
     * @param priceList
     * @param calculatorHeader
     * @return
     */
    public List<CalculatorLine> compute(PriceList priceList, CalculatorHeader calculatorHeader) {
        //校验数据完整性
        if (checkCalculatorHeader(calculatorHeader) == false) {
            calculatorHeader.setCalcSuccessful(CommonConstUtil.NO);
            throw new ComputeException(CalcErrorCodeEnum.COMPUTE_CHECK_FAIL);
        } else {
            calculatorHeaderRepository.save(calculatorHeader);
            Long headerId = calculatorHeader.getHeaderId();
            calculatorLineRepository.deleteAllByHeaderId(headerId);
        }

        //构建计算器
        List<CalculatorLine> lines;
        Computer computer = new Computer(priceList.getCalcType(), priceList.getBackflowCycle(), calculatorHeader);
        switch (priceList.getCalcType()) {
            case CommonConstUtil.EQUAL_PRINCIPAL:
                //等额本金
                lines = computer.computeEqualPrincipal();
                break;
            case CommonConstUtil.EQUAL_PAYMENT:
                //等额本息
                lines = computer.computeEqualPayment();
                break;
            case CommonConstUtil.EQUAL_INTEREST:
                //平息法
                lines = computer.computeEqualInterest();
                break;
            case CommonConstUtil.APPEASE:
                //先息后本
                lines = computer.computeAppEase();
                break;
            case CommonConstUtil.FREE_CASHFLOW:
                //任意现金流
                lines = computer.computeEqualInterest();
                break;
            default:
                return null;
        }

        calculatorHeader = computer.getCalculatorHeader();

        //计算成功保存数据
        if (CommonConstUtil.YES.equalsIgnoreCase(calculatorHeader.getCalcSuccessful())) {
            calculatorHeaderRepository.save(calculatorHeader);
            calculatorLineRepository.saveAll(lines);
        }

        return lines;
    }

    /**
     * @param priceList
     * @param calculatorHeader
     * @return
     */
    public void recompute(PriceList priceList, CalculatorHeader calculatorHeader, List<CalculatorLine> lines) {
        //校验数据完整性
        if (checkCalculatorHeader(calculatorHeader) == false) {
            calculatorHeader.setCalcSuccessful(CommonConstUtil.NO);
            throw new ComputeException(CalcErrorCodeEnum.COMPUTE_CHECK_FAIL);
        }

        //检验行信息
        if (checkCalculatorLine(calculatorHeader, lines) == false) {
            calculatorHeader.setCalcSuccessful(CommonConstUtil.NO);
            throw new ComputeException(CalcErrorCodeEnum.RECOMPUTE_CHECK_FAIL);
        }

        Computer computer = new Computer(priceList.getCalcType(), priceList.getBackflowCycle(), calculatorHeader, lines);
        computer.recomputer();

        calculatorHeader = computer.getCalculatorHeader();
        lines = computer.getLines();

        //计算成功保存数据
        if (CommonConstUtil.YES.equalsIgnoreCase(calculatorHeader.getCalcSuccessful())) {
            calculatorHeaderRepository.save(calculatorHeader);
            calculatorLineRepository.saveAll(lines);
        }
    }
}
