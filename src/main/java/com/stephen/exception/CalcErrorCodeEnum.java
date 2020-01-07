package com.stephen.exception;

import org.apache.commons.lang.StringUtils;

/**
 * @author IvanStephen
 * @project calculator
 * @date 2019/10/28 16:17
 **/
public enum CalcErrorCodeEnum implements ErrorCode {
    //请求异常
    PARAM_ERROR("101", "请求参数解析异常"),
    HTTP_METHOD_NOT_SURPPORT("102", "请求方式错误"),

    //计算异常
    COMPUTE_CHECK_FAIL("301", "计算校验不通过"),
    IRR_CASHFLOW_FAIL("302", "IRR现金流不合法"),
    RECOMPUTE_CHECK_FAIL("303", "行校验不通过"),

    //未知异常
    UNKNOWN_ERROR("999", "未知错误，请联系管理员");

    /**
     * 错误码
     */
    private final String code;
    private final String description;

    /**
     * 构造器
     *
     * @param code        错误码
     * @param description 描述
     */
    private CalcErrorCodeEnum(final String code, final String description) {
        this.code = code;
        this.description = description;
    }

    public static CalcErrorCodeEnum getByCode(String code) {
        for (CalcErrorCodeEnum value : CalcErrorCodeEnum.values()) {
            if (StringUtils.equals(code, value.getCode())) {
                return value;
            }
        }
        return UNKNOWN_ERROR;
    }

    /**
     * 枚举是否包含此code
     *
     * @param code 枚举code
     * @return 结果
     */
    public static Boolean contains(String code) {
        for (CalcErrorCodeEnum value : CalcErrorCodeEnum.values()) {
            if (StringUtils.equals(code, value.getCode())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
