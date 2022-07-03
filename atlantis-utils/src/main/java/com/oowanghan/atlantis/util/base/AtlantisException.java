package com.oowanghan.atlantis.util.base;

import com.oowanghan.atlantis.util.str.StringUtil;

public class AtlantisException extends RuntimeException {
    private static final long serialVersionUID = 8247610319171014183L;

    public AtlantisException(Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    public AtlantisException(String message) {
        super(message);
    }

    public AtlantisException(String messageTemplate, Object... params) {
        super(StringUtil.format(messageTemplate, params));
    }

    public AtlantisException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public AtlantisException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
        super(message, throwable, enableSuppression, writableStackTrace);
    }

    public AtlantisException(Throwable throwable, String messageTemplate, Object... params) {
        super(StringUtil.format(messageTemplate, params), throwable);
    }
}