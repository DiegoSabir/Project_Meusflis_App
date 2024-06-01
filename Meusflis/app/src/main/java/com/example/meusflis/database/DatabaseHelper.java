package com.example.meusflis.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import com.example.meusflis.model.MultimediaContent;

import java.util.ArrayList;
import java.util.HashMap;
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



    /**
     * Método para verificar si el usuario existe.
     * @param email El correo electrónico del usuario.
     * @param password La contraseña del usuario.
     * @return true si el usuario existe, false de lo contrario.
     */
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { DatabaseConstants.COLUMN_EMAIL };
        String selection = DatabaseConstants.COLUMN_EMAIL + " = ? AND " + DatabaseConstants.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = { email, password };
        Cursor cursor = null;

        try {
            cursor = db.query(
                    DatabaseConstants.TABLE_USER,
                    columns, selection, selectionArgs,
                    null,
                    null,
                    null);

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



    /**
     * Método para insertar un nuevo usuario.
     * @param email El correo electrónico del usuario.
     * @param password La contraseña del usuario.
     * @param name El nombre del usuario.
     * @param telephone El número de teléfono del usuario.
     * @param card La tarjeta del usuario.
     * @return true si el usuario fue insertado exitosamente, false de lo contrario.
     */
    public boolean insertUser(String email, String password, String name, String birthyear, String telephone, String card) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseConstants.COLUMN_EMAIL, email);
        values.put(DatabaseConstants.COLUMN_PASSWORD, password);
        values.put(DatabaseConstants.COLUMN_NAME, name);
        values.put(DatabaseConstants.COLUMN_BIRTHYEAR, birthyear);
        values.put(DatabaseConstants.COLUMN_TELEPHONE, telephone);
        values.put(DatabaseConstants.COLUMN_CARD, card);

        long result = db.insert(DatabaseConstants.TABLE_USER, null, values);
        db.close();

        return result != -1;
    }



    /**
     * Método para obtener la contraseña a través del email.
     * @param email El correo electrónico del usuario.
     * @return La contraseña del usuario, o null si no se encuentra.
     */
    public String getPasswordByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { DatabaseConstants.COLUMN_PASSWORD };
        String selection = DatabaseConstants.COLUMN_EMAIL + " = ?";
        String[] selectionArgs = { email };
        Cursor cursor = null;
        String password = null;

        try {
            cursor = db.query(
                    DatabaseConstants.TABLE_USER,
                    columns, selection, selectionArgs,
                    null,
                    null,
                    null);

            if (cursor != null && cursor.moveToFirst()) {
                int passwordIndex = cursor.getColumnIndexOrThrow(DatabaseConstants.COLUMN_PASSWORD);
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



    /**
     * Método para obtener las portadas de los contenidos multimedia con más "me gusta".
     * @param limit El número máximo de portadas a obtener.
     * @return Una lista de arrays de bytes que representan las portadas.
     */
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
                    int coverIndex = cursor.getColumnIndexOrThrow(DatabaseConstants.COLUMN_COVER);
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



    /**
     * Método para obtener todos los datos del usuario.
     * @param email El correo electrónico del usuario.
     * @return Un HashMap con los detalles del usuario.
     */
    public HashMap<String, String> getUserDetails(String email) {
        HashMap<String, String> userDetails = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(DatabaseConstants.TABLE_USER, null, DatabaseConstants.COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int emailIndex = cursor.getColumnIndexOrThrow(DatabaseConstants.COLUMN_EMAIL);
                int passwordIndex = cursor.getColumnIndexOrThrow(DatabaseConstants.COLUMN_PASSWORD);
                int nameIndex = cursor.getColumnIndexOrThrow(DatabaseConstants.COLUMN_NAME);
                int telephoneIndex = cursor.getColumnIndexOrThrow(DatabaseConstants.COLUMN_TELEPHONE);
                int cardIndex = cursor.getColumnIndexOrThrow(DatabaseConstants.COLUMN_CARD);

                if (emailIndex != -1) {
                    userDetails.put(DatabaseConstants.COLUMN_EMAIL, cursor.getString(emailIndex));
                }
                if (passwordIndex != -1) {
                    userDetails.put(DatabaseConstants.COLUMN_PASSWORD, cursor.getString(passwordIndex));
                }
                if (nameIndex != -1) {
                    userDetails.put(DatabaseConstants.COLUMN_NAME, cursor.getString(nameIndex));
                }
                if (telephoneIndex != -1) {
                    userDetails.put(DatabaseConstants.COLUMN_TELEPHONE, cursor.getString(telephoneIndex));
                }
                if (cardIndex != -1) {
                    userDetails.put(DatabaseConstants.COLUMN_CARD, cursor.getString(cardIndex));
                }
            }
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return userDetails;
    }



    /**
     * Método para obtener los datos del contenido multimedia.
     * @return Una lista de objetos MultimediaContent.
     */
    public ArrayList<MultimediaContent> getMultimediaContentData() {
        ArrayList<MultimediaContent> multimediaContents = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorContent = null;
        Cursor cursorGenre = null;

        String query = "SELECT id, title, author, demographic_category, cover, likes, url_manga, url_anime " +
                "FROM multimedia_content";

        try {
            cursorContent = db.rawQuery(query, null);
            if (cursorContent.moveToFirst()) {
                do {
                    int idIndex = cursorContent.getColumnIndexOrThrow("id");
                    int titleIndex = cursorContent.getColumnIndexOrThrow("title");
                    int authorIndex = cursorContent.getColumnIndexOrThrow("author");
                    int demographicCategoryIndex = cursorContent.getColumnIndexOrThrow("demographic_category");
                    int coverIndex = cursorContent.getColumnIndexOrThrow("cover");
                    int likesIndex = cursorContent.getColumnIndexOrThrow("likes");
                    int urlMangaIndex = cursorContent.getColumnIndexOrThrow("url_manga");
                    int urlAnimeIndex = cursorContent.getColumnIndexOrThrow("url_anime");

                    if (idIndex != -1 && titleIndex != -1 && authorIndex != -1 &&
                            demographicCategoryIndex != -1 && coverIndex != -1 && likesIndex != -1 &&
                            urlMangaIndex != -1 && urlAnimeIndex != -1) {

                        String id = cursorContent.getString(idIndex);
                        String title = cursorContent.getString(titleIndex);
                        String author = cursorContent.getString(authorIndex);
                        String demographicCategory = cursorContent.getString(demographicCategoryIndex);
                        byte[] coverBytes = cursorContent.getBlob(coverIndex);
                        int likes = cursorContent.getInt(likesIndex);
                        String urlManga = cursorContent.getString(urlMangaIndex);
                        String urlAnime = cursorContent.getString(urlAnimeIndex);

                        String genreQuery = "SELECT g.name AS genre " +
                                "FROM genre g " +
                                "JOIN multimedia_content_genre mcg ON g.id = mcg.genre_id " +
                                "WHERE mcg.multimedia_content_id = ?";
                        cursorGenre = db.rawQuery(genreQuery, new String[]{id});

                        List<String> genres = new ArrayList<>();
                        if (cursorGenre.moveToFirst()) {
                            do {
                                int genreIndex = cursorGenre.getColumnIndexOrThrow("genre");
                                if (genreIndex != -1) {
                                    genres.add(cursorGenre.getString(genreIndex));
                                }
                            }
                            while (cursorGenre.moveToNext());
                        }

                        String genreString = TextUtils.join(", ", genres);

                        Bitmap cover = BitmapFactory.decodeByteArray(coverBytes, 0, coverBytes.length);
                        MultimediaContent multimediaContent = new MultimediaContent(title, genreString, author, demographicCategory, cover, likes, urlManga, urlAnime);
                        multimediaContent.setUrlManga(urlManga);
                        multimediaContent.setUrlAnime(urlAnime);
                        multimediaContents.add(multimediaContent);
                    }
                }
                while (cursorContent.moveToNext());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (cursorContent != null) {
                cursorContent.close();
            }
            if (cursorGenre != null) {
                cursorGenre.close();
            }
            db.close();
        }
        return multimediaContents;
    }




    /**
     * Método para filtrar contenido multimedia por título.
     * @param titulo El título a buscar.
     * @return Una lista de objetos MultimediaContent que coinciden con el título.
     */
    public ArrayList<MultimediaContent> filterByTitle(String titulo) {
        ArrayList<MultimediaContent> multimediaContents = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorContent = null;
        Cursor cursorGenre = null;

        String query = "SELECT id, title, author, demographic_category, cover, likes, url_manga, url_anime " +
                "FROM multimedia_content " +
                "WHERE title LIKE '%" + titulo + "%'";

        try {
            cursorContent = db.rawQuery(query, null);
            if (cursorContent.moveToFirst()) {
                do {
                    int idIndex = cursorContent.getColumnIndexOrThrow("id");
                    int titleIndex = cursorContent.getColumnIndexOrThrow("title");
                    int authorIndex = cursorContent.getColumnIndexOrThrow("author");
                    int demographicCategoryIndex = cursorContent.getColumnIndexOrThrow("demographic_category");
                    int coverIndex = cursorContent.getColumnIndexOrThrow("cover");
                    int likesIndex = cursorContent.getColumnIndexOrThrow("likes");
                    int urlMangaIndex = cursorContent.getColumnIndexOrThrow("url_manga");
                    int urlAnimeIndex = cursorContent.getColumnIndexOrThrow("url_anime");

                    if (idIndex != -1 && titleIndex != -1 && authorIndex != -1 &&
                            demographicCategoryIndex != -1 && coverIndex != -1 && likesIndex != -1 &&
                            urlMangaIndex != -1 && urlAnimeIndex != -1) {

                        String id = cursorContent.getString(idIndex);
                        String title = cursorContent.getString(titleIndex);
                        String author = cursorContent.getString(authorIndex);
                        String demographicCategory = cursorContent.getString(demographicCategoryIndex);
                        byte[] coverBytes = cursorContent.getBlob(coverIndex);
                        int likes = cursorContent.getInt(likesIndex);
                        String urlManga = cursorContent.getString(urlMangaIndex);
                        String urlAnime = cursorContent.getString(urlAnimeIndex);

                        String genreQuery =
                                "SELECT g.name AS genre " +
                                "FROM genre g " +
                                "JOIN multimedia_content_genre mcg ON g.id = mcg.genre_id " +
                                "WHERE mcg.multimedia_content_id = ?";
                        cursorGenre = db.rawQuery(genreQuery, new String[]{id});

                        List<String> genres = new ArrayList<>();
                        if (cursorGenre.moveToFirst()) {
                            do {
                                int genreIndex = cursorGenre.getColumnIndexOrThrow("genre");
                                if (genreIndex != -1) {
                                    genres.add(cursorGenre.getString(genreIndex));
                                }
                            }
                            while (cursorGenre.moveToNext());
                        }

                        String genreString = TextUtils.join(", ", genres);

                        Bitmap cover = BitmapFactory.decodeByteArray(coverBytes, 0, coverBytes.length);
                        MultimediaContent multimediaContent = new MultimediaContent(title, genreString, author, demographicCategory, cover, likes, urlManga, urlAnime);
                        multimediaContents.add(multimediaContent);
                    }
                }
                while (cursorContent.moveToNext());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (cursorContent != null) {
                cursorContent.close();
            }
            if (cursorGenre != null) {
                cursorGenre.close();
            }
            db.close();
        }
        return multimediaContents;
    }



    /**
     * Método para actualizar los datos del usuario.
     * @param email El correo electrónico del usuario.
     * @param password La nueva contraseña del usuario.
     * @param name El nuevo nombre del usuario.
     * @param telephone El nuevo número de teléfono del usuario.
     * @param card La nueva tarjeta del usuario.
     * @return true si los detalles del usuario fueron actualizados exitosamente, false de lo contrario.
     */
    public boolean updateUserDetails(String email, String password, String name, String telephone, String card) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseConstants.COLUMN_PASSWORD, password);
            values.put(DatabaseConstants.COLUMN_NAME, name);
            values.put(DatabaseConstants.COLUMN_TELEPHONE, telephone);
            values.put(DatabaseConstants.COLUMN_CARD, card);
            int rows = db.update(DatabaseConstants.TABLE_USER, values,
                    DatabaseConstants.COLUMN_EMAIL + "=?", new String[]{email});
            return rows > 0;
        }
        finally {
            db.close();
        }
    }



    /**
     * Método para obtener todos los años de nacimiento del usuario actual.
     * @param email El correo electrónico del usuario actual.
     * @return Una lista de años de nacimiento únicos para el usuario actual.
     */
    public List<String> getAllBirthYearsForUser(String email) {
        List<String> birthYears = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT DISTINCT " + DatabaseConstants.COLUMN_BIRTHYEAR + " FROM " +
                    DatabaseConstants.TABLE_USER + " WHERE " + DatabaseConstants.COLUMN_EMAIL + " = ?";
            cursor = db.rawQuery(query, new String[]{email});

            if (cursor.moveToFirst()) {
                do {
                    String birthYear = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.COLUMN_BIRTHYEAR));
                    birthYears.add(birthYear);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return birthYears;
    }
}
