package com.oowanghan.atlantis.framework.web.vo;


import com.oowanghan.atlantis.framework.common.exception.BizException;

import java.io.Serial;
import java.io.Serializable;

public class BizResponse<T> implements Serializable {

    protected static final int SUCCESS_STATUS = 200;
    protected static final String SUCCESS_MESSAGE = "success";
    @Serial
    private static final long serialVersionUID = 6109259661960244534L;

    private T data;

    private String message;

    private int status;

    public static <T> BizResponse<T> success() {
        return success((T) "");
    }

    public static <T> BizResponse<T> success(T data) {
        return success(data, SUCCESS_MESSAGE, "");
    }

    public static <T> BizResponse<T> success(T data, String message, String moreInfo) {
        return success(data, message, SUCCESS_STATUS, moreInfo);
    }

    public static <T> BizResponse<T> success(T data, String message, int responseStatus, String moreInfo) {
        BizResponse<T> response = new BizResponse<>();
        response.setData(data);
        response.setStatus(responseStatus);
        response.setMessage(message);
        return response;
    }

    public static <T> BizResponse<T> error(int responseStatus, String message, String moreInfo) {
        BizResponse<T> response = new BizResponse<>();
        response.setStatus(responseStatus);
        response.setMessage(message);
        return response;
    }

    public static <T> BizResponse<T> error(int responseStatus, String message) {
        return error(responseStatus, message, null);
    }

    public static <T> BizResponse<T> error(int responseStatus) {
        return error(responseStatus, null, null);
    }

    public static <T> void checkResponse(BizResponse<T> response) {
        if (response.getStatus() != SUCCESS_STATUS) {
            throw new BizException(response.getStatus(), response.getMessage());
        }
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
