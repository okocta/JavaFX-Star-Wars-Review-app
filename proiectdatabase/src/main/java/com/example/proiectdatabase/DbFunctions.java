package com.example.proiectdatabase;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbFunctions {


    public Connection databaseLink;

    public Connection getConnection() {
        String databaseUser = "postgres";
        String databasePassword = "1505tavi";
        String databaseName="proiect";
        String url = "jdbc:postgresql://localhost:5432/"+databaseName;

        try {
            Class.forName("org.postgresql.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        }catch(Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
        return databaseLink;
    }
}
