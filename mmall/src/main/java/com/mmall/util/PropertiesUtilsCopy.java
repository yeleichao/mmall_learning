package com.mmall.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by yeleichao on 2017-7-15.
 */
public class PropertiesUtilsCopy {

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtilsCopy.class);
    private static Properties prop;

    static{
        String fileName = "mmall.properties";
        prop = new Properties();
        try {
            prop.load(new InputStreamReader(PropertiesUtilsCopy.class.getClassLoader().getResourceAsStream(fileName)));
        } catch (IOException e) {
            logger.error("配置文件读取异常",e);
        }
    }


}
