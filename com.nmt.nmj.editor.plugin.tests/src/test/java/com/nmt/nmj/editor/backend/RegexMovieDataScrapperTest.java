package com.nmt.nmj.editor.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;


public class RegexMovieDataScrapperTest {

    @Test
    public void testGetTitle() throws IOException {
        MovieDataScrapper scrapper = new RegexMovieDataScrapper();

        InputStream movieList = this.getClass().getResourceAsStream("/movie-list");
        BufferedReader br = new BufferedReader(new InputStreamReader(movieList));
        String strLine;
        int movieCount = 0;
        int successCount = 0;
        while ((strLine = br.readLine()) != null) {
            try {
                String title = scrapper.getTitle(strLine);
                successCount++;
                System.out.println(title);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            movieCount++;
        }
        movieList.close();
        System.out.printf("matching %d/%d\n", successCount, movieCount);
    }

}
