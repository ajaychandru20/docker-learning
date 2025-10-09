package com.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class ResourceLoader {

    private static final Logger logger = LoggerFactory.getLogger(ResourceLoader.class);

    public static InputStream getResourceFileToStream(String url) {

        try {
            InputStream stream = ResourceLoader.class.getClassLoader().getResourceAsStream(url);
            if (Objects.nonNull(stream)) {
                logger.info("The path passed in ResourceLoader is: {}", url);
                return stream;
            }
            return Files.newInputStream(Path.of(url));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


}
