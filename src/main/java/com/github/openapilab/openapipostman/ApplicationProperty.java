package com.github.openapilab.openapipostman;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ApplicationProperty {

    @Value("${ROOT_FOLDER:#{null}}")
    private String rootFolder;

    public String getRootFolder() {
        return rootFolder;
    }

    public void setRootFolder(String rootFolder) {
        this.rootFolder = rootFolder;
    }

}
