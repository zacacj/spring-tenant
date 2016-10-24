package br.com.zup.spring.tenant;

public interface TenantBuilder {
    String get();
    void set(String tenant);
}
