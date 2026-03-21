package com.grkn.agents;

import com.grkn.agents.core.AgentResult;
import com.grkn.agents.core.DeveloperAgentOrchestrator;
import com.grkn.agents.properties.OpenAiProperties;
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
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Application.class);

        DeveloperAgentOrchestrator service = context.getBean(DeveloperAgentOrchestrator.class);
        Scanner scanner = new Scanner(System.in);
        AgentResult result = service.run("C:\\Users\\PC\\Documents\\repo\\Agents-HITL", """
                Your goal is to improve given repository's code and implement new features according to your needs.
                Steps to improve:
                    1. add h2 database dependency to pom
                    2. configure properties yml for h2 database configuration
                    3. create CRUD operation for person. You can create PersonController, PersonService and PersonRepository
                    4. Use spring data jpa for crud operations
                """);

        while (result.getOutcome().equals("NEEDS_HUMAN_INPUT")) {
            System.out.println(result.getFinalAnswer());
            String goal = scanner.nextLine();
            if ("exit".equals(goal))
                break;
            service.run("C:\\Users\\PC\\Documents\\repo\\Agents-HITL", goal);
        }

        System.out.println("*********FINALIZED*********");
        System.out.println(result);

        context.close();
    }
}
