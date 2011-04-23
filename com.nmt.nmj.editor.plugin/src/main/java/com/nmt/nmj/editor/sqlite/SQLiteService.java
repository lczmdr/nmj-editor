package com.nmt.nmj.editor.sqlite;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.nmt.nmj.editor.exception.NmjEditorException;
import com.nmt.nmj.editor.model.Video;

public class SQLiteService {

    private SQLiteConnector sqliteConnector;

    public void openConnection(String fileName) throws NmjEditorException {
        sqliteConnector = new SQLiteConnector();
        try {
            sqliteConnector.connect(fileName);
        } catch (ClassNotFoundException e) {
            throw new NmjEditorException("SQLite libraries are missing.");
        }
    }

    public void closeConnection() throws NmjEditorException {
        if (sqliteConnector != null) {
            try {
                sqliteConnector.getConnection().close();
            } catch (SQLException e) {
                throw new NmjEditorException("Error closing the database connection");
            }
        }
    }

    public boolean isConnected() throws NmjEditorException {
        if (sqliteConnector == null) {
            return false;
        }
        return !sqliteConnector.isClosed();
    }

    public String getFileName() {
        return sqliteConnector.getFileName();
    }

    public List<Video> getAllMovies() throws NmjEditorException {
        Connection connection = sqliteConnector.getConnection();
        List<Video> videos = new ArrayList<Video>();
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            ResultSet rs = statement.executeQuery(SQLQueries.MOVIES_QUERY);
            while (rs.next()) {
                Video video = new Video();
                video.setId(rs.getInt("VIDEO_ID"));
                video.setTitle(rs.getString("TITLE"));
                video.setReleaseDate(rs.getString("RELEASE_DATE"));
                video.setRuntime(formatIntoHHMMSS(rs.getInt("RUNTIME")));
                video.setRating(rs.getDouble("RATING"));
                video.setSystem(rs.getString("SYSTEM"));
                video.setVideoCodec(rs.getString("VIDEO_CODEC"));
                video.setResolution(rs.getString("RESOLUTION"));
                video.setFileName(rs.getString("PATH"));
                video.setFps(rs.getDouble("FPS"));
                videos.add(video);
            }
        } catch (SQLException e) {
            throw new NmjEditorException("Error reading movies from database");
        }
        return videos;
    }

    public void getDetailedInformation(Video video) throws NmjEditorException {
        Connection connection = sqliteConnector.getConnection();
        try {
            Statement statement = connection.createStatement();
            video.setGenres(new ArrayList<String>());
            video.setDirectors(new ArrayList<String>());
            video.setCasting(new ArrayList<String>());
            video.setKeywords(new ArrayList<String>());
            ResultSet rs = statement.executeQuery(SQLQueries.MOVIES_INFORMATION_QUERY + video.getId());
            while (rs.next()) {
                String attributeType = rs.getString("TYPE");
                if (attributeType.equals("KEYWORD")) {
                    video.addKeyword(rs.getString("VALUE"));
                } else if (attributeType.equals("GENRE")) {
                    video.addGenre(rs.getString("VALUE"));
                } else if (attributeType.equals("DIRECTOR")) {
                    video.addDirector(rs.getString("VALUE"));
                } else if (attributeType.equals("PRINCIPAL_CAST_MEMBER")) {
                    video.addCasting(rs.getString("VALUE"));
                }
            }
            rs = statement.executeQuery(SQLQueries.MOVIES_SYNOPSIS_QUERY + video.getId());
            while (rs.next()) {
                String synopsis = rs.getString("CONTENT");
                video.setSynopsis(synopsis);
            }
        } catch (SQLException e) {
            throw new NmjEditorException("Error retrieving extra information of video " + video.getTitle(), e);
        }
    }

    private String formatIntoHHMMSS(int secondsInput) {
        int hours = secondsInput / 3600, remainder = secondsInput % 3600, minutes = remainder / 60, seconds = remainder % 60;
        return ((hours < 10 ? "0" : "") + hours + ":" + (minutes < 10 ? "0" : "") + minutes + ":"
                + (seconds < 10 ? "0" : "") + seconds);

    }

}
