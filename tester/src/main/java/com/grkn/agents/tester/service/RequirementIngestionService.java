package com.grkn.agents.tester.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Service
public class RequirementIngestionService {

    public List<Path> listRequirementFiles(String requirementsRoot) throws IOException {
        Path root = Paths.get(requirementsRoot).toAbsolutePath().normalize();
        if (!Files.exists(root)) {
            throw new IllegalArgumentException("Requirements root does not exist: " + root);
        }
        try (Stream<Path> stream = Files.walk(root)) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(this::isAllowedRequirementFile)
                    .sorted()
                    .toList();
        }
    }

    public String readRequirement(Path requirementFile) throws IOException {
        return Files.readString(requirementFile);
    }

    private boolean isAllowedRequirementFile(Path path) {
        String name = path.getFileName().toString().toLowerCase();
        return name.endsWith(".txt") || name.endsWith(".md");
    }
}
