package br.com.zup.spring.tenant;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
@Aspect
public class TenantConnectionPrepareAspect {

    private static final Logger LOG = LoggerFactory.getLogger(TenantConnectionPrepareAspect.class);


    private final String queryChangeTenant;

    public TenantConnectionPrepareAspect(@Value("${tenant.query.changeTenant}") String queryChangeTenant) {
        this.queryChangeTenant = queryChangeTenant;
    }

    @Around("execution(java.sql.Connection javax.sql.DataSource.getConnection())")
    public Object prepareConnection(ProceedingJoinPoint proceedingJoinPoint) {

        try {
            Connection connection = (Connection) proceedingJoinPoint.proceed();
            return prepare(connection);
        } catch (Throwable throwable) {
            LOG.error("Error to prepare tenant connection for slug {}.", TenantContextHolder.get(), throwable);
            throw new RuntimeException(throwable);
        }
    }

    private Connection prepare(Connection connection) throws SQLException {
        LOG.debug("Preparing connection for tenant {}...", TenantContextHolder.get());
        try (PreparedStatement pst = connection.prepareStatement(queryChangeTenant)) {
            pst.setString(1, TenantContextHolder.get());
            pst.executeUpdate();
        }
        return connection;
    }
}
