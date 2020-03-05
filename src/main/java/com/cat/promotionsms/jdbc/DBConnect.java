package com.cat.promotionsms.jdbc;

import com.cat.promotionsms.properties.PropertiesClass;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {

    public static Connection getConnectionCATATP() {
        PropertiesClass prop = new PropertiesClass();
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(prop.getProperties().getProperty("DB_URL"), prop.getProperties().getProperty("DB_USERNAME"), prop.getProperties().getProperty("DB_PASSWORD"));
            conn.setAutoCommit(false);
            if (conn != null) {
                System.out.println("Connected database successfully...");
            } else {
                System.out.println("Connected database Failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return conn;
    }
}
