package com.grkn.agents.architect.core;

public interface LlmClient {

    String generate(String prompt) throws Exception;
}
