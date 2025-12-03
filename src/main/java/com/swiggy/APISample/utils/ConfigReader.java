package com.swiggy.APISample.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import com.swiggy.APISample.commons.FilePathConstants;


public class ConfigReader {

    private static Properties properties = new Properties();

    static {
        try {
            FileInputStream fis = new FileInputStream(FilePathConstants.configFilePath);
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("❌ Unable to load config.properties", e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
