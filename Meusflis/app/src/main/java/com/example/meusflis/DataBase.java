package com.example.meusflis;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DataBase extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "BBDD.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USER = "user";
    private static final String COLUMN_EMAIL= "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_TELEPHONE = "telephone";
    private static final String COLUMN_CARD= "card";
    private static final String TABLE_CONTENT = "content";
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

        consulta = bbdd.query(TABLE_USER, null, COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?", new String[]{email, password}, null, null, null);

        boolean exists = consulta.getCount() > 0;

        consulta.close();
        bbdd.close();

        return exists;
    }
}
