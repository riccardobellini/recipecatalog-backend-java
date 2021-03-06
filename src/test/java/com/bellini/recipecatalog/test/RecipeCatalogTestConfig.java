package com.bellini.recipecatalog.test;

import javax.sql.DataSource;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootConfiguration
@ComponentScan(basePackages = {
        "com.bellini.recipecatalog.dao.v1.dishtype",
        "com.bellini.recipecatalog.dao.v1.ingredient",
        "com.bellini.recipecatalog.dao.v1.book",
        "com.bellini.recipecatalog.dao.v1.publication",
        "com.bellini.recipecatalog.dao.v1.recipe",
        "com.bellini.recipecatalog.service.v1.book",
        "com.bellini.recipecatalog.service.v1.dishtype",
        "com.bellini.recipecatalog.service.v1.ingredient",
        "com.bellini.recipecatalog.service.v1.publication",
        "com.bellini.recipecatalog.service.v1.recipe" })
@EnableAutoConfiguration
public class RecipeCatalogTestConfig {

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource ds) {
        return new JdbcTemplate(ds);
    }

}
