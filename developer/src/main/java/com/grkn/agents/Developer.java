package com.grkn.agents;

import com.grkn.agents.developer.core.DeveloperAgentOrchestrator;
import com.grkn.agents.developer.resource.AgentResult;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan(value = "com.grkn.agents.developer")
@PropertySource("classpath:application.yaml")
public class Developer {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext developerContext =
                new AnnotationConfigApplicationContext(Developer.class);

        DeveloperAgentOrchestrator bean = developerContext.getBean(DeveloperAgentOrchestrator.class);
        AgentResult result = bean.run("C:\\Users\\PC\\Documents\\repo\\AgenticAIExample","Can you write detailed README.md file inside repository. Rule: File should be easy to understand for developers","Update README.md");

    }
}
