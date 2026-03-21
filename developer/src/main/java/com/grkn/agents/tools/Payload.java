package com.grkn.agents.tools;

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
    public String resultOfSearch;
    public String mvnResult;
    public String searchPattern;
}
