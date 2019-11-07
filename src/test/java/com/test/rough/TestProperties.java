package com.test.rough;


import com.test.base.TestBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class TestProperties {

    public static void main(String[] args) throws IOException {
        Properties config = new Properties();
        Properties OR = new Properties();

       // File src = new File ("./test/Properties/OR.properties");
       // File src = new File ("F:\\Automation\\INTLJ\\DataDriven\\src\\test\\resources\\Properties\\OR.properties");

        FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"\\src\\test\\resources\\Properties\\config.properties");
        config.load(fis);
                        fis = new FileInputStream(System.getProperty("user.dir")+"\\src\\test\\resources\\Properties\\OR.properties");
        OR.load(fis);
        System.out.println(config.getProperty("browser"));
       System.out.println(System.getProperty("user.dir")+"\\src\\test\\resources\\Properties\\OR.property");
        System.out.println(OR.getProperty("homeBtn"));


        //TestBase.setUp();

    }
}
