package com.grkn.agents;

import com.grkn.agents.ui.AgentUiApp;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan(value = "com.grkn.agents")
@PropertySource("classpath:application.yaml")
public class Developer {

    public static void main(String[] args) {
        AgentUiApp.main(args);
    }
}
