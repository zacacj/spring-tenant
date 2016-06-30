# spring-tenant

## Features

* Postgresql multi tenant support
* Oracle multi tenant support

## <a name="quick-start">Quick Start</a>

### Maven

```
<dependency>
  <groupId>br.com.zup</groupId>
  <artifactId>spring-tenant</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### Common properties

Define a prefix for your tenants and add in your spring application.properties:
```
tenant.prefix=my_prefix
```

### Spring JDBC Template

This project has dependency to spring-jdbc, and the bean jdbcTemplate needs to be provided by your spring application.

### Postgresql

Supported without any special configuration.

### Oracle

It's necessary to define the default tenant in your spring application.properties:
```
tenant.default=some_user
```

### Tenant Discoverer

Snippet code to get all tenants by a particular prefix

```Java
 @Value("${tenant.prefix}")
 private String tenantPrefix;
    
 Optional<List<Tenant>> tenants = tenantDiscoverer.getTenants(tenantPrefix);
```

### Tenant Prepare Connection

The main component of solution, it's responsible for intercept all connections from __javax.sql.DataSource.getConnection__ and execute the alter session based from tenant in your context (thread local).
By default this component is already configured.

### Set tenant in your current context
```Java
  Tenant tenant = ;// get tenant from request or database (e.g. TenantDiscoverer)
  TenantContextHolder.set(tenant);
  // run some code for your tenant...
```

