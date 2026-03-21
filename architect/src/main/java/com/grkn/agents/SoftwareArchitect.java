package com.grkn.agents;

import com.grkn.agents.core.*;
import com.grkn.agents.properties.ArchitectProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan(value = "com.grkn.agents")
@PropertySource("classpath:application.yaml")
@EnableConfigurationProperties(ArchitectProperties.class)
public class SoftwareArchitect {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(SoftwareArchitect.class);

        ArchitectAgent architectAgent = context.getBean(ArchitectAgent.class);
        ArchitectureRequest architectureRequest =
                new ArchitectureRequest("Requirement: Encryption Algorithm Selection",
                        "Choose strong and industry-standard encryption algorithms to protect stored user credentials.",
                        "The customer requires a secure authentication and authorization system to enable user login and control access to their application",
                        ArchitectureMode.DEFAULT
                );
        ArchitectureResponse response = architectAgent.run(architectureRequest);
        context.getBean(Validator.class).validate(response.architecture());
    }
}
