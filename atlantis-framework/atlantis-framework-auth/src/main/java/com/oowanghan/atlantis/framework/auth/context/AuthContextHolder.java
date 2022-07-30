package com.oowanghan.atlantis.framework.auth.context;

/**
 * 鉴权上下文
 */
public class AuthContextHolder {

    private static final ThreadLocal<AuthContext> holder = new ThreadLocal<>();

    public AuthContextHolder() {
    }

    public static AuthContext getContext() {
        AuthContext context = holder.get();
        if (context == null) {
            context = new AuthContext();
            holder.set(context);
        }

        return context;
    }

    public static void setContext(AuthContext context) {
        holder.set(context);
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        holder.remove();
    }
}