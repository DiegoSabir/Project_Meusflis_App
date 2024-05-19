package com.example.meusflis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBase extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "BBDD.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USER = "user";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_TELEPHONE = "telephone";
    private static final String COLUMN_CARD = "card";
    private final Context context;
    SQLiteDatabase bbdd;
    Cursor consulta;

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    public boolean checkUser(String email, String password) {
        bbdd = this.getReadableDatabase();
        try {
            consulta = bbdd.query(TABLE_USER, null, COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?", new String[]{email, password}, null, null, null);
            boolean exists = consulta.getCount() > 0;
            return exists;
        }
        finally {
            if (consulta != null) {
                consulta.close();
            }
            if (bbdd != null) {
                bbdd.close();
            }
        }
    }

    public HashMap<String, String> getUserDetails(String email) {
        HashMap<String, String> userDetails = new HashMap<>();
        bbdd = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = bbdd.query(TABLE_USER, null, COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                userDetails.put(COLUMN_EMAIL, cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
                userDetails.put(COLUMN_PASSWORD, cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
                userDetails.put(COLUMN_NAME, cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                userDetails.put(COLUMN_TELEPHONE, cursor.getString(cursor.getColumnIndex(COLUMN_TELEPHONE)));
                userDetails.put(COLUMN_CARD, cursor.getString(cursor.getColumnIndex(COLUMN_CARD)));
            }
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            if (bbdd != null) {
                bbdd.close();
            }
        }
        return userDetails;
    }

    public boolean addUser(String email, String password, String name, String telephone, String card) {
        bbdd = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();

            values.put(COLUMN_EMAIL, email);
            values.put(COLUMN_PASSWORD, password);
            values.put(COLUMN_NAME, name);
            values.put(COLUMN_TELEPHONE, telephone);
            values.put(COLUMN_CARD, card);

            long result = bbdd.insert(TABLE_USER, null, values);
            return result != -1;
        }
        finally {
            if (bbdd != null) {
                bbdd.close();
            }
        }
    }

    public boolean updateUserDetails(String email, String password, String name, String telephone, String card) {
        bbdd = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_PASSWORD, password);
            values.put(COLUMN_NAME, name);
            values.put(COLUMN_TELEPHONE, telephone);
            values.put(COLUMN_CARD, card);

            int rows = bbdd.update(TABLE_USER, values, COLUMN_EMAIL + "=?", new String[]{email});
            return rows > 0;
        }
        finally {
            if (bbdd != null) {
                bbdd.close();
            }
        }
    }

    public List<String> getGenreNames() {
        List<String> genreNames = new ArrayList<>();
        bbdd = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = bbdd.query("genre", new String[]{"name"}, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    genreNames.add(cursor.getString(cursor.getColumnIndex("name")));
                }
                while (cursor.moveToNext());
            }
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            bbdd.close();
        }
        return genreNames;
    }

    public List<MultimediaContent> getFilteredContents(String selectedGenre, String selectedDemographicCategory) {
        List<MultimediaContent> filteredElements = new ArrayList<>();
        bbdd = this.getReadableDatabase();
        Cursor cursorContent = null;
        Cursor cursorGenre = null;

        String query = "SELECT id, title, author, demographic_category FROM multimedia_content";

        // Construir la consulta con los filtros si se han seleccionado valores en los Spinners
        if (!selectedGenre.equals("All") || !selectedDemographicCategory.equals("All")) {
            query += " WHERE";

            if (!selectedGenre.equals("All")) {
                query += " id IN (SELECT multimedia_content_id FROM multimedia_content_genre WHERE genre_id = (SELECT id FROM genre WHERE name = '" + selectedGenre + "'))";
            }

            if (!selectedDemographicCategory.equals("All")) {
                if (!selectedGenre.equals("All")) {
                    query += " AND";
                }
                query += " demographic_category = '" + selectedDemographicCategory + "'";
            }
        }

        try {
            cursorContent = bbdd.rawQuery(query, null);
            if (cursorContent.moveToFirst()) {
                do {
                    String title = cursorContent.getString(cursorContent.getColumnIndex("title"));
                    String author = cursorContent.getString(cursorContent.getColumnIndex("author"));
                    String demographicCategory = cursorContent.getString(cursorContent.getColumnIndex("demographic_category"));

                    cursorGenre = bbdd.rawQuery(
                            "SELECT g.name FROM genre g " +
                                    "JOIN multimedia_content_genre mcg ON g.id = mcg.genre_id " +
                                    "WHERE mcg.multimedia_content_id = (SELECT id FROM multimedia_content WHERE title = ?)",
                            new String[]{title}
                    );
                    List<String> genres = new ArrayList<>();
                    if (cursorGenre.moveToFirst()) {
                        do {
                            genres.add(cursorGenre.getString(cursorGenre.getColumnIndex("name")));
                        }
                        while (cursorGenre.moveToNext());
                    }
                    String genreString = TextUtils.join(", ", genres);

                    filteredElements.add(new MultimediaContent(title, genreString, author, demographicCategory));
                }
                while (cursorContent.moveToNext());
            }
        }
        finally {
            if (cursorContent != null) {
                cursorContent.close();
            }
            if (cursorGenre != null) {
                cursorGenre.close();
            }
            bbdd.close();
        }
        return filteredElements;
    }

}
