package com.test.rough;
import org.testng.Assert;
import java.sql.*;
import java.util.HashMap;
import java.sql.Connection;

public class Oracon {
    public static HashMap<String, Object> param1 = new HashMap <String,Object>();

    public static void main(String[] args) throws Exception {
        //@Hostanme:Portname
       // http://www.java2s.com/Code/Jar/j/Downloadjdbcoraclejar.htm
        param1.put("HOST","jdbc:oracle:thin:@ebizsit.bmc.com:1521");
        param1.put("USERNAME","mrajurka");
        param1.put("PASSWORD","OEPLUS#123");
        param1.put("QUERY","SELECT * FROM APPS.BMOE_PRODUCT Where PRODUCT_NUMBER = 'DRNAC.0.0.00'");
        param1.put("COLUMN NAME","PRODUCT_NAME");
        param1.put("EXPECTED RESULT","HP MeasureWare Interface");
         Oracon  obj1  = new Oracon();
         obj1.querySQL(param1);

        String response_value = obj1.querySQL(param1);
        System.out.println(response_value);

      //  String response_node  =ts1.getJsonNodeValue(params);

//SELECT * FROM ( SELECT id,NAME,VERSION_STATUS, TO_DATE('19700101','yyyymmdd') + (MODIFIED_DATE/24/60/60)MAXDATE, ROW_NUMBER() OVER (PARTITION BY NAME ORDER BY MODIFIED_DATE DESC) RNK FROM aradmin.INT_PIM_Product where NAME = '${SmartNo}') WHERE RNK <= 1
    }
    public static String querySQL(HashMap<String, Object> params)throws SQLException, Exception {
        String Host = (String) params.get("HOST");
        String Username = (String) params.get("USERNAME");
        String Password = (String) params.get("PASSWORD");
        String Query = (String) params.get("QUERY");
        String ColumnName = (String) params.get("COLUMN NAME");
        String returnNumRecords = (String) params.get("EXPECTED RESULT");
        String ColumnData = null;
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection con = null;
        try {
            con = DriverManager.getConnection(Host, Username, Password);
            Statement stmt = con.createStatement();
            System.out.println("Given Query : " + Query);
            ResultSet rs = stmt.executeQuery(Query);
            int no_of_rows = 0;
            if (!rs.equals(null)) {
                while (rs.next()) {
                    ColumnData = rs.getString(ColumnName);
                    no_of_rows++;
                    System.out.println("Column Name is :" +  ColumnName);
                    System.out.println("Column Name Data is :" +  ColumnData);
                }
                System.out.println("No. of Rows in DB are : " + no_of_rows);
            }
            if (no_of_rows > 1) {
                System.out.println("As Multiple rows are found data is compared with last row");
            }else if (no_of_rows ==0){
                throw new Exception("No Records found , Check query by manually runing in the db : " + Query);
            }
        } catch (Exception ex) {
            throw new Exception("ERROR : " + ex.getMessage());
        } finally {
            if(!(con==null))
                con.close();
        }
        Assert.assertEquals(ColumnData, returnNumRecords, "Data is NOT Matching");

        return ColumnData;
      //  System.out.println(ColumnData);
    }

}
