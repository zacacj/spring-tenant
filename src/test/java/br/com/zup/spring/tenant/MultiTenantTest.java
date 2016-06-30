package br.com.zup.spring.tenant;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ConfigurationTest.class, TenantConfig.class})
public class MultiTenantTest {

    @Autowired
    private static JdbcTemplate jdbcTemplate;

    @Test
    public void runTests() {
        Class[] cls={ParallelTest.class };
        JUnitCore.runClasses(new ParallelComputer(true, true), cls);
    }

    public static class ParallelTest {
        @Test
        public void testGetUserOfTenant1ShouldReturnUserTenant1() {
            TenantContextHolder.set(new Tenant("TENANT_1"));
            Optional<List<User>> users = Optional.ofNullable(jdbcTemplate.query("select name from users", (rs, rowNum) -> new UserRowMapper().mapRow(rs, rowNum)));
            Assert.assertTrue(users.isPresent());
            Assert.assertEquals(1, users.get().size());
            Assert.assertEquals("User Tenant 1", users.get().stream().findFirst().get().name);
        }

        @Test
        public void testGetUserOfTenant2ShouldReturnUserTenant2() {
            TenantContextHolder.set(new Tenant("TENANT_2"));
            Optional<List<User>> users = Optional.ofNullable(jdbcTemplate.query("select name from users", (rs, rowNum) -> new UserRowMapper().mapRow(rs, rowNum)));
            Assert.assertTrue(users.isPresent());
            Assert.assertEquals(1, users.get().size());
            Assert.assertEquals("User Tenant 2", users.get().stream().findFirst().get().name);
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
