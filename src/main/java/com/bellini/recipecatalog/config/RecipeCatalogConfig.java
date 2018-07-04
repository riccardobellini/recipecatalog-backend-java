package com.bellini.recipecatalog.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.bellini.recipecatalog")
@PropertySource("classpath:application.properties")
public class RecipeCatalogConfig extends WebMvcConfigurerAdapter {
	
//	@Bean
//    DataSource dataSource() {
//        DataSource dataSource = null;
//        JndiTemplate jndi = new JndiTemplate();
//        try {
//            dataSource = jndi.lookup("java:comp/env/jdbc/RecipeCatalog", DataSource.class);
//        } catch (NamingException e) {
//            e.printStackTrace();
//        }
//        return dataSource;
//    }
	
	@Bean
    DataSource dataSource() {
		JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
	    DataSource dataSource = dsLookup.getDataSource("jdbc/RecipeCatalog");       
	    return dataSource;
	}
	
}
