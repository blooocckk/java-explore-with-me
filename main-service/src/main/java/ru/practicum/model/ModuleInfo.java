package ru.practicum.model;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ModuleInfo {
    @Value("${project.groupId}")
    private String groupId;

    @Value("${project.artifactId}")
    private String artifactId;

    @Value("${project.version}")
    private String version;

    public void printModuleInfo() {
        System.out.println("Group ID: " + groupId);
        System.out.println("Artifact ID: " + artifactId);
        System.out.println("Version: " + version);
    }
}
