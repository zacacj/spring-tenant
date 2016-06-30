package br.com.zup.spring.tenant;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
@Aspect
public class TenantConnectionPrepareAspect {

    private static final Logger LOG = LogManager.getLogger(TenantConnectionPrepareAspect.class.getName());

    @Value("${tenant.query.changeTenant}")
    private String queryChangeTenant;

    @Around("execution(java.sql.Connection javax.sql.DataSource.getConnection())")
    public Object prepareConnection(ProceedingJoinPoint proceedingJoinPoint) {
        LOG.debug("Preparing connection for {}", TenantContextHolder.get());
        try {
            Connection connection = (Connection) proceedingJoinPoint.proceed();
            return prepare(connection);
        } catch (Throwable throwable) {
            LOG.error("Error to set {} on connection.", TenantContextHolder.get(), throwable);
            throw new RuntimeException(throwable);
        }
    }

    private Connection prepare(Connection connection) throws SQLException {
        String sql = queryChangeTenant.replaceAll(":tenant", TenantContextHolder.get().id);
        Statement st = connection.createStatement();
        st.execute(sql);
        st.close();
        return connection;
    }
}
