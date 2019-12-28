package com.stephen.util;

/**
 * @author IvanStephen
 * @project calculator
 * @date 2019/10/28 14:33
 **/
public class CommonConstUtil {
    //启用
    public final static String YES = "Y";
    //禁用
    public final static String NO = "N";

    //付款方式-先付
    public final static String AD_PAY_TYPE = "1";
    //付款方式-后付
    public final static String AT_PAY_TYPE = "0";

    //业务类型-正租
    public final static String BUSINESS_TYPE_LEASE = "LEASE";
    //业务类型-回租
    public final static String BUSINESS_TYPE_LEASEBACK = "LEASE_BACK";

    /**
     * 计算类型
     */
    //等额本息
    public final static String EQUAL_PAYMENT = "EQUAL_PAYMENT";
    //等额本金
    public final static String EQUAL_PRINCIPAL = "EQUAL_PRINCIPAL";
    //先息后本
    public final static String EQUAL_INTEREST = "EQUAL_INTEREST";
    //任意现金流
    public final static String FREE_CASHFLOW = "FREE_CASHFLOW";

    /**
     * 异常消息
     */
    //未知错误
    public final static String UNKNOWN_ERROR = "999";
    //请求参数错误
    public final static String PARAM_ERROR = "101";
    //请求方式错误
    public final static String HTTP_METHOD_NOT_SURPPORT = "102";
    //请求成功
    public final static String RESPONSE_SUCCESS = "200";
    //请求失败
    public final static String RESPONSE_FAILURE = "404";
    //请求错误
    public final static String RESPONSE_ERROR = "500";

    public final static int DAYS_IN_YEAR = 365;
}
