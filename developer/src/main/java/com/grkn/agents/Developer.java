package com.grkn.agents;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan(value = "com.grkn.agents.developer")
@PropertySource("classpath:application.yaml")
public class Developer {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext developerContext =
                new AnnotationConfigApplicationContext(Developer.class);
    }
}
