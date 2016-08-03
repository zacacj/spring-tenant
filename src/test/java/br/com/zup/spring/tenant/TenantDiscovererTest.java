package br.com.zup.spring.tenant;


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
public class TenantDiscovererTest {

    @Autowired
    private TenantDiscoverer tenantDiscoverer;


    @Test
    public void testGetTenantsWithPrefixShouldReturnListOfTenants() {
        Optional<List<String>> tenants = tenantDiscoverer.getTenants("TENANT");
        Assert.assertTrue(tenants.isPresent());
        Assert.assertEquals(2, tenants.get().size());
    }

}
