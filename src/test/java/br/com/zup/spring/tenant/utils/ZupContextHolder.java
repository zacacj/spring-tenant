package br.com.zup.spring.tenant.utils;


public class ZupContextHolder {

    private static final ThreadLocal<ZupContext> CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    public static ThreadLocal<ZupContext> getContextHolder() {
        return CONTEXT_THREAD_LOCAL;
    }

    public static ZupContext getContext() {
        ZupContext zupContext = CONTEXT_THREAD_LOCAL.get();
        if (zupContext == null) {
            zupContext = new ZupContext();
            CONTEXT_THREAD_LOCAL.set(zupContext);
        }
        return zupContext;
    }

    public static void resetContext() {
        CONTEXT_THREAD_LOCAL.remove();
    }

    public static boolean isEmpty() {
        return CONTEXT_THREAD_LOCAL.get() == null;
    }

    public static class ZupContext {

        private String tracking;
        private String organization;
        private String application;

        public String getTracking() {
            return tracking;
        }

        public void setTracking(String tracking) {
            this.tracking = tracking;
        }

        public String getOrganization() {
            return organization;
        }

        public void setOrganization(String organization) {
            this.organization = organization;
        }

        public String getApplication() {
            return application;
        }

        public void setApplication(String application) {
            this.application = application;
        }
    }
}
