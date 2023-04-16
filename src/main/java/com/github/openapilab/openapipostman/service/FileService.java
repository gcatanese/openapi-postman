package com.github.openapilab.openapipostman.service;

import com.github.openapilab.openapipostman.ApplicationProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    @Autowired
    private ApplicationProperty applicationProperty;

    public String save(String raw) throws Exception {

        String filename = makeDir() + "/openapi.spec";

        Path path = Paths.get(filename);
        byte[] strToBytes = raw.getBytes();
        Files.write(path, strToBytes);

        return filename;

    }

    public String makeDir() throws Exception {
        String folder = applicationProperty.getRootFolder() != null ? applicationProperty.getRootFolder() : "";
        folder = folder + "/postman_" + UUID.randomUUID() + "/";

        Path output = Files.createDirectories(Paths.get(folder));

        return output.toAbsolutePath().toString();
    }

    public String read(String filename) throws IOException {
        String read = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);


        return read;
    }


}
