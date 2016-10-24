package br.com.zup.spring.tenant;


import br.com.zup.spring.tenant.utils.ZupContextHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@PropertySource({"classpath:/test.properties"})
public class ConfigurationTest {

    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase db = builder
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("db/sql/create-db.sql")
                .addScript("db/sql/insert-data.sql")
                .build();
        return db;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertiesResolver() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public static TenantBuilder tenantBuilder(@Value("${tenant.default}") String tenantDefault) {
        return new TenantBuilder() {
            @Override
            public String get() {
                if (ZupContextHolder.isEmpty()) {
                    return tenantDefault;
                }
                ZupContextHolder.ZupContext context = ZupContextHolder.getContext();
                if (StringUtils.isEmpty(context.getApplication()) || StringUtils.isEmpty(context.getOrganization())) {
                    return tenantDefault;
                }
                return context.getApplication() + "_" + context.getOrganization();
            }

            @Override
            public void set(String tenant) {
                if (Objects.equals(tenant, tenantDefault)) {
                    ZupContextHolder.resetContext();
                    return;
                }
                String[] split = tenant.split("_");
                ZupContextHolder.ZupContext context = ZupContextHolder.getContext();
                if (split.length > 0) {
                    context.setApplication(split[0]);
                }
                if (split.length > 1) {
                    context.setOrganization(split[1]);
                }
            }
        };
    }

}
