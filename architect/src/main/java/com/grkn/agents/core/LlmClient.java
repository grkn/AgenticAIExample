package com.grkn.agents.core;

public interface LlmClient {

    String generate(String prompt) throws Exception;
}
