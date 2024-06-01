package com.example.meusflis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meusflis.R;
import com.example.meusflis.database.DatabaseHelper;

import java.util.HashMap;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private EditText etEmailProfile, etPasswordProfile, etNameProfile, etTelephoneProfile, etCardProfile;
    Spinner spBirthYearProfile;
    private Button btnBackProfile, btnSaveChangesProfile;
    private boolean isDataChanged = false;
    private DatabaseHelper databaseHelper;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        email = getIntent().getStringExtra("email");

        initializeViews();
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
        spBirthYearProfile = findViewById(R.id.spBirthYearProfile);
        btnBackProfile = findViewById(R.id.btnBackCatalogueFromProfile);
        btnSaveChangesProfile = findViewById(R.id.btnSaveChangesProfile);
    }



    /**
     * Carga los detalles del perfil del usuario desde la base de datos y los establece en
     * los campos EditText apropiados.
     */
    private void loadUserProfile() {
        databaseHelper = new DatabaseHelper(this);

        HashMap<String, String> userDetails = databaseHelper.getUserDetails(email);

        etEmailProfile.setText(userDetails.get("email"));
        etPasswordProfile.setText(userDetails.get("password"));
        etNameProfile.setText(userDetails.get("name"));
        etTelephoneProfile.setText(userDetails.get("telephone"));
        etCardProfile.setText(userDetails.get("card"));

        List<String> birthYears = databaseHelper.getAllBirthYearsForUser(email);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, birthYears);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBirthYearProfile.setAdapter(spinnerAdapter);
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
                boolean isUpdated = databaseHelper.updateUserDetails(email,
                        etPasswordProfile.getText().toString(),
                        etNameProfile.getText().toString(),
                        etTelephoneProfile.getText().toString(),
                        etCardProfile.getText().toString());

                if (isUpdated) {
                    btnSaveChangesProfile.setVisibility(View.GONE);
                    Toast.makeText(ProfileActivity.this, getString(R.string.updateSuccessfully), Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ProfileActivity.this, getString(R.string.updateFailed), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBackProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, CatalogueActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
    }
}
