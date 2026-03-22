package com.grkn.agents.productowner.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiProperties {

    @Value("${agent.openai.base-url}")
    private String baseUrl;

    @Value("${agent.openai.model}")
    private String model;

    @Value("${agent.openai.api-key}")
    private String apiKey;
}