package com.hotelbooking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/hotel_booking?serverTimezone=UTC";
    private static final String USER = "hoteluser";      // MySQL username
    private static final String PASSWORD = "hotel123";   // MySQL password

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            return conn;
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed!");
            e.printStackTrace();
            return null;
        }
    }
}
