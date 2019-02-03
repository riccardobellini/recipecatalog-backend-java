package com.bellini.recipecatalog.config;

import java.util.Properties;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class RecipeCatalogConfig {

//    @Bean
//    DataSource dataSource() {
//        JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
//        DataSource dataSource = dsLookup.getDataSource("jdbc/RecipeCatalog");
//        return dataSource;
//    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, Environment env)
            throws NamingException {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setPackagesToScan("com.bellini.recipecatalog.model");

        Properties additionalProperties = new Properties();
        additionalProperties.put("hibernate.dialect",
                env.getProperty("hibernate.dialect"));

        em.setJpaProperties(additionalProperties);

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

}
