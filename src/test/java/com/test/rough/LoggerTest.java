package com.test.rough;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LoggerTest {


    public static Logger log1 = Logger.getLogger(LoggerTest.class.getName());

/*    public static String getLog1(String logMessage) {
        return logMessage;
    }*/
public static void main (String args[]){
    PropertyConfigurator.configure("./src/test/resources/Logs/log4j.properties");
     log1.info("My first Logger");
     log1.error("ERRROr Logger");
}
}
