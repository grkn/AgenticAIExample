package com.grkn.agents.developer.tools;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Payload {
    public String content;
    public String filePath;
    public String newContent;
    public String rootPath;
    public String result;
    public String mvnResult;
    public String searchPattern;
}
