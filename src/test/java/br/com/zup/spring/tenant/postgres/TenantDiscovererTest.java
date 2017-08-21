package br.com.zup.spring.tenant.postgres;


import br.com.zup.spring.tenant.TenantConfig;
import br.com.zup.spring.tenant.TenantDiscoverer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ConfigurationTest.class, TenantConfig.class})
public class TenantDiscovererTest extends AbstractTest {

    @Autowired
    private TenantDiscoverer tenantDiscoverer;

    @Test
    public void shouldReturnListOfTenants() {
        Optional<List<String>> tenants = tenantDiscoverer.getTenants("tenant");
        Assert.assertTrue(tenants.isPresent());
        Assert.assertEquals(2, tenants.get().size());
    }

}
