package com.nmt.nmj.editor.backend;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.nmt.nmj.editor.exception.NmjEditorException;
import com.nmt.nmj.editor.model.Movie;
import com.nmt.nmj.editor.model.TvShow;
import com.nmt.nmj.editor.sqlite.SQLQueries;

public class SqliteJukeboxDatabaseService implements JukeboxDatabaseService {

    private String sqliteDB;
    private Connection connection;

    public SqliteJukeboxDatabaseService(String sqliteDB) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        this.sqliteDB = sqliteDB;
    }

    @Override
    public void openConnection() throws NmjEditorException {
        try {
            connection = DriverManager.getConnection(getUrlConnection());
        } catch (SQLException e) {
            throw new NmjEditorException(e.getMessage(), e);
        }
    }

    @Override
    public void closeConnection() throws NmjEditorException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new NmjEditorException("Error trying to close the database connection.", e);
        }
    }

    private String getUrlConnection() {
        return "jdbc:sqlite:" + sqliteDB;
    }

    @Override
    public List<Movie> getMovies() throws NmjEditorException {
        File posterRoot = new File(sqliteDB);
        List<Movie> movies = new ArrayList<Movie>();
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            ResultSet rs = statement.executeQuery(SQLQueries.MOVIES_QUERY);
            while (rs.next()) {
                Movie movie = new Movie();
                movie.setId(rs.getInt("VIDEO_ID"));
                movie.setTitle(rs.getString("TITLE"));
                movie.setSearchTitle(rs.getString("SEARCH_TITLE"));
                movie.setReleaseDate(rs.getString("RELEASE_DATE"));
                movie.setRuntime(formatIntoHHMMSS(rs.getInt("RUNTIME")));
                movie.setRating(rs.getDouble("RATING"));
                movie.setCertification(rs.getString("PARENTAL_RATING"));
                movie.setImdb(rs.getString("TTID"));
                movie.setSystem(rs.getString("SYSTEM"));
                movie.setVideoCodec(rs.getString("VIDEO_CODEC"));
                movie.setResolution(rs.getString("RESOLUTION"));
                movie.setFileName(rs.getString("PATH"));
                movie.setFps(rs.getDouble("FPS"));
                movie.setPosterImage(createPosterFullPath(posterRoot, rs.getString("DETAIL_POSTER")));
                movie.setThumbnailImage(createPosterFullPath(posterRoot, rs.getString("THUMBNAIL")));
                getDetailedInformation(movie);
                movies.add(movie);
            }
        } catch (SQLException e) {
            throw new NmjEditorException("Error reading movies from database");
        }
        return movies;
    }

    private void getDetailedInformation(Movie movie) throws NmjEditorException {
        try {
            Statement statement = connection.createStatement();
            movie.setGenres(new ArrayList<String>());
            movie.setDirectors(new ArrayList<String>());
            movie.setCasting(new ArrayList<String>());
            movie.setKeywords(new ArrayList<String>());
            ResultSet rs = statement.executeQuery(SQLQueries.MOVIES_INFORMATION_QUERY + movie.getId());
            while (rs.next()) {
                String attributeType = rs.getString("TYPE");
                if (attributeType.equals("KEYWORD")) {
                    movie.addKeyword(rs.getString("VALUE"));
                } else if (attributeType.equals("GENRE")) {
                    movie.addGenre(rs.getString("VALUE"));
                } else if (attributeType.equals("DIRECTOR")) {
                    movie.addDirector(rs.getString("VALUE"));
                } else if (attributeType.equals("PRINCIPAL_CAST_MEMBER")) {
                    movie.addCasting(rs.getString("VALUE"));
                }
            }
            rs = statement.executeQuery(SQLQueries.MOVIES_SYNOPSIS_QUERY + movie.getId());
            while (rs.next()) {
                String synopsis = rs.getString("CONTENT");
                movie.setSynopsis(synopsis);
            }
        } catch (SQLException e) {
            throw new NmjEditorException("Error retrieving extra information of video " + movie.getTitle(), e);
        }
    }

    @Override
    public List<TvShow> getTvShows() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void save(Movie movie) {
        // TODO Auto-generated method stub

    }

    @Override
    public void save(TvShow tvShow) {
        // TODO Auto-generated method stub

    }

    @Override
    public void flushConnection() throws NmjEditorException {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean isOpen() {
        try {
            return !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    private String formatIntoHHMMSS(int secondsInput) {
        int hours = secondsInput / 3600, remainder = secondsInput % 3600, minutes = remainder / 60, seconds = remainder % 60;
        return ((hours < 10 ? "0" : "") + hours + ":" + (minutes < 10 ? "0" : "") + minutes + ":"
                + (seconds < 10 ? "0" : "") + seconds);

    }

    private String createPosterFullPath(File file, String posterImage) throws SQLException {
        if (posterImage == null || posterImage.equals("")) {
            return "";
        }
        return file.getParent() + posterImage.replace("nmj_database", "");
    }

    @Override
    public String getDatabaseDescription() {
        return "Database: " + sqliteDB;
    }
}
