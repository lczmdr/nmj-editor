package com.nmt.nmj.editor.backend;

public class MovieInformationFactory {

    public static TmdbService createTmdbService() {
        return new TmdbService();
    }

    public static TmdbService createTmdbService(String apiKey) {
        return new TmdbService(apiKey);
    }

}
