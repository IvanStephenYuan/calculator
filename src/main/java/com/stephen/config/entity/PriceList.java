package com.stephen.config.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author IvanStephen
 * @project calculator
 * @date 2019/10/23 17:50
 **/
@Entity
@Data
@ApiModel(description = "产品表")
@Table(name = "cls_price_list", indexes = {@Index(name = "cls_price_list_n1",  columnList="price_list", unique = true)})
@org.hibernate.annotations.Table(appliesTo = "cls_price_list", comment = "产品表")
@Proxy(lazy = false)
public class PriceList implements Serializable{
    private static final long serialVersionUID = 7207974328030155273L;

    @Id
    @Column(name = "price_list", unique = true, nullable = false, columnDefinition = "varchar(60) comment '价目表代码'")
    @ApiModelProperty(value = "价目表代码", name = "priceList")
    private String priceList;

    @Column(name = "price_list_name", columnDefinition = "varchar(60) comment '产品名称'")
    @ApiModelProperty(value = "产品名称", name = "priceListName")
    private String priceListName;

    @Column(name = "description", length = 255)
    private String description;

    @OrderBy
    @Column(name = "order_seq")
    private int orderSeq;

    @Column(name = "calc_type")
    private  String calcType;

    @Column(name = "inter_irr", columnDefinition = "decimal comment '内部收益率'")
    private Double interIrr;

    @Column(name = "inter_xirr", columnDefinition = "decimal comment '内部扩展收益率'")
    private Double interXirr;

    @Column(name = "calc_usage")
    private String calcUsage;

    @Column(name = "backflow_cycle", columnDefinition = "decimal(10) comment '回本周期'")
    private int backflowCycle;

    @Column(name = "business_code")
    private String businessCode;

    @Column(name = "area_code")
    private String areaCode;

    @Column(name = "unit_code")
    private String unitCode;

    @Column(name = "employee_id")
    private Long emoloyeeId;

    @Column(name = "authority_group")
    private String authorityGroup;

    @Column(name = "enabled_flag", columnDefinition = "varchar(1) default 'Y' comment '启用'")
    private String enabledFlag;
}
