package com.test.testcases;

import com.test.base.ConfigurationUtilities;
import org.testng.annotations.Test;

import java.io.IOException;

public class ConnectSFDC extends ConfigurationUtilities {


    @Test

    public void restcallToSalesforce() throws IOException {

        params.put("USERNAME", "manishrajurkar87@gmail.com");
        params.put("PASSWORD", "Lightning@123AtgW1ctyph0jw936mT2zVKxn");
        params.put("CLIENTID", "3MVG9d8..z.hDcPIOtFMLGaOQn_8JxZ6mzOxU.6vF3AmLcrzcf1w4br_xDDuBzqDQoJU8C7lUYJfJSE0oGNh4");
        params.put("CLIENTSECRET", "B204ECC8C399FE85E75E94FC12A4E3AB7748007C5E489A9692BE03F4165B8C63");
        params.put("SOQL Query", "select id from contact limit 1");
        params.put("Json Path Expression" ,"$.records[0].Id");

        ConfigurationUtilities ts1 = new ConfigurationUtilities();
        ts1.executeSoql(params);
        String response_node  =ts1.getJsonNodeValue(params);
        System.out.println("RESPONSE NODE IS :" +response_node);

    }
}
