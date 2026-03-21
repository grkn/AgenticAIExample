package com.grkn.agents.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SearchPatternInFile implements Tool<Payload, Payload> {

    @Override
    public String name() {
        return "SEARCH_PATTERN_IN_FILE";
    }

    @Override
    public String description() {
        return """
                
                Purpose: Searches for a text pattern in repository files that return file's path which includes pattern.
                
                Format must be json.
                
                Json format:
                
                {
                 "searchPattern": "search text pattern which need to be searched",
                 "rootPath": "absolute path of the repository that you are working on"
                }
                
                """;
    }

    @Override
    public Payload execute(Payload payload) {
        StringBuilder result = new StringBuilder();
        try (var paths = Files.walk(Paths.get(payload.getRootPath()))) {
            paths.filter(Files::isRegularFile)
                    .filter(this::isRelevantFile)
                    .forEach(path -> appendIfMatches(path, payload.getSearchPattern(), result));
            System.out.println("Search file does not return anything. Search for " + payload.getSearchPattern());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        payload.setFilePath(result.toString());
        return payload;
    }


    private boolean isRelevantFile(Path path) {
        String value = path.toString();
        return value.endsWith(".java")
                || value.endsWith(".xml")
                || value.endsWith(".yml")
                || value.endsWith(".yaml")
                || value.endsWith(".properties");
    }

    private void appendIfMatches(Path path, String pattern, StringBuilder result) {
        try {
            String content = Files.readString(path);
            if (content.contains(pattern)) {
                result.append(path).append("\n");
            }
        } catch (Exception ignored) {
        }
    }

}
