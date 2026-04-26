package com.orangeHRM.utilities;

import com.orangeHRM.base.BaseClass;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DBConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/orangehrm";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";
    public static final Logger logger = BaseClass.logger;


    public static Connection getDBConnection() {
        try {
            logger.info("start db connection");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            logger.info("db connection success");
            return conn;
        } catch (SQLException e) {
            logger.error("error while connecting to db");
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String,String> getEmployeeDetails(String employee_id){
        String query = "SELECT emp_firstname,emp_middle_name,emp_lastname FROM hs_hr_employee WHERE employee_id ="
                +employee_id;
       HashMap<String,String> employeeDetails =  new HashMap<>();
       try(Connection conn = getDBConnection();
           Statement stmt = conn.createStatement();
           ResultSet rs=stmt.executeQuery(query)){
           logger.info("Executing query "+query);
           if(rs.next()){
             String firstName =   rs.getString("emp_firstname");
             String middleName = rs.getString("emp_middle_name");
             String lastName = rs.getString("emp_lastname");

             //store in a map

            employeeDetails.put("firstname",firstName);
            employeeDetails.put("middlename",middleName!=null?middleName:"");
            employeeDetails.put("lastname",lastName);
               logger.info("query executed");
           }
           else{
               logger.error("employee not found");
           }
       }
       catch (Exception e){
           logger.error("error while execution");
           e.printStackTrace();
       }
       return employeeDetails;
    }
}
