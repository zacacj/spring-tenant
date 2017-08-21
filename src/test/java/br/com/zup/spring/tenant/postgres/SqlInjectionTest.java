package br.com.zup.spring.tenant.postgres;

import br.com.zup.spring.tenant.TenantConfig;
import br.com.zup.spring.tenant.TenantContextHolder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ConfigurationTest.class, TenantConfig.class})
public class SqlInjectionTest extends AbstractTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Test
    public void shouldThrowSQLException() {
        try {
            TenantContextHolder.set("tenant_zup; delete from users;");
            jdbcTemplate.query("select * from users", (rs, i) -> rs.getString(1));
        } catch (RuntimeException e) {
            Assert.assertTrue(e.getCause() instanceof SQLException);
        }
    }
}
