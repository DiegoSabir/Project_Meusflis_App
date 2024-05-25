package com.example.meusflis.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meusflis.R;
import com.example.meusflis.database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    EditText etLoginEmail, etLoginPassword;
    Button btnSignIn;
    CheckBox chkRemember;
    TextView tvSignUpRedirect, tvForgotPassword;
    DatabaseHelper databaseHelper;
    private static final String PREFS_NAME = "prefs";
    private static final String PREF_EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseHelper = new DatabaseHelper(this);

        // Inicializar vistas
        initViews();

        // Cargar preferencias
        loadPreferences();

        // Configurar listeners
        setListeners();
    }

    /**
     * Inicializa todas las vistas.
     */
    private void initViews() {
        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        chkRemember = findViewById(R.id.chkRemember);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUpRedirect = findViewById(R.id.tvSignUpRedirect);
    }

    /**
     * Carga las preferencias del archivo de SharedPreferences y rellena el campo de email si hay un email guardado.
     * Marca el checkbox si hay un email guardado.
     */
    private void loadPreferences() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedEmail = preferences.getString(PREF_EMAIL, null);
        if (savedEmail != null) {
            etLoginEmail.setText(savedEmail);
            chkRemember.setChecked(true);
        }
    }


    /**
     * Configura todos los OnClickListener.
     */
    private void setListeners() {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateEmail() || !validatePassword()) {
                    return;
                }

                String email = etLoginEmail.getText().toString();
                String password = etLoginPassword.getText().toString();

                boolean userExists = databaseHelper.checkUser(email, password);

                if (userExists) {
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    savePreferences(email);
                    Intent intent = new Intent(LoginActivity.this, CatalogueActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgetActivity.class);
                startActivity(intent);
            }
        });

        tvSignUpRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }


    /**
     * Guarda o elimina el email en SharedPreferences basado en si el checkbox está marcado o no.
     * @param email El email del usuario que se guardará o eliminará en las preferencias.
     */
    private void savePreferences(String email) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if (chkRemember.isChecked()) {
            editor.putString(PREF_EMAIL, email);
        }
        else {
            editor.remove(PREF_EMAIL);
        }
        editor.apply();
    }

    /**
     * Valida que el campo de email no esté vacío y muestra un error si es necesario.
     * @return true si el email es válido, false en caso contrario.
     */
    public Boolean validateEmail() {
        String validation = etLoginEmail.getText().toString();
        if (validation.isEmpty()) {
            etLoginEmail.setError("Username cannot be empty");
            return false;
        }
        else {
            etLoginEmail.setError(null);
            return true;
        }
    }

    /**
     * Valida que el campo de contraseña no esté vacío y muestra un error si es necesario.
     * @return true si la contraseña es válida, false en caso contrario.
     */
    public Boolean validatePassword() {
        String validation = etLoginPassword.getText().toString();
        if (validation.isEmpty()) {
            etLoginPassword.setError("Password cannot be empty");
            return false;
        }
        else {
            etLoginPassword.setError(null);
            return true;
        }
    }
}
