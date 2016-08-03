package br.com.zup.spring.tenant;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@Component
@Aspect
public class TenantConnectionPrepareAspect {

    private static final Logger LOG = LoggerFactory.getLogger(TenantConnectionPrepareAspect.class);

    @Value("${tenant.query.changeTenant}")
    private String queryChangeTenant;

    @Around("execution(java.sql.Connection javax.sql.DataSource.getConnection())")
    public Object prepareConnection(ProceedingJoinPoint proceedingJoinPoint) {

        try {
            Connection connection = (Connection) proceedingJoinPoint.proceed();
            return prepare(connection);

        } catch (Throwable throwable) {
            LOG.error("Error to prepare tenant connection for slug {}.", TenantContextHolder.slug(), throwable);
            throw new RuntimeException(throwable);
        }
    }

    private Connection prepare(Connection connection) throws SQLException {

        LOG.debug("Preparing connection for tenant {}...", TenantContextHolder.tenant());
        Optional<Statement> statementOptional = Optional.empty();
        try {
            String sql = queryChangeTenant.replaceAll(":tenant", TenantContextHolder.tenant());
            statementOptional = Optional.ofNullable(connection.createStatement());
            if (statementOptional.isPresent()) {
                statementOptional.get().execute(sql);
            }
        } finally {
            if (statementOptional.isPresent()) {
                statementOptional.get().close();
            }
        }
        return connection;
    }
}
