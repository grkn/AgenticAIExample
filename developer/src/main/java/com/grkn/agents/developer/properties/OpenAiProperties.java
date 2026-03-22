package com.grkn.agents.developer.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
@Getter
@Setter
public class OpenAiProperties {

    @Value("${agent.openai.base-url}")
    private String baseUrl;

    @Value("${agent.openai.model}")
    private String model;

    @Value("${agent.openai.api-key}")
    private String apiKey;
}


