package com.oowanghan.atlantis.framework.orm.liqiubase;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.DataSourceClosingSpringLiquibase;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

/**
 * 工厂类
 *
 * @Author WangHan
 * @Create 11:39 2022/7/22
 */
public final class SpringLiquibaseFactory {

    private SpringLiquibaseFactory() {
    }

    public static SpringLiquibase createSpringLiquibase(DataSource liquibaseDatasource,
                                                        LiquibaseProperties liquibaseProperties,
                                                        DataSource dataSource,
                                                        DataSourceProperties dataSourceProperties) {
        SpringLiquibase liquibase;
        DataSource liquibaseDataSource = getDataSource(liquibaseDatasource, liquibaseProperties, dataSource);
        if (liquibaseDataSource != null) {
            liquibase = new SpringLiquibase();
            liquibase.setDataSource(liquibaseDataSource);
            liquibase.setChangeLog(liquibaseProperties.getChangeLog());
            liquibase.setContexts(liquibaseProperties.getContexts());
            liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
            liquibase.setDropFirst(liquibaseProperties.isDropFirst());
            liquibase.setChangeLogParameters(liquibaseProperties.getParameters());
            liquibase.setShouldRun(liquibaseProperties.isEnabled());
            return liquibase;
        }
        liquibase = new DataSourceClosingSpringLiquibase();
        liquibase.setDataSource(createNewDataSource(liquibaseProperties, dataSourceProperties));
        return liquibase;
    }

    public static AsyncSpringLiquibase createAsyncSpringLiquibase(Environment env,
                                                                  Executor executor,
                                                                  DataSource liquibaseDatasource,
                                                                  LiquibaseProperties liquibaseProperties,
                                                                  DataSource dataSource,
                                                                  DataSourceProperties dataSourceProperties) {
        AsyncSpringLiquibase liquibase = new AsyncSpringLiquibase(executor, env);
        DataSource liquibaseDataSource = getDataSource(liquibaseDatasource, liquibaseProperties, dataSource);
        if (liquibaseDataSource != null) {
            liquibase.setCloseDataSourceOnceMigrated(false);
            liquibase.setDataSource(liquibaseDataSource);
        } else {
            liquibase.setDataSource(createNewDataSource(liquibaseProperties, dataSourceProperties));
        }
        return liquibase;
    }

    private static DataSource getDataSource(DataSource liquibaseDataSource, LiquibaseProperties liquibaseProperties, DataSource dataSource) {
        if (liquibaseDataSource != null) {
            return liquibaseDataSource;
        }
        if (liquibaseProperties.getUrl() == null && liquibaseProperties.getUser() == null) {
            return dataSource;
        }
        return null;
    }

    private static DataSource createNewDataSource(LiquibaseProperties liquibaseProperties, DataSourceProperties dataSourceProperties) {
        String url = getProperty(liquibaseProperties::getUrl, dataSourceProperties::determineUrl);
        String user = getProperty(liquibaseProperties::getUser, dataSourceProperties::determineUsername);
        String password = getProperty(liquibaseProperties::getPassword, dataSourceProperties::determinePassword);
        return DataSourceBuilder.create().url(url).username(user).password(password).build();
    }

    private static String getProperty(Supplier<String> property, Supplier<String> defaultValue) {
        return Optional.of(property).map(Supplier::get).orElseGet(defaultValue);
    }

}
