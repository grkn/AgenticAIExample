package com.grkn.agents.developer.tools;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class ListFile implements Tool<Payload, Payload>{
    @Override
    public String name() {
        return "LIST";
    }

    @Override
    public String description() {
        return """
                
                Purpose: List files in repository files.
                
                Format must be json.
                
                Json format:
                {
                 "rootPath": "absolute path of current repository that you are working"
                }
                
                """;
    }

    @SneakyThrows
    @Override
    public Payload execute(Payload payload) {
        Path root = Paths.get(payload.getRootPath());
        try (var stream = Files.walk(root, 10)) {
             payload.setResult(stream
                    .filter(Files::isRegularFile)
                    .filter(this::isRelevantFile)
                    .limit(200)
                    .map(Path::toString)
                    .collect(Collectors.joining("\n")));
             return payload;
        }
    }

    private boolean isRelevantFile(Path path) {
        String value = path.toString();
        return value.endsWith(".java")
                || value.endsWith(".xml")
                || value.endsWith(".yml")
                || value.endsWith(".yaml")
                || value.endsWith(".properties")
                || value.endsWith(".md");
    }

}
