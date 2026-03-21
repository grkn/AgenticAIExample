package com.grkn.agents.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class WriteFile implements Tool<Payload, Payload> {
    @Override
    public String name() {
        return "WRITE";
    }

    @Override
    public String description() {
        return """
                
                Purpose: Write modified file to the repository which you are working on.
                
                Format must be json.
                
                Input Json format:
                {
                 "content": "content of the file that you need to write",
                 "filePath": "absolute path of the file that you need to write"
                }
                
                """;
    }

    @Override
    public Payload execute(Payload payload) {
        File f = new File(payload.getFilePath());
        createDirectory(payload);
        try (FileOutputStream fileOutputStream = new FileOutputStream(f)) {
            fileOutputStream.write(payload.getContent().getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            System.out.println("Can not write to file: " + payload);
        }

        return payload;
    }

    private static void createDirectory(Payload payload) {
        Path path = Paths.get(payload.getFilePath());
        List<String> possiblePaths = new LinkedList<>();
        while (path.getParent() != null) {
            possiblePaths.add(path.toString());
            path = path.getParent();
        }

        for (int i = possiblePaths.size() - 1; i >=0 ; i--) {
            File tmp = new File(possiblePaths.get(i));
            if (!tmp.exists()) {
                boolean isCreated = tmp.mkdir();
                if (isCreated) {
                    System.out.println("New directory created. Name: " + tmp.getName());
                }
            }
        }
    }
}
