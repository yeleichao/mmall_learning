package com.mmall.common;

import java.io.*;
import java.util.*;

/**
 * Created by yeleichao on 2017-6-13.
 */
public class test {
    public static void main(String[] args) {
//        Properties prop = System.getProperties();
//        prop.list(System.out);

        //使用java中的Properties读取配置文件中的内容
        Properties prop = new Properties();
        try {
            prop.load(new InputStreamReader(test.class.getClassLoader().getResourceAsStream("mmall.properties"), "UTF-8"));
            Enumeration en = prop.propertyNames();
            while(en.hasMoreElements()){
                String key = (String) en.nextElement();
                String value = prop.getProperty(key);
                System.out.println(key+"="+value);

                prop.setProperty(key, value);
                OutputStream out = new FileOutputStream("mmallCopy.properties");

                prop.store(out, "更改后");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Enumeration 只提供了Vector和HashTable类型集合的元素

    }
}
