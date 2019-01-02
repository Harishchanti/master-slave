package com.jpa.example.masterslave.config;

import com.jpa.example.masterslave.aop.DbType;
import com.jpa.example.masterslave.aop.RoutingDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.jpa.example.masterslave.repositories", entityManagerFactoryRef = "EM", transactionManagerRef = "TM")
public class DatabaseConfiguration {

    private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String PROPERTY_NAME_HIBERNATE_FORMAT_SQL = "hibernate.format_sql";
    /*
     * A common way to auto-create a database schema in a Spring Boot JPA application is just
     * to set a spring.jpa.hibernate.ddl-auto property to create, create-drop or update.
     */
    private static final String PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";

    private static final String PROPERTY_NAME_HIBERNATE_NAMING_STRATEGY = "hibernate.ejb.naming_strategy";
    private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private static final String PROPERTY_NAME_HIBERNATE_LAZY_LOAD = "hibernate.enable_lazy_load_no_trans";


    private static final String PROPERTY_NAME_HIBERNATE_ENVERS_AUDIT_TABLE_SUFFIX = "org.hibernate.envers.audit_table_suffix";
    private static final String PROPERTY_NAME_HIBERNATE_ENVERS_REVISION_FIELD_NAME = "org.hibernate.envers.revision_field_name";
    private static final String PROPERTY_NAME_HIBERNATE_ENVERS_REVISION_TYPE_FIELD_NAME = "org.hibernate.envers.revision_type_field_name";
    private static final String PROPERTY_NAME_HIBERNATE_ENVERS_REVISION_ON_COLLECTION_CHANGE = "org.hibernate.envers.revision_on_collection_change";
    private static final String PROPERTY_NAME_HIBERNATE_ENVERS_AUDIT_STRATEGY = "org.hibernate.envers.audit_strategy";
    private static final String PROPERTY_NAME_HIBERNATE_ENVERS_AUDIT_STRATEGY_VALIDITY_END_REV_FIELD_NAME = "org.hibernate.envers.audit_strategy_validity_end_rev_field_name";
    private static final String PROPERTY_NAME_HIBERNATE_ENVERS_AUDIT_STRATEGY_VALIDITY_STORE_REVEND_TIMESTAMP = "org.hibernate.envers.audit_strategy_validity_store_revend_timestamp";
    private static final String PROPERTY_NAME_HIBERNATE_ENVERS_AUDIT_STRATEGY_VALIDITY_REVEND_TIMESTAMP_FIELD_NAME = "org.hibernate.envers.audit_strategy_validity_revend_timestamp_field_name";


    private static final String PROPERTY_PACKAGES_TO_SCAN = "com.jpa.example.masterslave.model";

    private static final String MASTER_PROPERTY_NAME_DATABASE_DRIVER = "datasource.primary.driverclassname";
    private static final String MASTER_PROPERTY_NAME_DATABASE_PASSWORD = "datasource.primary.password";
    private static final String MASTER_PROPERTY_NAME_DATABASE_USERNAME = "datasource.primary.username";
    private static final String MASTER_PROPERTY_NAME_DATABASE_URL = "datasource.primary.url";

    protected static final String MASTER_PROPERTY_NAME_DATABASE_MIN_CONNECTIONS = "datasource.primary.minconnections";
    protected static final String MASTER_PROPERTY_NAME_DATABASE_MAX_PARTITIONS = "datasource.primary.maxpartitions";
    protected static final String MASTER_PROPERTY_NAME_DATABASE_MAX_LIFETIME = "datasource.primary.maxlifetimeinmillis";
    protected static final String MASTER_PROPERTY_NAME_DATABASE_CONNECTION_TIMEOUT = "datasource.primary.connectiontimeoutinmillis";


    private static final String SLAVE_PROPERTY_NAME_DATABASE_DRIVER = "datasource.secondary.driverclassname";
    private static final String SLAVE_PROPERTY_NAME_DATABASE_PASSWORD = "datasource.secondary.password";
    private static final String SLAVE_PROPERTY_NAME_DATABASE_USERNAME = "datasource.secondary.username";
    private static final String SLAVE_PROPERTY_NAME_DATABASE_URL = "datasource.secondary.url";

    protected static final String SLAVE_PROPERTY_NAME_DATABASE_MIN_CONNECTIONS = "datasource.secondary.minconnections";
    protected static final String SLAVE_PROPERTY_NAME_DATABASE_MAX_PARTITIONS = "datasource.secondary.maxpartitions";
    protected static final String SLAVE_PROPERTY_NAME_DATABASE_MAX_LIFETIME = "datasource.secondary.maxlifetimeinmillis";
    protected static final String SLAVE_PROPERTY_NAME_DATABASE_CONNECTION_TIMEOUT = "datasource.secondary.connectiontimeoutinmillis";

    @Autowired
    private Environment environment;


    @Bean(name = "masterDataStore")
    public DataSource masterDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty(MASTER_PROPERTY_NAME_DATABASE_DRIVER));
        dataSource.setJdbcUrl(environment.getRequiredProperty(MASTER_PROPERTY_NAME_DATABASE_URL));
        dataSource.setUsername(environment.getRequiredProperty(MASTER_PROPERTY_NAME_DATABASE_USERNAME));
        dataSource.setPassword(environment.getRequiredProperty(MASTER_PROPERTY_NAME_DATABASE_PASSWORD));
        dataSource.setConnectionTestQuery("SELECT 1");
        dataSource.setMaximumPoolSize(Integer.parseInt(environment.getProperty(MASTER_PROPERTY_NAME_DATABASE_MAX_PARTITIONS)));
        dataSource.setMaxLifetime(Long.parseLong(environment.getProperty(MASTER_PROPERTY_NAME_DATABASE_MAX_LIFETIME)));
        dataSource.setConnectionTimeout(Long.parseLong(environment.getProperty(MASTER_PROPERTY_NAME_DATABASE_CONNECTION_TIMEOUT)));

        return dataSource;
    }

    @Bean(name = "slaveDataStore")
    public DataSource slaveDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty(SLAVE_PROPERTY_NAME_DATABASE_DRIVER));
        dataSource.setJdbcUrl(environment.getRequiredProperty(SLAVE_PROPERTY_NAME_DATABASE_URL));
        dataSource.setUsername(environment.getRequiredProperty(SLAVE_PROPERTY_NAME_DATABASE_USERNAME));
        dataSource.setPassword(environment.getRequiredProperty(SLAVE_PROPERTY_NAME_DATABASE_PASSWORD));
        dataSource.setConnectionTestQuery("SELECT 1");
        dataSource.setMaximumPoolSize(Integer.parseInt(environment.getProperty(SLAVE_PROPERTY_NAME_DATABASE_MAX_PARTITIONS)));
        dataSource.setMaxLifetime(Long.parseLong(environment.getProperty(SLAVE_PROPERTY_NAME_DATABASE_MAX_LIFETIME)));
        dataSource.setConnectionTimeout(Long.parseLong(environment.getProperty(SLAVE_PROPERTY_NAME_DATABASE_CONNECTION_TIMEOUT)));
        return dataSource;
    }


    @Bean
    @DependsOn(value = {"masterDataStore", "slaveDataStore"})
    public DataSource routingDataSource() {
        RoutingDataSource routingDataSource = new RoutingDataSource();
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(DbType.MASTER, masterDataSource());
        dataSourceMap.put(DbType.SLAVE, slaveDataSource());
        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(dataSourceMap.get(DbType.MASTER));
        return routingDataSource;
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        return new LazyConnectionDataSourceProxy(routingDataSource());
    }


    @Bean(name = "TM")
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setDataSource(dataSource());
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }


    @Bean(name = "EM")
    @DependsOn("masterDataStore")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setPackagesToScan(PROPERTY_PACKAGES_TO_SCAN);

        Properties jpaProperties = new Properties();
        jpaProperties.put(PROPERTY_NAME_HIBERNATE_DIALECT, environment.getRequiredProperty(PROPERTY_NAME_HIBERNATE_DIALECT));
        jpaProperties.put(PROPERTY_NAME_HIBERNATE_FORMAT_SQL, environment.getRequiredProperty(PROPERTY_NAME_HIBERNATE_FORMAT_SQL));
        jpaProperties.put(PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO, environment.getRequiredProperty(PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO));
        jpaProperties.put(PROPERTY_NAME_HIBERNATE_NAMING_STRATEGY, environment.getRequiredProperty(PROPERTY_NAME_HIBERNATE_NAMING_STRATEGY));
        jpaProperties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL, environment.getRequiredProperty(PROPERTY_NAME_HIBERNATE_SHOW_SQL));
        jpaProperties.put(PROPERTY_NAME_HIBERNATE_LAZY_LOAD, environment.getRequiredProperty(PROPERTY_NAME_HIBERNATE_LAZY_LOAD));

        jpaProperties.put(PROPERTY_NAME_HIBERNATE_ENVERS_AUDIT_TABLE_SUFFIX, environment.getRequiredProperty(PROPERTY_NAME_HIBERNATE_ENVERS_AUDIT_TABLE_SUFFIX));
        jpaProperties.put(PROPERTY_NAME_HIBERNATE_ENVERS_REVISION_FIELD_NAME, environment.getRequiredProperty(PROPERTY_NAME_HIBERNATE_ENVERS_REVISION_FIELD_NAME));
        jpaProperties.put(PROPERTY_NAME_HIBERNATE_ENVERS_REVISION_TYPE_FIELD_NAME, environment.getRequiredProperty(PROPERTY_NAME_HIBERNATE_ENVERS_REVISION_TYPE_FIELD_NAME));
        jpaProperties.put(PROPERTY_NAME_HIBERNATE_ENVERS_REVISION_ON_COLLECTION_CHANGE, environment.getRequiredProperty(PROPERTY_NAME_HIBERNATE_ENVERS_REVISION_ON_COLLECTION_CHANGE));
        jpaProperties.put(PROPERTY_NAME_HIBERNATE_ENVERS_AUDIT_STRATEGY, environment.getRequiredProperty(PROPERTY_NAME_HIBERNATE_ENVERS_AUDIT_STRATEGY));
        jpaProperties.put(PROPERTY_NAME_HIBERNATE_ENVERS_AUDIT_STRATEGY_VALIDITY_END_REV_FIELD_NAME, environment.getRequiredProperty(PROPERTY_NAME_HIBERNATE_ENVERS_AUDIT_STRATEGY_VALIDITY_END_REV_FIELD_NAME));
        jpaProperties.put(PROPERTY_NAME_HIBERNATE_ENVERS_AUDIT_STRATEGY_VALIDITY_STORE_REVEND_TIMESTAMP, environment.getRequiredProperty(PROPERTY_NAME_HIBERNATE_ENVERS_AUDIT_STRATEGY_VALIDITY_STORE_REVEND_TIMESTAMP));
        jpaProperties.put(PROPERTY_NAME_HIBERNATE_ENVERS_AUDIT_STRATEGY_VALIDITY_REVEND_TIMESTAMP_FIELD_NAME, environment.getRequiredProperty(PROPERTY_NAME_HIBERNATE_ENVERS_AUDIT_STRATEGY_VALIDITY_REVEND_TIMESTAMP_FIELD_NAME));

        entityManagerFactoryBean.setJpaProperties(jpaProperties);
        return entityManagerFactoryBean;
    }
}

