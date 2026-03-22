package com.grkn.agents.developer.tools;

import java.io.FileNotFoundException;

public interface Tool<T, R> {

    String name();
    R execute(T t);
    String description();
}
