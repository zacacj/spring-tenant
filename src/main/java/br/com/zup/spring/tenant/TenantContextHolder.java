package br.com.zup.spring.tenant;

public class TenantContextHolder {

    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void set(String tenant) {
        threadLocal.set(tenant);
    }

    public static void unset() {
        threadLocal.remove();
    }

    public static String get() {
        return threadLocal.get();
    }

    public static ThreadLocal<String> getThreadLocal() {
        return threadLocal;
    }
}