
package com.chinamilitary.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
    private static final Log logger = LogFactory.getLog(Configuration.class);
    private static InputStream inputFile;
    static Properties Pt = new Properties();

    static {
        try {
            inputFile = Configuration.class.getResourceAsStream("/config.properties");
            Pt.load(inputFile);
            inputFile.close();
            logger.info(".Properties File is loaded!");
        } catch (IOException e) {
            logger.error("读取配置文件失败,IOException:" + e.getMessage());
        }
    }

    private static void loadProperties() {
        try {
            inputFile = Configuration.class.getResourceAsStream("/config.properties");
            Pt.load(inputFile);
            inputFile.close();
            logger.info(".Properties File is loaded!");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(".Properties File is missing!");
        }
    }

    public static String getValue(String key) {
        String sValue = "";
        try {
            if (Pt.containsKey(key)) {
                sValue = Pt.getProperty(key);
            } else {
                logger.info("can't find key[" + key + "] configurtion");
                loadProperties();
                sValue = Pt.getProperty(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sValue;
    }

}
