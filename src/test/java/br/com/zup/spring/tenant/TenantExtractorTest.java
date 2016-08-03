package br.com.zup.spring.tenant;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ConfigurationTest.class, TenantConfig.class})
public class TenantExtractorTest {

    @Before
    public void before() {
        TenantContextHolder.setTenantExtractor(null);
    }

    @Test(expected = TenantExtractor.TenantExtractorNotFoundException.class)
    public void noSlugExtractorProvidedShouldThrowRuntimeException() {

        TenantContextHolder.set("zup");
        Assert.assertEquals("tenant_zup", TenantContextHolder.tenant());
    }

    @Test
    public void slugExtractorProvidedShouldReturnSlug() {

        TenantContextHolder.setTenantExtractor(tenant -> "tenant_".concat(tenant));
        TenantContextHolder.set("zup");
        Assert.assertEquals("tenant_zup", TenantContextHolder.tenant());
    }

}

