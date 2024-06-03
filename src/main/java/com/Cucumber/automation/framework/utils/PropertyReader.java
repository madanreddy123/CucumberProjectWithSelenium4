package com.Cucumber.automation.framework.utils;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertyReader {

    private static Properties prop = new Properties();
    private static Logging log = new Logging(PropertyReader.class.getName());


    public static Properties init_prop()
    {
        prop = new Properties();
        try {
            FileInputStream ip = new FileInputStream("./src/main/resources/Configs/qa.properties");
            prop.load(ip);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return prop;
    }

    public static String getFieldValue(String fieldName) {
        init_prop();
        String fieldValue = prop.getProperty(fieldName);
        log.info("Received parameter '" + fieldName + "' = " + fieldValue);
        if (fieldValue == null) {
            log.error("INCORRECT PARAMETER '" + fieldName + "' WAS SUPPLIED FOR RETRIEVAL FROM CONFIG");
            return "false";
        } else {return fieldValue;}
    }

    public static void setFieldValue(String fieldName, String fieldValue) {
        prop.setProperty(fieldName, fieldValue);
        log.info("Set parameter '" + fieldName + "' = " + fieldValue);
    }
}
