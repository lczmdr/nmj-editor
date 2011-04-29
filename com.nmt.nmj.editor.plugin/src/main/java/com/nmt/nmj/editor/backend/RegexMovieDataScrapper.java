package com.nmt.nmj.editor.backend;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegexMovieDataScrapper implements MovieDataScrapper {
    
    private static final int TITLE_GROUP = 2;
//    private static final int YEAR_GROUP = 4;
//    private static final int GROUP_GROUP = 9;
    
    private static final String FILENAME_REGEX = "((.*?)\\.)((\\d{4})\\.)?(((CD[1-3]|AC3|DTS|PROPER|CHRONO|COLORIZED|DC|DUBBED|EXTENDED|FINAL|FS|INT|INTERNAL|LIMITED|PROPER|RATED|REAL|REMASTERED|REPACK|RERIP|RETAIL|SE|SUBBED|THEATRICAL|UE|UNCUT|UNRATED|WS)\\.)*)(CAM|TS|TELESYNC|PDVD|WP|WORKPRINT|SCR|SCREENER|DVDSCR|DVD-SCREENER|VHS-SCREENER|R5|TC|TELECINE|BDRIP|BRRIP|DVDRIP|DVDR|TVRIP|DSR|STV|HDTV|PDTV|DVBRIP|SATRIP|VHSRIP|720p|1080p).*-(.*)";
    private static final Pattern filenamePattern = Pattern.compile(FILENAME_REGEX, Pattern.CASE_INSENSITIVE);

    @Override
    public String getTitle(String fileName) throws Exception{
        Matcher matcher = filenamePattern.matcher(fileName);
        if (matcher.matches())
            return matcher.group(TITLE_GROUP).replace('.', ' ');
        throw new Exception("Not match found for " + fileName);
    }

}
