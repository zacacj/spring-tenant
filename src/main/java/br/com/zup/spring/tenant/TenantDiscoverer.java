package br.com.zup.spring.tenant;

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

    private final String queryGetTenants;
    private final String defaultTenant;
    private final JdbcTemplate jdbcTemplate;

    public TenantDiscoverer(@Value("${tenant.query.getTenants}") String queryGetTenants,
                            @Value("${tenant.default}") String defaultTenant,
                            JdbcTemplate jdbcTemplate) {
        this.queryGetTenants = queryGetTenants;
        this.defaultTenant = defaultTenant;
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<List<String>> getTenants(String withPrefix) {
        TenantContextHolder.set(defaultTenant);
        String prefixParam = withPrefix + "%";
        Object[] params = {prefixParam};
        return Optional.ofNullable(jdbcTemplate.query(queryGetTenants, params, (rs, rowNum) -> new TenantRowMapper().mapRow(rs, rowNum)));
    }

    public static class TenantRowMapper implements RowMapper<String> {

        @Override
        public String mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getString(1);
        }
    }
}
