package org.example;

import java.sql.*;

public class DbHelper {

    private static final String URL = "jdbc:sqlite:./data/database.sqlite";

    static {
        try {
            // Создаём папку data, если её нет
            new java.io.File("./data").mkdirs();
            Class.forName("org.sqlite.JDBC");

            try (Connection c = DriverManager.getConnection(URL)) {
                c.createStatement().execute("""
                    CREATE TABLE IF NOT EXISTS users (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        email TEXT,
                        password TEXT,
                        confirmed INTEGER DEFAULT 0,
                        token TEXT
                    );
                """);

                c.createStatement().execute("""
                    CREATE TABLE IF NOT EXISTS contacts (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT,
                        phone TEXT
                    );
                """);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
