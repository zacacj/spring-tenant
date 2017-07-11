package br.com.zup.spring.tenant.postgres;

import br.com.zup.spring.tenant.DBUtils;
import br.com.zup.spring.tenant.TenantContextHolder;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractTest {

    @Autowired
    DBUtils dbUtils;

    @Before
    public void before() {
        TenantContextHolder.set("public");
        dbUtils.execute("create-db.sql", "insert-data.sql");
    }
}
