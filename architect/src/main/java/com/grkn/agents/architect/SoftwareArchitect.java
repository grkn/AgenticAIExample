package com.grkn.agents.architect;

import com.grkn.agents.architect.properties.OpenAiProperties;
import com.grkn.agents.architect.resource.ArchitectureRequest;
import com.grkn.agents.architect.resource.ArchitectureResponse;
import com.grkn.agents.architect.service.ArchitectAgent;
import com.grkn.agents.architect.type.ArchitectureMode;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan(value = "com.grkn.agents.architect")
@PropertySource("classpath:application.yaml")
@EnableConfigurationProperties(OpenAiProperties.class)
public class SoftwareArchitect {
    public static void main(String[] args) throws Exception {
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
    }
}
