package com.stephen;

import com.stephen.calculator.core.ResponseData;
import com.stephen.exception.ComputeException;
import com.stephen.util.CommonConstUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;

/**
 * @author IvanStephen
 * @project calculator
 * @date 2019/10/28 17:01
 **/
@ControllerAdvice
public class ControllerAdviceProcessor{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseData handleException(HttpServletRequest request, Exception exception){
        String code = CommonConstUtil.UNKNOWN_ERROR;
        String description = null;

        if (exception instanceof HttpMessageNotReadableException) {
            code = CommonConstUtil.PARAM_ERROR;
        } else if (exception instanceof HttpRequestMethodNotSupportedException) {
            code = CommonConstUtil.HTTP_METHOD_NOT_SURPPORT;
        }

        if (exception instanceof ComputeException) {
            ComputeException computeException = (ComputeException) exception;
            description = computeException.getMessage();
            code = computeException.getErrorCode().getCode();

        } else if (exception instanceof AccessDeniedException) {
            description = "无权限访问";
            code = "403";
        }
        if (description == null) {
            description = exception.getMessage();
        }
        ResponseData response = new ResponseData();
        response.setResponseCode(code);
        response.setResponseMessage(description);
        logger.error("code: " + code + "  message: " + description, exception);
        return response;
    }
}
