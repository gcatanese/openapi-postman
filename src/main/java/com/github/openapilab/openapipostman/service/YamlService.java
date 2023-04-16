package com.github.openapilab.openapipostman.service;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class YamlService {

    public boolean isValidYaml(String yaml) {
        boolean ret = true;

        try {
            YAMLMapper yamlMapper = new YAMLMapper();
            yamlMapper.readTree(yaml);
        } catch (IOException e) {
            // invalid YAML
            ret = false;
        }

        return ret;
    }

}
