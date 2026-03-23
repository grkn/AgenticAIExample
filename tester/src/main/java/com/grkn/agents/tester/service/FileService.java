package com.grkn.agents.tester.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileService {

    public List<Path> listFiles(String rootPath) throws IOException {
        Path root = normalize(rootPath);
        ensureExists(root);
        try (Stream<Path> stream = Files.walk(root)) {
            return stream.filter(Files::isRegularFile).sorted().toList();
        }
    }

    public String readFile(String filePath, String workspaceRoot) throws IOException {
        Path workspace = normalize(workspaceRoot);
        Path file = safeResolve(workspace, filePath);
        ensureExists(file);
        return Files.readString(file);
    }

    public Path writeFile(String filePath, String workspaceRoot, String content) throws IOException {
        Path workspace = normalize(workspaceRoot);
        Path file = safeResolve(workspace, filePath);
        if (file.getParent() != null) {
            Files.createDirectories(file.getParent());
        }
        return Files.writeString(file, content);
    }

    public List<Path> searchByPattern(String rootPath, String pattern) throws IOException {
        Path root = normalize(rootPath);
        ensureExists(root);
        String normalized = pattern == null ? "" : pattern.toLowerCase();
        try (Stream<Path> stream = Files.walk(root)) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().toLowerCase().contains(normalized))
                    .sorted()
                    .toList();
        }
    }

    private Path normalize(String path) {
        return Paths.get(path).toAbsolutePath().normalize();
    }

    private Path safeResolve(Path workspaceRoot, String candidatePath) {
        Path resolved = workspaceRoot.resolve(candidatePath).toAbsolutePath().normalize();
        if (!resolved.startsWith(workspaceRoot)) {
            throw new IllegalArgumentException("Path traversal is not allowed: " + candidatePath);
        }
        return resolved;
    }

    private void ensureExists(Path path) {
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("Path does not exist: " + path);
        }
    }
}
