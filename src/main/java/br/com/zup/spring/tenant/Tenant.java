package br.com.zup.spring.tenant;

public class Tenant {
    public final String id;

    /**
     * By convention your tenant have to be formed with prefix + _ + organization.
     * Example: rw_zup (rw: prefix, zup: organization)
     */
    public Tenant(String id) {
        this.id = id;
    }


    public String organization() {
        return id.substring(id.indexOf("_") + 1);
    }

    @Override
    public String toString() {
        return String.format("[Tenant: %s]", id);
    }
}
