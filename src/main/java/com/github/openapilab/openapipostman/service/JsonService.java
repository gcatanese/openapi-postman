package com.github.openapilab.openapipostman.service;

import com.github.openapilab.openapipostman.exception.BaseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class JsonService {

    public boolean isJson(String json) {
        return json.trim().startsWith("{");
    }
    public boolean isValidJson(String json) {
        boolean ret = true;

        try {
            ObjectMapper jsonMapper = new ObjectMapper();
            jsonMapper.readTree(json);

        } catch (IOException e) {
            // invalid JSON
            ret = false;
        }

        return ret;
    }

    public String setOpenApiVersion(String json) {
        try {
            ObjectMapper jsonMapper = new ObjectMapper();
            JsonNode rootNode = jsonMapper.readTree(json);

            ((ObjectNode) rootNode).put("openapi", "3.0.3");
            json = jsonMapper.writeValueAsString(rootNode);


        } catch (IOException e) {
            throw new BaseException("Error processing JSON");
        }

        return json;
    }

    public String prettifyJson(String json)  {
        String ret = "";

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            Object jsonObject = objectMapper.readValue(json, Object.class);
            ret = objectMapper.writeValueAsString(jsonObject);
        } catch (Exception e) {
            ret = json;
        }

        return  ret;
    }

}
