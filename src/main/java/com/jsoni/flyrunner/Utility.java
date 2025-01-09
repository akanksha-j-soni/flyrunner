package com.jsoni.flyrunner;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Utility {
	
    private static final String PROPERTIES_FILE = "src/main/resources/application.properties";

	  // Load database properties
    public static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(PROPERTIES_FILE)) {
            properties.load(fis);
        }
        return properties;
    }
}
