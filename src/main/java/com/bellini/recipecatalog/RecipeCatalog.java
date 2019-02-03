package com.bellini.recipecatalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:hibernate.properties")
public class RecipeCatalog {

    public static void main(String[] args) {
        SpringApplication.run(RecipeCatalog.class, args);
    }

}
