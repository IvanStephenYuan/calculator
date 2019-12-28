package com.stephen.calculator.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author IvanStephen
 * @project calculator
 * @date 2019/10/24 17:01
 **/
@Entity
@Data
@ApiModel(description = "报价器行表")
@Table(name = "cls_fin_calculator_ln", indexes = {@Index(name = "cls_fin_calculator_ln_n1",  columnList="header_id", unique = false)})
@org.hibernate.annotations.Table(appliesTo = "cls_fin_calculator_ln", comment = "报价行表")
public class CalculatorLine {
    public CalculatorLine(){

    }

    public CalculatorLine(LocalDate dueDate, int interestPeriodDays, double cashflowIrr){
        this.dueDate = dueDate;
        this.interestPeriodDays = interestPeriodDays;
        this.cashflowIrr = cashflowIrr;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_id")
    private long lineId;

    @Column(name = "header_id", nullable = false)
    private long headerId;

    @Column(name = "times", columnDefinition = "decimal(3,0) comment '期数'")
    @ApiModelProperty(value = "期数")
    private int times;

    @Column(name = "calc_date", columnDefinition = "DateTime comment '计算日'")
    @ApiModelProperty(value = "计算日")
    private LocalDate calcDate;

    @Column(name = "due_date", columnDefinition = "DateTime comment '到期日'")
    @ApiModelProperty(value = "到期日")
    private LocalDate dueDate;

    @Column(name = "rental", columnDefinition = "decimal(12,2) comment '租金'")
    @ApiModelProperty(value = "租金")
    private double rental;

    @Column(name = "net_rental", columnDefinition = "decimal(12,2) comment '不含税租金'")
    @ApiModelProperty(value = "不含税租金")
    private double netRental;

    @Column(name = "vat_rental", columnDefinition = "decimal(12,2) comment '租金增值税'")
    @ApiModelProperty(value = "租金增值税")
    private double vatRental;

    @Column(name = "principal", columnDefinition = "decimal(12,2) comment '本金'")
    @ApiModelProperty(value = "本金")
    private double principal;

    @Column(name = "net_principal", columnDefinition = "decimal(12,2) comment '不含税本金'")
    @ApiModelProperty(value = "不含税本金")
    private double netPrincipal;

    @Column(name = "vat_principal", columnDefinition = "decimal(12,2) comment '本金增值税'")
    @ApiModelProperty(value = "本金增值税")
    private double vatPrincipal;

    @Column(name = "interest", columnDefinition = "decimal(12,2) comment '利息'")
    @ApiModelProperty(value = "利息")
    private double interest;

    @Column(name = "net_interest", columnDefinition = "decimal(12,2) comment '不含税利息'")
    @ApiModelProperty(value = "不含税利息")
    private double netInterest;

    @Column(name = "vat_interest", columnDefinition = "decimal(12,2) comment '利息增值税'")
    @ApiModelProperty(value = "利息增值税")
    private double vatInterest;

    @Column(name = "outstanding_principal", columnDefinition = "decimal(12,2) comment '剩余本金'")
    @ApiModelProperty(value = "剩余本金")
    private double outstandingPrincipal;

    @Column(name = "interest_period_days", columnDefinition = "decimal(12,0) comment '占用天数'")
    @ApiModelProperty(value = "占用天数")
    private int interestPeriodDays;

    @Column(name = "lease_amount", columnDefinition = "decimal(12,2) comment '租赁物价款'")
    @ApiModelProperty(value = "租赁物价款")
    private double leaseAmount;

    @Column(name = "down_payment", columnDefinition = "decimal(12,2) comment '首付款'")
    @ApiModelProperty(value = "首付款")
    private double downPayment;

    @Column(name = "lease_charge", columnDefinition = "decimal(12,2) comment '咨询费'")
    @ApiModelProperty(value = "咨询费")
    private double leaseCharge;

    @Column(name = "net_lease_charge", columnDefinition = "decimal(12,2) comment '不含税咨询费'")
    @ApiModelProperty(value = "不含税咨询费")
    private double netLeaseCharge;

    @Column(name = "vat_lease_charge", columnDefinition = "decimal(12,2) comment '咨询费税额'")
    @ApiModelProperty(value = "咨询费税额")
    private double vatLeaseCharge;

    @Column(name = "residual_value", columnDefinition = "decimal(12,2) comment '留购价'")
    @ApiModelProperty(value = "留购价")
    private double residualValue;

    @Column(name = "deposit", columnDefinition = "decimal(12,2) comment '保证金'")
    @ApiModelProperty(value = "保证金")
    private double deposit;

    @Column(name = "balloon", columnDefinition = "decimal(12,2) comment '尾款'")
    @ApiModelProperty(value = "尾款")
    private double balloon;

    @Column(name = "other_fee", columnDefinition = "decimal(12,2) comment '其他费用'")
    @ApiModelProperty(value = "其他费用")
    private double otherFee;

    @Column(name = "net_other_fee", columnDefinition = "decimal(12,2) comment '不含税其他费用'")
    @ApiModelProperty(value = "不含税其他费用")
    private double netOtherFee;

    @Column(name = "vat_other_fee", columnDefinition = "decimal(12,2) comment '其他费用税额'")
    @ApiModelProperty(value = "其他费用税额")
    private double vatOtherFee;

    @Column(name = "other_fee2", columnDefinition = "decimal(12,2) comment '其他费用2'")
    @ApiModelProperty(value = "其他费用2")
    private double otherFee2;

    @Column(name = "net_other_fee2", columnDefinition = "decimal(12,2) comment '不含税其他费用2'")
    @ApiModelProperty(value = "不含税其他费用2")
    private double netOtherFee2;

    @Column(name = "vat_other_fee2", columnDefinition = "decimal(12,2) comment '其他费用2税额'")
    @ApiModelProperty(value = "其他费用2税额")
    private double vatOtherFee2;

    @Column(name = "other_fee3", columnDefinition = "decimal(12,2) comment '其他费用3'")
    @ApiModelProperty(value = "其他费用3")
    private double otherFee3;

    @Column(name = "net_other_fee3", columnDefinition = "decimal(12,2) comment '不含税其他费用3'")
    @ApiModelProperty(value = "不含税其他费用3")
    private double netOtherFee3;

    @Column(name = "vat_other_fee3", columnDefinition = "decimal(12,2) comment '其他费用3税额'")
    @ApiModelProperty(value = "其他费用3税额")
    private double vatOtherFee3;

    @Column(name = "cashflow_irr", columnDefinition = "decimal(12,2) comment 'IRR现金流'")
    @ApiModelProperty(value = "IRR现金流")
    private double cashflowIrr;

    @Column(name = "lease_item_amount", columnDefinition = "decimal(14,2) comment '项目期总余额'")
    @ApiModelProperty(value = "项目期总余额")
    private double leaseItemAmount;
}
