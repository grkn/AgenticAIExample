package com.grkn.agents.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OpenAiProperties {

    @Value("${llm.openai.api-key}")
    private String apiKey;
    @Value("${llm.openai.model:gpt-4o-mini}")
    private String model;
    @Value("${llm.openai.base-url:https://api.openai.com/v1}")
    private String baseUrl;
}
