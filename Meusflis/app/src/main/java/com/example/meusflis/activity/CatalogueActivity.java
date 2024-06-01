package com.example.meusflis.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.meusflis.R;
import com.example.meusflis.adapter.MultimediaContentAdapter;
import com.example.meusflis.database.DatabaseHelper;
import com.example.meusflis.model.MultimediaContent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CatalogueActivity extends MenuActivity {

    ImageSlider isTopMultimediaContent;
    SearchView svSearchMultimediaContent;
    ListView lvCatalogue;
    ArrayList<MultimediaContent> listContent;
    String email;
    private static final int TELEPHONE_CALL = 1;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);

        email = getIntent().getStringExtra("email");

        databaseHelper = new DatabaseHelper(this);

        initializeViews();
        saveEmail();
        loadTopContentCovers();
        setupMultimediaContentList();
        setupSearchView();
        setupContextMenu();
    }



    /**
     * Método para inicializar las vistas de la actividad.
     */
    private void initializeViews() {
        isTopMultimediaContent = findViewById(R.id.isTopMultimediaContent);
        svSearchMultimediaContent = findViewById(R.id.svSearchMultimediaContent);
        lvCatalogue = findViewById(R.id.lvCatalogue);
    }



    /**
     * Método para guardar el email en las preferencias compartidas.
     */
    private void saveEmail() {
        SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
        email = preferences.getString("email", "");
    }



    /**
     * Método para cargar las portadas de los contenidos más valorados en el ImageSlider.
     */
    private void loadTopContentCovers() {
        List<byte[]> topContentCovers = databaseHelper.getTopLikedContentCovers(5);

        if (topContentCovers == null) {
            topContentCovers = new ArrayList<>();
        }

        ArrayList<SlideModel> slideModels = new ArrayList<>();

        for (byte[] coverImage : topContentCovers) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(coverImage, 0, coverImage.length);
            Uri imageUri = saveImageToCache(bitmap);
            if (imageUri != null) {
                slideModels.add(new SlideModel(imageUri.toString(), ScaleTypes.FIT));
            }
        }
        isTopMultimediaContent.setImageList(slideModels, ScaleTypes.FIT);
    }



    /**
     * Método para guardar una imagen en el caché y obtener su URI.
     * @param bitmap El bitmap de la imagen a guardar.
     * @return URI de la imagen guardada.
     */
    private Uri saveImageToCache(Bitmap bitmap) {
        try {
            File cacheDir = getCacheDir();
            File tempFile = File.createTempFile("cover", ".png", cacheDir);
            FileOutputStream fos = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            return Uri.fromFile(tempFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    /**
     * Método para obtener datos del contenido multimedia de la base de datos.
     */
    private void setupMultimediaContentList() {
        listContent = databaseHelper.getMultimediaContentData();
        MultimediaContentAdapter multimediaContentAdapter = new MultimediaContentAdapter(this, listContent);
        lvCatalogue.setAdapter(multimediaContentAdapter);
    }



    /**
     * Método para configurar el SearchView y su listener.
     */
    private void setupSearchView() {
        svSearchMultimediaContent.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<MultimediaContent> filteredList = databaseHelper.filterByTitle(newText);
                MultimediaContentAdapter multimediaContentAdapter = new MultimediaContentAdapter(CatalogueActivity.this, filteredList);
                lvCatalogue.setAdapter(multimediaContentAdapter);
                return false;
            }
        });
    }



    /**
     * Método para manejar la selección de un ítem del menú.
     * @param item El ítem del menú seleccionado.
     * @return true si el ítem fue manejado, false de lo contrario.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.option_profile:
                intent = new Intent(CatalogueActivity.this, ProfileActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
                return true;

            case R.id.option_english:
                setLocale("en");
                return true;

            case R.id.option_spanish:
                setLocale("es");
                return true;

            case R.id.option_galician:
                setLocale("gl");
                return true;

            case R.id.option_japanese:
                setLocale("ja");
                return true;

            case R.id.option_contact:
                makePhoneCall();
                return true;

            case R.id.option_exit:
                showExitDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    /**
     * Método para realizar una llamada telefónica.
     */
    private void makePhoneCall() {
        String phoneNumber = getResources().getString(R.string.contactUsPhone);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, TELEPHONE_CALL);
        }
    }



    /**
     * Método para manejar el resultado de la solicitud de permisos.
     * @param requestCode El código de solicitud.
     * @param permissions Los permisos solicitados.
     * @param grantResults Los resultados de la solicitud de permisos.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == TELEPHONE_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            }
            else {
                Toast.makeText(this, getString(R.string.callPermisionDenied), Toast.LENGTH_SHORT).show();
            }
        }
    }



    /**
     * Método para mostrar un diálogo de confirmación al salir de la aplicación.
     */
    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_menu_close_clear_cancel)
                .setTitle(getString(R.string.exitDialogTitle))
                .setMessage(getString(R.string.exitDialogMessage))
                .setPositiveButton(getString(R.string.optionExit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .setNegativeButton(getString(R.string.optionLogOut), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(CatalogueActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    }
                })
                .show();
    }



    /**
     * Método para configurar el menú contextual para los elementos del ListView.
     */
    private void setupContextMenu() {
        lvCatalogue.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                getMenuInflater().inflate(R.menu.contextual_menu, menu);
            }
        });
    }



    /**
     * Método para manejar la selección de un ítem del menú contextual.
     * @param item El ítem del menú contextual seleccionado.
     * @return true si el ítem fue manejado, false de lo contrario.
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        MultimediaContent selectedContent = (MultimediaContent) lvCatalogue.getItemAtPosition(item.getGroupId());

        switch (item.getItemId()) {
            case R.id.ctx_manga:
                openWebPage(selectedContent.getUrlManga());
                break;

            case R.id.ctx_anime:
                openWebPage(selectedContent.getUrlAnime());
                break;
        }
        return true;
    }



    /**
     * Método para abrir una página web en un navegador.
     * @param url La URL de la página web a abrir.
     */
    private void openWebPage(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
