package com.grkn.agents;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan(value = "com.grkn.agents.developer")
@PropertySource("classpath:application.yaml")
public class Developer {

}
