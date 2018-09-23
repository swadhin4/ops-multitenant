package com.pms.web.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtil {

	final static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

	private static Properties prop;
    
    static{
        InputStream is = null;
        try {
            prop = new Properties();
            is =  Thread.currentThread().getContextClassLoader().getResourceAsStream("system-config.properties");
            prop.load(is);
            logger.info("Properties file loaded : system-config.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     
    public static String getPropertyValue(String key){
        return prop.getProperty(key);
    }
}
