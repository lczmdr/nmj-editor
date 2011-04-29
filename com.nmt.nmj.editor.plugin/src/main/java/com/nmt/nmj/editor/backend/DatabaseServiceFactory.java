package com.nmt.nmj.editor.backend;

public class DatabaseServiceFactory {

    public static SqliteJukeboxDatabaseService createSqliteJukeboxDatabaseService(String selectedFile) {
        try {
            return new SqliteJukeboxDatabaseService(selectedFile);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
