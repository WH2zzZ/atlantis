package com.oowanghan.atlantis.framework.mysql.liqiubase;

import java.util.concurrent.Executor;

import liquibase.exception.LiquibaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.liquibase.DataSourceClosingSpringLiquibase;
import org.springframework.core.env.Environment;
import org.springframework.util.StopWatch;

public class AsyncSpringLiquibase extends DataSourceClosingSpringLiquibase {

    public static final String DISABLED_MESSAGE = "Liquibase is disabled";
    public static final String STARTING_ASYNC_MESSAGE = "Starting Liquibase async";
    public static final String STARTING_SYNC_MESSAGE = "Starting Liquibase sync";
    public static final String STARTED_MESSAGE = "Liquibase has updated your database in {} ms";
    public static final String EXCEPTION_MESSAGE =
            "Liquibase could not start correctly, your database is NOT ready: " + "{}";

    public static final long SLOWNESS_THRESHOLD = 5; // seconds
    public static final String SLOWNESS_MESSAGE = "Warning, Liquibase took more than {} seconds to start up!";

    private final Logger logger = LoggerFactory.getLogger(AsyncSpringLiquibase.class);

    private final Executor executor;

    private final Environment env;

    public AsyncSpringLiquibase(Executor executor, Environment env) {
        this.executor = executor;
        this.env = env;
    }

    @Override
    public void afterPropertiesSet() throws LiquibaseException {
        logger.debug(STARTING_SYNC_MESSAGE);
        initDb();
    }

    protected void initDb() throws LiquibaseException {
        StopWatch watch = new StopWatch();
        watch.start();
        super.afterPropertiesSet();
        watch.stop();
        logger.debug(STARTED_MESSAGE, watch.getTotalTimeMillis());
        if (watch.getTotalTimeMillis() > SLOWNESS_THRESHOLD * 1000L) {
            logger.warn(SLOWNESS_MESSAGE, SLOWNESS_THRESHOLD);
        }
    }
}