package com.stephen.calculator.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author IvanStephen
 * @project calculator
 * @date 2019/10/24 17:00
 **/
@Entity
@Data
@ApiModel(description = "报价器头表")
@Table(name = "cls_fin_calculator_hd", indexes = {@Index(name = "cls_fin_calculator_hd_n1",  columnList="price_list")})
@org.hibernate.annotations.Table(appliesTo = "cls_fin_calculator_hd", comment = "报价头表")
public class CalculatorHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "header_id")
    private long headerId;
    
    @Column(name = "price_list", columnDefinition = "varchar(60) comment '价目表代码'")
    @ApiModelProperty(value = "产品名称")
    private String priceList;

    @Column(name = "calc_successful", columnDefinition = "varchar(1) comment '计算成功标志'")
    @ApiModelProperty(value = "计算成功标志")
    private String calcSuccessful;

    @Column(name = "business_type", columnDefinition = "varchar(60) comment '业务类型'")
    @ApiModelProperty(value = "业务类型")
    private String businessType;

    @Column(name = "lease_start_date", columnDefinition = "datetime comment '租赁开始日'")
    @ApiModelProperty(value = "租赁开始日")
    private LocalDate leaseStartDate;

    @Column(name = "lease_end_date", columnDefinition = "datetime comment '租赁结束日'")
    @ApiModelProperty(value = "租赁结束日")
    private LocalDate leaseEndDate;

    @Column(name = "pay_type", columnDefinition = "varchar(1) comment '支付方式'")
    @ApiModelProperty(value = "支付方式")
    private String payType;

    @Column(name = "lease_times", columnDefinition = "varchar(30) comment '支付方式'")
    @ApiModelProperty(value = "租赁期数")
    private int leaseTimes;

    @Column(name = "annual_pay_times", columnDefinition = "varchar(30) comment '支付频率'")
    @ApiModelProperty(value = "支付频率")
    private int annualPayTimes;

    @Column(name = "lease_term", columnDefinition = "varchar(30) comment '租赁期限'")
    @ApiModelProperty(value = "租赁期限")
    private int leaseTerm;

    @Column(name = "currency", columnDefinition = "varchar(30) comment '币种'")
    @ApiModelProperty(value = "币种")
    private String currency;

    @Column(name = "vat_rate", columnDefinition = "Decimal(10,6) comment '利息税率'")
    @ApiModelProperty(value = "利息税率")
    private Double vatRate;

    @Column(name = "charge_vat_rate", columnDefinition = "Decimal(10,6) comment '咨询费税率'")
    @ApiModelProperty(value = "咨询费税率")
    private double chargeVatRate;

    @Column(name = "billing_method", columnDefinition = "varchar(60) comment '开票方式'")
    @ApiModelProperty(value = "开票方式")
    private String billingMethod;

    @Column(name = "flt_adj_method", columnDefinition = "varchar(60) comment '上浮/下调'")
    @ApiModelProperty(value = "上浮/下调")
    private String fltAdjMethod;

    @Column(name = "flt_adj_range", columnDefinition = "varchar(60) comment '幅度'")
    @ApiModelProperty(value = "幅度")
    private String fltAdjRange;

    @Column(name = "flt_adj_times", columnDefinition = "varchar(60) comment '调整频率'")
    @ApiModelProperty(value = "调整频率")
    private String fltAdjTimes;

    @Column(name = "base_rate", columnDefinition = "decimal(10,6) comment '基准利率'")
    @ApiModelProperty(value = "基准利率")
    private double baseRate;

    @Column(name = "int_rate", columnDefinition = "decimal(10,6) comment '租赁利率'")
    @ApiModelProperty(value = "租赁利率")
    private double intRate;

    @Column(name = "calculate_days", columnDefinition = "decimal(10,6) comment '年计算天数'")
    @ApiModelProperty(value = "年计算天数")
    private double calculateDays;

    @Column(name = "irr", columnDefinition = "decimal(10,6) comment '内部收益率'")
    @ApiModelProperty(value = "内部收益率")
    private double irr;

    @Column(name = "real_irr", columnDefinition = "decimal(10,6) comment '实际IRR'")
    @ApiModelProperty(value = "实际IRR")
    private double realIrr;

    @Column(name = "xirr", columnDefinition = "decimal(10,6) comment '扩展内部收益率'")
    @ApiModelProperty(value = "扩展内部收益率")
    private double xirr;

    @Column(name = "inter_irr", columnDefinition = "decimal(10,6) comment '管控内部收益率'")
    @ApiModelProperty(value = "管控内部收益率")
    private double interIrr;

    @Column(name = "inter_xirr", columnDefinition = "decimal(10,6) comment '管控扩展内部收益率'")
    @ApiModelProperty(value = "管控扩展内部收益率")
    private double interXirr;

    @Column(name = "lease_amount", columnDefinition = "decimal(12,2) comment '租赁物价款'")
    @ApiModelProperty(value = "租赁物价款")
    private double leaseAmount;

    @Column(name = "down_payment", columnDefinition = "decimal(12,2) comment '首付款'")
    @ApiModelProperty(value = "首付款")
    private double downPayment;

    @Column(name = "contract_amount", columnDefinition = "decimal(12,2) comment '合同总额'")
    @ApiModelProperty(value = "合同总额")
    private double contractAmount;

    @Column(name = "total_rental", columnDefinition = "decimal(12,2) comment '租金总额'")
    @ApiModelProperty(value = "租金总额")
    private double totalRental;

    @Column(name = "total_interest", columnDefinition = "decimal(12,2) comment '利息总额'")
    @ApiModelProperty(value = "利息总额")
    private double totalInterest;

    @Column(name = "net_total_interest", columnDefinition = "decimal(12,2) comment '不含税利息'")
    @ApiModelProperty(value = "不含税利息")
    private double netTotalInterest;

    @Column(name = "vat_total_interest", columnDefinition = "decimal(12,2) comment '利息增值税额'")
    @ApiModelProperty(value = "利息增值税额")
    private double vatTotalInterest;

    @Column(name = "total_fee", columnDefinition = "decimal(12,2) comment '其他收入总额'")
    @ApiModelProperty(value = "其他收入总额")
    private double totalFee;

    @Column(name = "net_total_fee", columnDefinition = "decimal(12,2) comment '不含税费用金额'")
    @ApiModelProperty(value = "不含税费用金额")
    private double netTotalFee;

    @Column(name = "vat_total_fee", columnDefinition = "decimal(12,2) comment '其他收入增值税额'")
    @ApiModelProperty(value = "其他收入增值税额")
    private double vatTotalFee;

    @Column(name = "finance_income", columnDefinition = "decimal(12,2) comment '租赁收入'")
    @ApiModelProperty(value = "租赁收入")
    private double financeIncome;

    @Column(name = "net_finance_income", columnDefinition = "decimal(12,2) comment '不含税租赁收入'")
    @ApiModelProperty(value = "不含税租赁收入")
    private double netFinanceIncome;

    @Column(name = "vat_finance_income", columnDefinition = "decimal(12,2) comment '租赁收入税额'")
    @ApiModelProperty(value = "租赁收入税额")
    private double vatFinanceIncome;

    @Column(name = "lease_charge", columnDefinition = "decimal(12,2) comment '咨询费'")
    @ApiModelProperty(value = "咨询费")
    private double leaseCharge;

    @Column(name = "net_lease_charge", columnDefinition = "decimal(12,2) comment '不含税咨询费'")
    @ApiModelProperty(value = "不含税咨询费")
    private double netLeaseCharge;

    @Column(name = "vat_lease_charge", columnDefinition = "decimal(12,2) comment '咨询费税额'")
    @ApiModelProperty(value = "咨询费税额")
    private double vatLeaseCharge;

    @Column(name = "residual_value", columnDefinition = "decimal(12,2) comment '留购价款'")
    @ApiModelProperty(value = "留购价款")
    private double residualValue;

    @Column(name = "deposit", columnDefinition = "decimal(12,2) comment '保证金'")
    @ApiModelProperty(value = "保证金")
    private double deposit;

    @Column(name = "deposit_deduction", columnDefinition = "varchar(1) comment '保证金是否抵扣'")
    @ApiModelProperty(value = "保证金是否抵扣")
    private String depositDeduction;

    @Column(name = "balloon", columnDefinition = "decimal(12,2) comment '尾款'")
    @ApiModelProperty(value = "尾款")
    private double balloon;

    @Column(name = "exchange_rate", columnDefinition = "decimal(10,6) comment '汇率'")
    @ApiModelProperty(value = "汇率")
    private double exchangeRate;

    @Column(name = "other_fee", columnDefinition = "decimal(12,2) comment '其他费用'")
    @ApiModelProperty(value = "其他费用")
    private double otherFee;

    @Column(name = "other_fee2", columnDefinition = "decimal(12,2) comment '其他费用2'")
    @ApiModelProperty(value = "其他费用2")
    private double otherFee2;

    @Column(name = "other_fee3", columnDefinition = "decimal(12,2) comment '其他费用3'")
    @ApiModelProperty(value = "其他费用3")
    private double otherFee3;

    @Column(name = "lease_item_amount", columnDefinition = "decimal(12,2) comment '项目期总余额'")
    @ApiModelProperty(value = "项目期总余额")
    private double leaseItemAmount;
}
