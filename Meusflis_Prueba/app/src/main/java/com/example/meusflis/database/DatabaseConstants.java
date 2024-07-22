package com.example.meusflis.database;

public class DatabaseConstants {

    /**
     * TABLE CONSTANTS
     */
    public static final String TABLE_USER = "user";
    public static final String TABLE_GENRE = "genre";
    public static final String TABLE_MULTIMEDIA_CONTENT = "multimedia_content";
    public static final String TABLE_MULTIMEDIA_CONTENT_GENRE = "multimedia_content_genre";


    /**
     * USER TABLE CONSTANTS
     */
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_BIRTHYEAR = "birthyear";
    public static final String COLUMN_TELEPHONE = "telephone";
    public static final String COLUMN_CARD = "card";


    /**
     * MULTIMEDIA_CONTENT TABLE CONSTANTS
     */
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_GENRE = "genre";
    public static final String COLUMN_DEMOGRAPHIC_CATEGORY = "demographic_category";
    public static final String COLUMN_COVER = "cover";
    public static final String COLUMN_LIKES = "likes";
    public static final String COLUMN_URL_MANGA = "url_manga";
    public static final String COLUMN_URL_ANIME = "url_anime";


    /**
     * GENRE TABLE CONSTANTS
     */
    public static final String COLUMN_GENRE_NAME = "name";


    /**
     * MULTIMEDIA_CONTENT_GENRE TABLE CONSTANTS
     */
    public static final String COLUMN_MULTIMEDIA_CONTENT_ID = "multimedia_content_id";
    public static final String COLUMN_GENRE_ID = "genre_id";
}
