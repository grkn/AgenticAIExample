package com.grkn.agents.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

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
        try (FileOutputStream fileOutputStream = new FileOutputStream(f)) {
           fileOutputStream.write(payload.getContent().getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            System.out.println("Can not write to file: " + payload);
        }

        return payload;
    }
}
