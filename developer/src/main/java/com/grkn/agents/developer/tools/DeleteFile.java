package com.grkn.agents.developer.tools;

import java.io.File;

public class DeleteFile implements Tool<Payload,Payload> {

    @Override
    public String name() {
        return "DELETE";
    }

    @Override
    public Payload execute(Payload payload) {
        File file = new File(payload.getFilePath());
        boolean isDelete = file.delete();
        payload.setResult(isDelete ? "Deleted file at path: " + payload.getFilePath() : " Not Deleted file" +
                " at path" + payload.getFilePath());
        return payload;
    }

    @Override
    public String description() {
        return """
            
            Purpose: delete a file in repository files.
    
            Format must be json.
            
            Json format:
            {
             "filePath": "absolute path of current repository that you are going to delete"
            }
            
            """;
    }
}
