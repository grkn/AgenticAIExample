package com.grkn.agents.developer.tools;

import java.io.*;

public class ReadFile implements Tool<Payload, Payload> {
    @Override
    public String name() {
        return "READ";
    }

    @Override
    public String description() {
        return """
                
                Purpose: Read a file in repository files.
                
                Format must be json.
                
                Json format:
                
                {
                 "filePath": "absolute path of selected file which needs to be read"
                }
                
                """;
    }

    @Override
    public Payload execute(Payload payload) {
        File f = new File(payload.getFilePath());
        try (FileInputStream reader = new FileInputStream(f)) {
            payload.setContent(new String(reader.readAllBytes()));
            return payload;
        } catch (Exception ex) {
            System.out.println("Can not read file " + payload.getFilePath());
            return payload;
        }
    }
}
