package com.github.openapilab.openapipostman.thread;

import com.github.openapilab.openapipostman.ApplicationProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
public class FolderCleanerThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(FolderCleanerThread.class);

    @Autowired
    private ApplicationProperty applicationProperty;


    @Scheduled(fixedDelay = 1000 * 60 * 2) // Run every 2 minutes
    public void cleanUpFolder() {

        long currentTime = System.currentTimeMillis();
        File folder = new File(applicationProperty.getRootFolder());
        File[] files = folder.listFiles();

        if(files != null) {
            for (File file : files) {
                long lastModifiedTime = file.lastModified();
                long timeDifference = currentTime - lastModifiedTime;
                long timeDifferenceInMinutes = TimeUnit.MILLISECONDS.toMinutes(timeDifference);

                if (timeDifferenceInMinutes >= 5) {
                    LOGGER.info("Deleting " + file);
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
    }

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }
}
