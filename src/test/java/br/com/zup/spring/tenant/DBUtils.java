package br.com.zup.spring.tenant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Arrays;

@Component
public class DBUtils {

    private final DataSource dataSource;

    @Autowired
    public DBUtils(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void execute(String... scripts) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        Arrays.stream(scripts).forEach(script -> populator.addScript(new ClassPathResource(script)));
        Connection connection = DataSourceUtils.getConnection(dataSource);
        populator.populate(connection);
        DataSourceUtils.releaseConnection(connection, dataSource);
    }
}