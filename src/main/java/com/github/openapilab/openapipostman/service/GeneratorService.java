package com.github.openapilab.openapipostman.service;

import org.openapitools.codegen.ClientOptInput;
import org.openapitools.codegen.DefaultGenerator;
import org.openapitools.codegen.config.CodegenConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class GeneratorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratorService.class);


    public String generate(String specfile) throws Exception {

        final CodegenConfigurator configurator = new CodegenConfigurator()
                .setGeneratorName("postman-collection")
                .setInputSpec(specfile)
                .setOutputDir(getPath(specfile));


        final ClientOptInput clientOptInput = configurator.toClientOptInput();
        DefaultGenerator generator = new DefaultGenerator();
        List<File> files = generator.opts(clientOptInput).generate();

        String postmanFile = getPath(specfile) + "/postman.json";
        LOGGER.info("File generated: {}", postmanFile);

        return postmanFile;

    }

     String getPath(String filename) {
        Path p = Paths.get(filename);
        Path folder = p.getParent();

        return folder.toString();
    }



}
