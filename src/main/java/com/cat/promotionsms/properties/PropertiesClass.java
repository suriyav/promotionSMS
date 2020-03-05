package com.cat.promotionsms.properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesClass {
    public Properties getProperties() {
        Properties properties = new Properties();
        FileInputStream fi = null;
        try {
//            String propFileName = "/TOPUPSMS/properties/Promotionsms.sit.properties";
            String propFileName = "/Users/sh0ckpro/ABC/CAT/RHDM/Properties/Promotionsms/properties/Promotionsms.sit.properties";
            fi = new FileInputStream(propFileName);
            if (fi != null) {
                properties.load(fi);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
            fi.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fi != null) {
                try {
                    fi.close();
                } catch (IOException e) {
                    e.getMessage();
                }
            }
        }
        return properties;
    }
}
