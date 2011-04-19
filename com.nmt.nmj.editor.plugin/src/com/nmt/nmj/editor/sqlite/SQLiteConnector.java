package com.nmt.nmj.editor.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteConnector {

    private Connection connection;

    public void connect(String fileName) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + fileName);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        SQLiteConnector connector = new SQLiteConnector();
        connector.connect("resources/media.db");
        Connection connection = connector.getConnection();
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);
        ResultSet rs = statement.executeQuery("SELECT * FROM VIDEO");
        int count = 0;
        while (rs.next()) {
            System.out.println("name = " + rs.getString("TITLE"));
            count++;
        }
        System.out.println("records: " + count);
    }

}
