package com.stephen.exception;

/**
 * @author IvanStephen
 * @project calculator
 * @date 2019/10/28 16:14
 * @apiNote 接口错误编码
 **/
public interface ErrorCode {
    /**
     * 获取错误码
     * @return
     */
    String getCode();

    /**
     * 获取错误信息
     * @return
     */
    String getDescription();
}
