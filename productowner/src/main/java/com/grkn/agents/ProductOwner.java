package com.grkn.agents;

import com.grkn.agents.productowner.core.ProductOwnerAgent;
import com.grkn.agents.productowner.properties.OpenAiProperties;
import com.grkn.agents.productowner.resource.SubProblem;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@ComponentScan(value = "com.grkn.agents.productowner")
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
            // Important divide requirements according to divisionFactorForSubproblems so it is detailed.
            int divisionFactorForProblems = 1;
            int i = 0;

            while (i < divisionFactorForProblems) {

                List<SubProblem> newSubProblems = new ArrayList<>();
                ListIterator<SubProblem> subProblemListIterator = subProblems.listIterator();
                while (subProblemListIterator.hasNext()) {
                    SubProblem next = subProblemListIterator.next();

                    newSubProblems.addAll(productOwnerAgent
                            .splitIntoSubProblems(
                                    "Title: " + next.getTitle() + " Description:" + next.getDescription()));
                    subProblemListIterator.remove();
                }
                subProblems.clear();
                subProblems.addAll(newSubProblems);
                i++;
            }
            String requirementsDocument = productOwnerAgent.buildRequirementsDocument(clarifiedProblem, subProblems, List.of());

            Files.writeString(Path.of("Requirement.txt"), requirementsDocument);
            System.out.println(requirementsDocument);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
