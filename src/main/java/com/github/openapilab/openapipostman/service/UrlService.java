package com.github.openapilab.openapipostman.service;

import com.github.openapilab.openapipostman.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Scanner;

@Service
public class UrlService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrlService.class);

    public String fetchUrl(String url)  {

        LOGGER.info("fetchUrl {}", url);

        String content = null;

        try {
            Scanner scanner = new Scanner(new URL(url).openStream(), "UTF-8");
            content = scanner.useDelimiter("\\A").next();
            scanner.close();
        } catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);
            throw new BaseException("Error fetching the OpenAPI file");
        }

        return content;
    }
}
