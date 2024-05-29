package com.example.meusflis.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meusflis.R;

import java.util.Locale;

public class MenuActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }



    /**
     * Método para inflar el menú de opciones.
     * @param menu El menú de opciones a inflar.
     * @return true si el menú fue creado exitosamente, false de lo contrario.
     */
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }



    /**
     * Método para cambiar la configuración de idioma de la aplicación.
     * @param lang El código del idioma a configurar.
     */
    protected void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Resources res = getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        DisplayMetrics dm = res.getDisplayMetrics();
        res.updateConfiguration(config, dm);

        Intent refresh = new Intent(this, CatalogueActivity.class);
        startActivity(refresh);
        finish();
    }
}
