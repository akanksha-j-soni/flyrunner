package com.jsoni.flyrunner;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBManager {

 

    // Connect to the database
    public  Connection connect() throws ClassNotFoundException {
        try {
                Properties properties = Utility.loadProperties();
                String url = properties.getProperty("db.url");
                String username = properties.getProperty("db.username");
                String password = properties.getProperty("db.password");
              
                System.out.println("Connection established successfully!");
                return DriverManager.getConnection(url, username, password);
                
            
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to the database!");
        }
		return null;
    }
    
   
 // Method to execute CREATE, INSERT, UPDATE, or DELETE queries
    public void executeCreateQuery(String query) throws ClassNotFoundException {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = connect();
            statement = connection.createStatement();
            statement.executeUpdate(query); // For non-select queries
            System.out.println("Query executed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            closeConnection(connection);
        }
    }

    // Method to execute SELECT queries and return a ResultSet
    public ResultSet executeSelectQuery(String query) throws ClassNotFoundException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connect();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query); // For SELECT queries
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Note: Do not close the connection here, as the ResultSet depends on it.
        return resultSet;
    }

    
 // Close the database connection
    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    
}

