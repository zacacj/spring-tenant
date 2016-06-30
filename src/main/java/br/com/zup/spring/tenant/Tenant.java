package br.com.zup.spring.tenant;

public class Tenant {
    public final String id;

    public Tenant(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("[Tenant: %s]", id);
    }
}
