package com.stephen.exception;

import lombok.Data;

/**
 * @author IvanStephen
 * @project calculator
 * @date 2019/10/28 16:01
 **/
@Data
public class ComputeException extends RuntimeException {
    private static final long serialVersionUID = -5152329653041638204L;

    protected final ErrorCode errorCode;

    private String code;

    public ComputeException() {
        super(CalcErrorCodeEnum.UNKNOWN_ERROR.getDescription());
        this.errorCode = CalcErrorCodeEnum.UNKNOWN_ERROR;
    }

    /**
     * 指定错误码通用异常
     *
     * @param errorCode
     */
    public ComputeException(final ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }

    /**
     * 自定义消息通用异常
     *
     * @param description
     */
    public ComputeException(final String description) {
        super(description);
        this.errorCode = CalcErrorCodeEnum.UNKNOWN_ERROR;
    }

    /**
     * 指定导火索构造通用异常
     *
     * @param throwable
     */
    public ComputeException(final Throwable throwable) {
        super(throwable);
        this.errorCode = CalcErrorCodeEnum.UNKNOWN_ERROR;
    }

    /**
     * 构造通用异常
     *
     * @param errorCode   错误码
     * @param description 详细描述
     */
    public ComputeException(final ErrorCode errorCode, final String description) {
        super(description);
        this.errorCode = errorCode;
    }

    /**
     * 构造通用异常
     *
     * @param errorCode 错误码
     * @param throwable 导火索
     */
    public ComputeException(final ErrorCode errorCode, final Throwable throwable) {
        super(errorCode.getDescription(), throwable);
        this.errorCode = errorCode;
    }

    /**
     * 构造通用异常
     *
     * @param description 详细描述
     * @param throwable   导火索
     */
    public ComputeException(final String description, final Throwable throwable) {
        super(description, throwable);
        this.errorCode = CalcErrorCodeEnum.UNKNOWN_ERROR;
    }

    /**
     * 构造通用异常
     *
     * @param errorCode   错误码
     * @param description 详细描述
     * @param throwable   导火索
     */
    public ComputeException(final ErrorCode errorCode, final String description,
                            final Throwable throwable) {
        super(description, throwable);
        this.errorCode = errorCode;
    }

    /**
     * Getter method for property <tt>errorCode</tt>.
     *
     * @return property value of errorCode
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
