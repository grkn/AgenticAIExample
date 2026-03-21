package com.grkn.agents;

import com.grkn.agents.core.ProductOwnerAgent;
import com.grkn.agents.core.SubProblem;
import com.grkn.agents.properties.OpenAiProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@ComponentScan(value = "com.grkn.agents")
@PropertySource("classpath:application.yaml")
@EnableConfigurationProperties(OpenAiProperties.class)
public class ProductOwner {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ProductOwner.class);

        ProductOwnerAgent productOwnerAgent = context.getBean(ProductOwnerAgent.class);
        try {
            String clarifiedProblem = productOwnerAgent.clarifyProblem("You have an customer and customer needs a authentication and authorization to login");
            List<SubProblem> subProblems = productOwnerAgent.splitIntoSubProblems(clarifiedProblem);
            String requirementsDocument = productOwnerAgent.buildRequirementsDocument(clarifiedProblem, subProblems, List.of());

            Files.writeString(Path.of("Requirement.txt"), requirementsDocument);
            System.out.println(requirementsDocument);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
