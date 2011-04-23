package com.nmt.nmj.editor.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.nmt.nmj.editor.exception.NmjEditorException;

public class SQLiteConnector {

    private Connection connection;
    private String fileName;

    public void connect(String fileName) throws ClassNotFoundException, NmjEditorException {
        this.fileName = fileName;
        Class.forName("org.sqlite.JDBC");
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + fileName);
        } catch (SQLException e) {
            throw new NmjEditorException(e.getMessage(), e);
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void disconnect() throws NmjEditorException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                fileName = null;
            }
        } catch (SQLException e) {
            throw new NmjEditorException("Error trying to close the database connection.", e);
        }
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isClosed() throws NmjEditorException {
        try {
            if (connection == null || connection.isClosed()) {
                return true;
            }
            return connection.isClosed();
        } catch (SQLException e) {
            throw new NmjEditorException("Error connecting with the database");
        }
    }

}
