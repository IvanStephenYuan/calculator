package com.stephen.config.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * @author IvanStephen
 * @project calculator
 * @date 2019/10/23 18:01
 **/
@Entity
@Data
@ApiModel(description = "产品明细配置")
@Table(name = "cls_price_list_config", indexes = {@Index(name = "cls_price_list_config_n1",  columnList="price_list")})
@org.hibernate.annotations.Table(appliesTo = "cls_price_list_config", comment = "产品明细配置")
public class PriceListConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "config_id")
    private Long configId;

    @Column(name = "price_list", nullable = false, length = 60)
    @ApiModelProperty(value = "价目表代码", name = "priceList")
    private String priceList;

    @Column(name = "column_name", length = 200)
    @ApiModelProperty(value = "表列名", name = "columnName")
    private String columnName;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "is_readonly", length = 1)
    private String isReadOnly;

    @Column(name = "is_require", length = 1)
    private String isRequire;

    @Column(name = "default_value", length = 200)
    private String defaultValue;

    @Column(name = "eanbled_flag", length = 1)
    private String enabledFlag;
}
