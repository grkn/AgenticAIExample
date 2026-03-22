package com.grkn.agents.developer.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grkn.agents.developer.properties.OpenAiProperties;
import com.grkn.agents.developer.tools.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties(OpenAiProperties.class)
public class AgentConfig {

    @Bean
    public ToolRegistry toolRegistry() {
        List<Tool<Payload, Payload>> tools = List.of(
                new ListFile(),
                new ReadFile(),
                new SearchPatternInFile(),
                new WriteFile(),
                new ReplaceFile(),
                new RunMavenCompileTool()
        );
        return new ToolRegistry(tools);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(JsonGenerator.Feature.ESCAPE_NON_ASCII);
        objectMapper.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        objectMapper.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        return objectMapper;
    }
}