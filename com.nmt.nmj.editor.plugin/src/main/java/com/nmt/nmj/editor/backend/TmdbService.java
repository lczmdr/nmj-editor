package com.nmt.nmj.editor.backend;

import java.util.List;

import com.moviejukebox.themoviedb.TheMovieDb;
import com.moviejukebox.themoviedb.model.Artwork;
import com.moviejukebox.themoviedb.model.MovieDB;

public class TmdbService {

    private static final String API_KEY = "1fc5646188408ff75246be5f65d3d55f"; //$NON-NLS-1$
    private static final String DEFAULT_LANGUAGE = "en"; //$NON-NLS-1$

    private TheMovieDb tmdb;

    public TmdbService() {
        tmdb = new TheMovieDb(API_KEY);
    }

    public TmdbService(String apiKey) {
        tmdb = new TheMovieDb(apiKey);
    }

    public List<MovieDB> searchMovie(String title) {
        return searchMovie(title, DEFAULT_LANGUAGE);
    }

    public List<MovieDB> searchMovie(String title, String language) {
        return tmdb.moviedbSearch(title, language);
    }

    public List<Artwork> obtainImages(String movieId) {
        MovieDB movieDB = tmdb.moviedbGetImages(movieId, DEFAULT_LANGUAGE);
        return movieDB.getArtwork();
    }

    public List<Artwork> obtainImages(String movieId, String language) {
        return obtainImages(movieId, language);
    }

}
