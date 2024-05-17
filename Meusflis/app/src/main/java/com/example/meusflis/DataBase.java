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
    private static final String COLUMN_EMAIL= "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_TELEPHONE = "telephone";
    private static final String COLUMN_CARD= "card";
    private static final String TABLE_CONTENT = "content";
    private final Context context;
    SQLiteDatabase bbdd;
    Cursor consulta;

    /**
     * Constructor para crear una instancia de la clase DataBase.
     * @param context El contexto de la aplicación.
     */
    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    /**
     * Obtiene una instancia de la base de datos en modo lectura.
     * @return Una instancia de SQLiteDatabase en modo lectura.
     */
    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

    /**
     * Obtiene una instancia de la base de datos en modo escritura.
     * @return Una instancia de SQLiteDatabase en modo escritura.
     */
    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    /**
     * Verifica si un usuario existe en la base de datos con el email y contraseña proporcionados.
     * @param email El email del usuario.
     * @param password La contraseña del usuario.
     * @return true si el usuario existe, false en caso contrario.
     */
    public boolean checkUser(String email, String password) {
        bbdd = this.getReadableDatabase();
        try{
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

    /**
     * Obtiene los detalles de un usuario a partir de su email.
     * @param email El email del usuario.
     * @return Un HashMap con los detalles del usuario.
     */
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

    /**
     * Agrega un nuevo usuario a la base de datos.
     * @param email El email del usuario.
     * @param password La contraseña del usuario.
     * @param name El nombre del usuario.
     * @param telephone El teléfono del usuario.
     * @param card El número de tarjeta del usuario.
     * @return true si el usuario fue agregado exitosamente, false en caso contrario.
     */
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

    /**
     * Actualiza los detalles de un usuario existente en la base de datos.
     * @param email El email del usuario.
     * @param password La nueva contraseña del usuario.
     * @param name El nuevo nombre del usuario.
     * @param telephone El nuevo teléfono del usuario.
     * @param card El nuevo número de tarjeta del usuario.
     * @return true si los detalles del usuario fueron actualizados exitosamente, false en caso contrario.
     */
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

    /**
     * Obtiene una lista de elementos de contenido de la base de datos.
     * @return Una lista de objetos `ListElement` que representan el contenido de la tabla content
     */
    public List<ListElement> getContents() {
        List<ListElement> elements = new ArrayList<>();
        bbdd = this.getReadableDatabase();
        Cursor cursorContent = null;
        Cursor cursorGenre = null;

        try {
            cursorContent = bbdd.rawQuery("SELECT title, author, demography FROM content", null);
            if (cursorContent.moveToFirst()) {
                do {
                    String title = cursorContent.getString(cursorContent.getColumnIndex("title"));
                    String author = cursorContent.getString(cursorContent.getColumnIndex("author"));
                    String demography = cursorContent.getString(cursorContent.getColumnIndex("demography"));

                    cursorGenre = bbdd.rawQuery("SELECT g.name FROM genre g JOIN content_genre cg ON g.id_genre = cg.id_genre WHERE cg.title = ?", new String[]{title});
                    List<String> genres = new ArrayList<>();
                    if (cursorGenre.moveToFirst()) {
                        do {
                            genres.add(cursorGenre.getString(cursorGenre.getColumnIndex("name")));
                        }
                        while (cursorGenre.moveToNext());
                    }
                    String genreString = TextUtils.join(", ", genres);

                    elements.add(new ListElement("#FFFFFF", title, genreString, author, demography));
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
        return elements;
    }
}
