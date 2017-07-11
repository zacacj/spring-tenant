package br.com.zup.spring.tenant.postgres;

import br.com.zup.spring.tenant.TenantConfig;
import br.com.zup.spring.tenant.TenantContextHolder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ConfigurationTest.class, TenantConfig.class})
public class MultiTenantTest extends AbstractTest {

    private static JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        MultiTenantTest.jdbcTemplate = jdbcTemplate;
    }

    @Test
    public void runTests() throws InterruptedException {
        Class[] cls = {ParallelTest.class};
        JUnitCore.runClasses(new ParallelComputer(true, true), cls);
        Assert.assertTrue(ParallelTest.latch.await(2, TimeUnit.SECONDS));
    }

    public static class ParallelTest {

        static final CountDownLatch latch = new CountDownLatch(2);

        @Test
        public void shouldReturnUserTenant1AndZupSlug() {
            TenantContextHolder.set("tenant_zup");
            Optional<List<User>> users = Optional.ofNullable(jdbcTemplate.query("select name from users", (rs, rowNum) -> new UserRowMapper().mapRow(rs, rowNum)));
            Assert.assertTrue(users.isPresent());
            Assert.assertEquals(1, users.get().size());
            Assert.assertEquals("User Tenant 1", users.get().stream().findFirst().get().name);
            latch.countDown();
        }

        @Test
        public void shouldReturnUserTenant2AndZuppSlug() {
            TenantContextHolder.set("tenant_zupp");
            Optional<List<User>> users = Optional.ofNullable(jdbcTemplate.query("select name from users", (rs, rowNum) -> new UserRowMapper().mapRow(rs, rowNum)));
            Assert.assertTrue(users.isPresent());
            Assert.assertEquals(1, users.get().size());
            Assert.assertEquals("User Tenant 2", users.get().stream().findFirst().get().name);
            latch.countDown();
        }
    }

    public static class User {
        public final String name;

        public User(String name) {
            this.name = name;
        }
    }

    public static class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(rs.getString(1));
        }
    }


}
