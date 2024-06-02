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
    private Spinner spBirthYearProfile;
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
     * Inicializa las vistas de la actividad y deshabilita el campo de email.
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

        etEmailProfile.setEnabled(false);
    }



    /**
     * Carga el perfil del usuario desde la base de datos y lo muestra en las vistas.
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
     * Añade TextWatchers a los campos de texto para detectar cambios en los datos del perfil.
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

        etPasswordProfile.addTextChangedListener(watcher);
        etNameProfile.addTextChangedListener(watcher);
        etTelephoneProfile.addTextChangedListener(watcher);
        etCardProfile.addTextChangedListener(watcher);
    }



    /**
     * Configura los botones de la actividad para guardar los cambios o regresar a la actividad anterior.
     */
    private void configureButtons() {
        btnSaveChangesProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
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



    /**
     * Valida la entrada del usuario en los campos de teléfono y tarjeta.
     *
     * @return true si la entrada es válida, false de lo contrario.
     */
    private boolean validateInput() {
        String telephone = etTelephoneProfile.getText().toString();
        String card = etCardProfile.getText().toString();

        if (!isValidTelephone(telephone)) {
            etTelephoneProfile.setError(getString(R.string.validationTelephone));
            Toast.makeText(ProfileActivity.this, getString(R.string.validationTelephoneMessage), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isValidCardNumber(card)) {
            etCardProfile.setError(getString(R.string.validationCard));
            Toast.makeText(ProfileActivity.this, getString(R.string.validationCardMessage), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



    /**
     * Verifica si el número de teléfono proporcionado es válido.
     *
     * @param telephone El número de teléfono a validar.
     * @return true si el número de teléfono es válido, false de lo contrario.
     */
    private boolean isValidTelephone(String telephone) {
        telephone = telephone.replaceAll("\\s", "");
        return telephone.matches("\\+\\d{1,3}\\d{9}");
    }



    /**
     * Verifica si el número de tarjeta proporcionado es válido usando el algoritmo de Luhn.
     *
     * @param cardNumber El número de tarjeta a validar.
     * @return true si el número de tarjeta es válido, false de lo contrario.
     */
    private boolean isValidCardNumber(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
}
