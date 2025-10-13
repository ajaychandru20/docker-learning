package com.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final Logger logger = LoggerFactory.getLogger(Config.class);
    private static final String DEFAULT_PROPERTIES = "test-data/config/default.properties";
    private static Properties properties;

    public static void initConfigProperties() {
        // initialize load properties
        properties = loadPropertiesFile();

        // overwrite the key value if we pass any properties in mvn test
        for (String key : properties.stringPropertyNames()) {
            if (System.getProperties().contains(key)) {
                properties.setProperty(key, System.getProperty(key));
            }
        }


        // print the properties
        logger.info("-----------------------------------");
        for (String key : properties.stringPropertyNames()) {
            logger.info("{} = {}", key, properties.getProperty(key));
        }
        logger.info("-----------------------------------");

    }

    private static Properties loadPropertiesFile() {
        Properties properties = new Properties();
        try (InputStream stream = ResourceLoader.getResourceFileToStream(DEFAULT_PROPERTIES)) {
            properties.load(stream);
        } catch (Exception e) {
            logger.error("Unable to find the Properties File");
        }
        return properties;

    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
