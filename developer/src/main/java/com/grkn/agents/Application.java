package com.grkn.agents;

import com.grkn.agents.core.AgentResult;
import com.grkn.agents.core.DeveloperAgentOrchestrator;
import com.grkn.agents.properties.OpenAiProperties;
import com.grkn.agents.ui.AgentUiApp;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import java.util.Scanner;

@ComponentScan(value = "com.grkn.agents")
@PropertySource("classpath:application.yaml")
@EnableConfigurationProperties(OpenAiProperties.class)
public class Application {

    public static void main(String[] args) throws Exception {
        AgentUiApp.main(args);
    }
}
