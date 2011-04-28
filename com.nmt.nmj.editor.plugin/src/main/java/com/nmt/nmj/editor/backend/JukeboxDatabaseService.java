package com.nmt.nmj.editor.backend;

import java.util.List;

import com.nmt.nmj.editor.exception.NmjEditorException;
import com.nmt.nmj.editor.model.Movie;
import com.nmt.nmj.editor.model.TvShow;

public interface JukeboxDatabaseService {

    List<Movie> getMovies() throws NmjEditorException;

    List<TvShow> getTvShows();

    void save(Movie movie);

    void save(TvShow tvShow);

    void closeConnection() throws NmjEditorException;

    void openConnection() throws NmjEditorException;

    void flushConnection() throws NmjEditorException;

    boolean isOpen();

    String getDatabaseDescription();
}
