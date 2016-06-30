package br.com.zup.spring.tenant;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TenantContextHolder {

    private static final Logger LOG = LogManager.getLogger(TenantConnectionPrepareAspect.class.getName());


    private static final ThreadLocal<Tenant> threadLocal = new ThreadLocal<>();

    public static void set(Tenant tenant) {
        LOG.debug("Tenant:" + tenant + "Thread: " + Thread.currentThread().getName());
        threadLocal.set(tenant);
    }

    public static void unset() {
        threadLocal.remove();
    }

    public static Tenant get() {
        return threadLocal.get();
    }

}