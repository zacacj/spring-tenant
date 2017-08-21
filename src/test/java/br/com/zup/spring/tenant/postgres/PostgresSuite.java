package br.com.zup.spring.tenant.postgres;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.io.IOException;

@RunWith(Suite.class)
@Suite.SuiteClasses({MultiTenantTest.class,
        SqlInjectionTest.class,
        TenantDiscovererTest.class})
public class PostgresSuite {

    static EmbeddedPostgres PG;

    @BeforeClass
    public static void beforeClass() throws IOException, InterruptedException {
        PG = EmbeddedPostgres.builder()
                .setPort(5432)
                .start();
    }

    @AfterClass
    public static void down() throws IOException {
        PG.close();
    }
}
