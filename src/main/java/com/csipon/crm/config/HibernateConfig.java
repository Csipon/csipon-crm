package com.csipon.crm.config;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

import static org.hibernate.cfg.AvailableSettings.*;

@Configuration
@EnableTransactionManagement
public class HibernateConfig {
    private static final Logger LOG = LoggerFactory.getLogger(HibernateConfig.class);

    private static final String HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private static final String HIBERNATE_BATCH_SIZE = "hibernate.batch-size";
    private static final String HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl-auto";
    private static final String HIBERNATE_CURRENT_SESSION_CONTEXT_CLASS = "hibernate.current-session-context-class";

    @Resource
    private Environment env;

    private DataSource dataSource;

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        return txManager;
    }

    @Bean
    public LocalSessionFactoryBean getSessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("com.csipon.crm");
        sessionFactory.setHibernateProperties(getHibernateProperties());
        return sessionFactory;
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put(DIALECT, env.getRequiredProperty(HIBERNATE_DIALECT));
        properties.put(SHOW_SQL, env.getRequiredProperty(HIBERNATE_SHOW_SQL));
        properties.put(STATEMENT_BATCH_SIZE, env.getRequiredProperty(HIBERNATE_BATCH_SIZE));
        properties.put(HBM2DDL_AUTO, env.getRequiredProperty(HIBERNATE_HBM2DDL_AUTO));
        properties.put(CURRENT_SESSION_CONTEXT_CLASS, env.getRequiredProperty(HIBERNATE_CURRENT_SESSION_CONTEXT_CLASS));
        return properties;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
