package com.nmt.nmj.editor.sqlite;

public class SQLQueries {

    /**
     * Select the info needed to display the availables movies in the movies
     * table
     */
    public static final String MOVIES_QUERY = "SELECT VIDEO.VIDEO_ID, VIDEO.TITLE, VIDEO.RATING, VIDEO.RUNTIME, VIDEO.SYSTEM, VIDEO.VIDEO_CODEC, VIDEO_ATTR.TYPE, VIDEO_ATTR.VALUE FROM VIDEO LEFT OUTER JOIN VIDEO_ATTR ON VIDEO.VIDEO_ID = VIDEO_ATTR.VIDEO_ID WHERE VIDEO_ATTR.VALUE='feature' ORDER BY VIDEO.TITLE";

}
