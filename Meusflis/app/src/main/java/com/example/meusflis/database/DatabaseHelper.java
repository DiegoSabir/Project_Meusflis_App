package com.example.meusflis.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import com.example.meusflis.model.MultimediaContent;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "Database.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }



    // Método para verificar si el usuario existe
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { DatabaseConstants.COLUMN_EMAIL };
        String selection = DatabaseConstants.COLUMN_EMAIL + " = ? AND " + DatabaseConstants.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = { email, password };
        Cursor cursor = null;
        try {
            cursor = db.query(
                    DatabaseConstants.TABLE_USER, columns, selection, selectionArgs, null, null, null);

            int cursorCount = cursor.getCount();
            cursor.close();
            db.close();

            if (cursorCount > 0) {
                return true;
            }
            return false;
        }
        catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
            Log.e("DatabaseHelper", "Error checking user", e);
            return false;
        }
    }



    // Método para insertar un nuevo usuario
    public boolean insertUser(String email, String password, String name, String telephone, String card) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.COLUMN_EMAIL, email);
        values.put(DatabaseConstants.COLUMN_PASSWORD, password);
        values.put(DatabaseConstants.COLUMN_NAME, name);
        values.put(DatabaseConstants.COLUMN_TELEPHONE, telephone);
        values.put(DatabaseConstants.COLUMN_CARD, card);

        long result = db.insert(DatabaseConstants.TABLE_USER, null, values);
        db.close();

        return result != -1;
    }



    public String getPasswordByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { DatabaseConstants.COLUMN_PASSWORD };
        String selection = DatabaseConstants.COLUMN_EMAIL + " = ?";
        String[] selectionArgs = { email };
        Cursor cursor = null;
        String password = null;

        try {
            cursor = db.query(DatabaseConstants.TABLE_USER, columns, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int passwordIndex = cursor.getColumnIndex(DatabaseConstants.COLUMN_PASSWORD);
                if (passwordIndex != -1) {
                    password = cursor.getString(passwordIndex);
                }
            }
        }
        catch (Exception e) {
            Log.e("DatabaseHelper", "Error retrieving password", e);
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return password;
    }



    public List<byte[]> getTopLikedContentCovers(int limit) {
        List<byte[]> coverList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT " + DatabaseConstants.COLUMN_COVER + " FROM " +
                    DatabaseConstants.TABLE_MULTIMEDIA_CONTENT +
                    " ORDER BY " + DatabaseConstants.COLUMN_LIKES + " DESC LIMIT ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(limit)});

            if (cursor.moveToFirst()) {
                do {
                    int coverIndex = cursor.getColumnIndex(DatabaseConstants.COLUMN_COVER);
                    if (coverIndex != -1) {
                        coverList.add(cursor.getBlob(coverIndex));
                    }
                }
                while (cursor.moveToNext());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return coverList;
    }
}
