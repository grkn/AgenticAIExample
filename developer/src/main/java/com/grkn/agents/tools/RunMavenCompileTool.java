package com.grkn.agents.tools;

public class RunMavenCompileTool implements Tool<Payload, Payload> {

    @Override
    public String name() {
        return "RUN_MAVEN_COMPILE";
    }

    @Override
    public String description() {
        return """
                
                Purpose: Run maven compile to ensure that code is compiled.
                
                Format must be json.
                
                Json format:
                {
                 "rootPath": "absolute path of the repository that you are working on"
                }
                
                """;
    }

    @Override
    public Payload execute(Payload payload){
        String os = System.getProperty("os.name").toLowerCase();
        String mvnCommand = os.contains("win") ? "mvn.cmd" : "mvn";
        ProcessBuilder pb = new ProcessBuilder(mvnCommand, "compile");
        pb.directory(new java.io.File(payload.getRootPath()));
        pb.redirectErrorStream(true);
        StringBuilder output = new StringBuilder();
        try {
            Process process = pb.start();

            try (java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            int exitCode = process.waitFor();
            output.append("\nEXIT_CODE=").append(exitCode);
            payload.setMvnResult(output.toString());
            return payload;
        } catch (Exception e) {
            payload.setMvnResult("Exception thrown from mvn compile");
            System.out.println("Maven compile process throws an exception");
            return payload;
        }
    }
}