package br.com.zup.spring.tenant;

public class TenantContextHolder {

    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    private static final String ROOT_SLUG = "root_slug_";

    private static TenantExtractor tenantExtractor;

    public static void setRootTenant(String tenant) {
        threadLocal.set(ROOT_SLUG + tenant);
    }

    private static String rootTenant() {
        return slug().replace(ROOT_SLUG, "");
    }

    public static boolean isRootTenant() {
        return slug().startsWith(ROOT_SLUG);
    }

    public static void set(String slug) {
        if (slug.startsWith(ROOT_SLUG)) {
            throw new IllegalArgumentException("Slug doesn't start with " + ROOT_SLUG);
        }
        threadLocal.set(slug);
    }

    public static void unset() {
        threadLocal.remove();
    }

    public static String tenant() {
        if (isRootTenant()) {
            return rootTenant();
        }
        checkTenantExtractor();
        return tenantExtractor.extractFrom(slug());
    }

    public static String slug() {
        return threadLocal.get();
    }

    public static ThreadLocal<String> getThreadLocal() {
        return threadLocal;
    }

    public static void setTenantExtractor(TenantExtractor extractor) {
        tenantExtractor = extractor;
    }

    private static void checkTenantExtractor() {
        if (tenantExtractor == null) {
            throw new TenantExtractor.TenantExtractorNotFoundException("TenantExtractor implementation have to be provided.");
        }
    }
}