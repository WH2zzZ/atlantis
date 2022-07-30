package com.oowanghan.atlantis.framework.common.exception;


import com.oowanghan.atlantis.framework.common.exception.annotation.ErrorMessage;

/**
 * 业务错误码
 */
public class BizErrorCode {

    @ErrorMessage("未授权访问")
    public static final int AUTH_ERROR = 401;

    @ErrorMessage("服务错误")
    public static final int SERVER_ERROR = 5000;
    @ErrorMessage("系统繁忙")
    public static final int SYSTEM_BUSY = 5001;
    @ErrorMessage("短信发送失败")
    public static final int SEND_SMS_ERROR = 5002;
    @ErrorMessage("消息通知发送失败")
    public static final int SEND_JPUSH_NOTICE_ERROR = 5003;

    @ErrorMessage("未知错误")
    public static final int UNKNOWN = 2000;
    @ErrorMessage("请勿重复提交请求")
    public static final int REPEAT_REQUEST = 2001;
    @ErrorMessage("参数有误")
    public static final int PARAM_ERROR = 2002;
    @ErrorMessage("验证码已过期")
    public static final int VERIFY_CODE_EXPIRE = 2003;
    @ErrorMessage("验证码错误，请重试")
    public static final int VERIFY_CODE_ERROR = 2004;
    @ErrorMessage("账户未注册")
    public static final int UNREGISTER_ERROR = 2005;
    @ErrorMessage("账户或密码错误")
    public static final int PASSWORD_ERROR = 2006;
    @ErrorMessage("账户已注册，请勿重复注册")
    public static final int REREGISTER_ERROR = 2008;
    @ErrorMessage("文件保存失败")
    public static final int FILE_SAVE_ERROR = 2009;
    @ErrorMessage("文件类型错误")
    public static final int FILE_TYPE_ERROR = 2010;
    @ErrorMessage("文件读取失败")
    public static final int FILE_READ_ERROR = 2011;
}
