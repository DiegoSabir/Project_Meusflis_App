package com.example.meusflis;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class Profile extends AppCompatActivity {
    private EditText etEmailProfile, etPasswordProfile, etNameProfile, etTelephoneProfile, etCardProfile;
    private Button btnBackProfile, btnSaveChangesProfile;
    private boolean isDataChanged = false;
    private Database bbdd;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initializeViews();
        getIntentData();
        loadUserProfile();
        addTextWatchers();
        configureButtons();
    }

    /**
     * Inicializa las vistas vinculándolas con sus respectivos ID en el diseño.
     */
    private void initializeViews() {
        etEmailProfile = findViewById(R.id.etEmailProfile);
        etPasswordProfile = findViewById(R.id.etPasswordProfile);
        etNameProfile = findViewById(R.id.etNameProfile);
        etTelephoneProfile = findViewById(R.id.etTelephoneProfile);
        etCardProfile = findViewById(R.id.etCardProfile);
        btnBackProfile = findViewById(R.id.btnBackProfile);
        btnSaveChangesProfile = findViewById(R.id.btnSaveChangesProfile);
    }

    /**
     * Recupera los datos de correo electrónico del Intent que inició esta Actividad.
     */
    private void getIntentData() {
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
    }

    /**
     * Carga los detalles del perfil del usuario desde la base de datos y los establece en
     * los campos EditText apropiados.
     */
    private void loadUserProfile() {
        bbdd = new Database(this);
        HashMap<String, String> userDetails = bbdd.getUserDetails(email);
        etEmailProfile.setText(userDetails.get("email"));
        etPasswordProfile.setText(userDetails.get("password"));
        etNameProfile.setText(userDetails.get("name"));
        etTelephoneProfile.setText(userDetails.get("telephone"));
        etCardProfile.setText(userDetails.get("card"));
    }

    /**
     * Agrega listeners de TextWatcher a los campos de EditText para monitorear los cambios
     * y configurar la visibilidad del botón de guardar.
     */
    private void addTextWatchers() {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isDataChanged = true;
                btnSaveChangesProfile.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
        etEmailProfile.addTextChangedListener(watcher);
        etPasswordProfile.addTextChangedListener(watcher);
        etNameProfile.addTextChangedListener(watcher);
        etTelephoneProfile.addTextChangedListener(watcher);
        etCardProfile.addTextChangedListener(watcher);
    }

    /**
     * Configura los detectores de clic en los botones para guardar los cambios y volver al catálogo.
     */
    private void configureButtons() {
        btnSaveChangesProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileChanges();
            }
        });

        btnBackProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToCatalogue();
            }
        });
    }

    /**
     * Guarda los cambios del perfil en la base de datos y muestra un mensaje Toast que
     * indica el éxito o el fallo.
     */
    private void saveProfileChanges() {
        boolean isUpdated = bbdd.updateUserDetails(email,
                etPasswordProfile.getText().toString(),
                etNameProfile.getText().toString(),
                etTelephoneProfile.getText().toString(),
                etCardProfile.getText().toString());

        if (isUpdated) {
            btnSaveChangesProfile.setVisibility(View.GONE);
            Toast.makeText(Profile.this, getString(R.string.profile_update_success), Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(Profile.this, getString(R.string.profile_update_failed), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Vuelve a la actividad del Catálogo y pasa el correo electrónico como un extra en la Intención.
     */
    private void navigateToCatalogue() {
        Intent intent = new Intent(Profile.this, Catalogue.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }
}
