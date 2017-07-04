package br.com.zup.spring.tenant;

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
public class SqlInjectionTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Test
    public void shouldThrowSQLException() {
        try {
            TenantContextHolder.set("TENANT_ZUP; delete from TENANT_ZUP.users;");
            jdbcTemplate.query("select * from users", (rs, i) -> rs.getString(1));
        } catch (RuntimeException e) {
            Assert.assertTrue(e.getCause() instanceof SQLException);
        }
    }
}
