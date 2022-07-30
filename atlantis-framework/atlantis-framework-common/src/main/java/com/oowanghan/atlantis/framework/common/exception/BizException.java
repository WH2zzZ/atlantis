package com.oowanghan.atlantis.framework.common.exception;

import com.oowanghan.atlantis.framework.common.exception.annotation.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class BizException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(BizException.class);

    private int status = BizErrorCode.SERVER_ERROR;
    private String message;
    private Object data;
    private String moreInfo;

    static {
        Arrays.stream(BizErrorCode.class.getDeclaredFields())
                .forEach(p -> {
                    try {
                        ErrorMessage annotation = p.getAnnotation(ErrorMessage.class);
                        System.setProperty(String.valueOf(p.get("value")), annotation.value());
                    } catch (Exception e) {
                        logger.error("fail to load error code:  " + p.getName());
                    }
                });
    }

    public BizException(int status, Object... msg) {
        String desc = System.getProperty(String.valueOf(status));
        StringBuilder message = new StringBuilder();
        if (desc != null) {
            if (!Arrays.toString(msg).contains(desc)) {
                message.append(desc);
            }
        }
        fillMessage(message, msg);
        this.status = status;
        this.message = message.toString();
    }

    public BizException(String msg) {
        this.status = 2000;
        this.message = msg;
    }

    private void fillMessage(StringBuilder message, Object[] msg) {
        if (msg != null && msg.length > 0) {
            for (Object aMsg : msg) {
                if (aMsg != null) {
                    if (message.length() != 0) {
                        message.append("，").append(aMsg.toString());
                    } else {
                        message.append(aMsg.toString());
                    }
                }
            }
        }
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public BizException() {
    }
}
