package com.oowanghan.atlantis.framework.web.vo;

import com.oowanghan.atlantis.framework.common.exception.BizException;
import com.oowanghan.atlantis.framework.web.vo.BizResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理
 */
@RestControllerAdvice
public class ExceptionTranslator {

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public BizResponse<Void> handleBindException(BindException ex) {
        return BizResponse.error(HttpStatus.BAD_REQUEST.value(), ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * 通用异常
     * @Author WangHan
     * @Create 3:32 下午 2020/9/18
     * @Param [ex]
     * @Return com.brc.iot.common.web.rest.response.SimpleResponse<java.lang.Void>
     */
    @ExceptionHandler(BizException.class)
    @ResponseBody
    public BizResponse<Void> handleCommonException(BizException ex) {
        return BizResponse.error(ex.getStatus(), ex.getMessage());
    }
}
