package br.com.zup.spring.tenant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class TenantDiscoverer {

    @Value("${tenant.query.getTenants}")
    private String queryGetTenants;

    @Value("${tenant.default}")
    private String defaultTenant;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Optional<List<Tenant>> getTenants(String withPrefix) {
        TenantContextHolder.set(new Tenant(defaultTenant));
        String sql = queryGetTenants.replaceAll(":prefix", withPrefix);
        return Optional.ofNullable(jdbcTemplate.query(sql, (rs, rowNum) -> new TenantRowMapper().mapRow(rs, rowNum)));
    }

    public static class TenantRowMapper implements RowMapper<Tenant> {

        @Override
        public Tenant mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Tenant(rs.getString(1));
        }
    }
}
