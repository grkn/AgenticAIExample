package com.grkn.agents.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class ReplaceFile implements Tool<Payload, Payload> {

    @Override
    public String name() {
        return "REPLACE";
    }

    @Override
    public String description() {
        return """
                
                Purpose: Replace a file with new content in the repository files.
                
                Format must be json.
                
                Json format:
                {
                 "filePath": "absolute path of selected file that needs to be replaced"
                 "newContent": "new content needs to be replaced"
                }
                
                """;
    }

    @Override
    public Payload execute(Payload payload) {
        File f = new File(payload.getFilePath());
        try (FileOutputStream fileOutputStream = new FileOutputStream(f)) {
            fileOutputStream.write(payload.getNewContent().getBytes(StandardCharsets.UTF_8));
            payload.setContent(payload.getNewContent());
        } catch (Exception ex) {
            System.out.println("Can not write to file: " + payload);
        }
        return payload;
    }
}
