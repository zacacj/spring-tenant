package br.com.zup.spring.tenant;

import org.springframework.context.annotation.*;

@EnableAspectJAutoProxy
@Configuration
@ComponentScan("br.com.zup.spring.tenant")
public class TenantConfig {

    @Configuration
    @Profile("postgresql")
    @PropertySource({"classpath:tenant-postgresql.properties"})
    static class Postgresql {
    }

    @Configuration
    @Profile("oracle")
    @PropertySource({"classpath:tenant-oracle.properties"})
    static class Oracle {
    }
}
