package com.nmt.nmj.editor.model;

import java.util.ArrayList;
import java.util.List;

public class Video {

    private int id;
    private String title;
    private String releaseDate;
    private String runtime;
    private double rating;
    private String system;
    private String videoCodec;
    private String resolution;
    private String fileName;
    private double fps;
    private String synopsis;
    private String posterImage;
    private String searchTitle;
    private List<String> genres = new ArrayList<String>();
    private List<String> directors = new ArrayList<String>();
    private List<String> casting = new ArrayList<String>();
    private List<String> keywords = new ArrayList<String>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getVideoCodec() {
        return videoCodec;
    }

    public void setVideoCodec(String videoCodec) {
        this.videoCodec = videoCodec;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public double getFps() {
        return fps;
    }

    public void setFps(double fps) {
        this.fps = fps;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getPosterImage() {
        return posterImage;
    }

    public void setPosterImage(String posterImage) {
        this.posterImage = posterImage;
    }

    public String getSearchTitle() {
        return searchTitle;
    }

    public void setSearchTitle(String searchTitle) {
        this.searchTitle = searchTitle;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void addGenre(String genre) {
        this.genres.add(genre);
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public void addDirector(String director) {
        this.directors.add(director);
    }

    public void setDirectors(List<String> directors) {
        this.directors = directors;
    }

    public List<String> getCasting() {
        return casting;
    }

    public void addCasting(String casting) {
        this.casting.add(casting);
    }

    public void setCasting(List<String> casting) {
        this.casting = casting;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void addKeyword(String keyword) {
        this.keywords.add(keyword);
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

}
