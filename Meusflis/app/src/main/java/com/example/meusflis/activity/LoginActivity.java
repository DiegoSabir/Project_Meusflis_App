package com.example.meusflis.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
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
    private static final String PREF_ATTEMPTS = "attempts";
    private static final int MAX_ATTEMPTS = 5;

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
     * Método para inicializar las vistas de la actividad.
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
     * Método para cargar las preferencias guardadas.
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
     * Método para configurar los listeners de los elementos de la UI.
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
                    Toast.makeText(LoginActivity.this, getString(R.string.btnSignUpSuccessful), Toast.LENGTH_SHORT).show();
                    savePreferences(email);
                    resetAttempts();
                    Intent intent = new Intent(LoginActivity.this, CatalogueActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                }
                else {
                    incrementAttempts();
                }
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleForgotPassword();
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
     * Método para guardar las preferencias del usuario.
     * @param email Email del usuario.
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
     * Método para incrementar el conteo de intentos fallidos de inicio de sesión.
     */
    private void incrementAttempts() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int attempts = preferences.getInt(PREF_ATTEMPTS, 0) + 1;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PREF_ATTEMPTS, attempts);
        editor.apply();

        if (attempts == MAX_ATTEMPTS - 1) {
            showPenultimateAttemptDialog();
        }
        else if (attempts >= MAX_ATTEMPTS) {
            showMaxAttemptsReachedDialog();
        }
        else {
            Toast.makeText(LoginActivity.this, getString(R.string.btnSignUpFailed), Toast.LENGTH_SHORT).show();
        }
    }



    /**
     * Método para reiniciar el conteo de intentos fallidos de inicio de sesión.
     */
    private void resetAttempts() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PREF_ATTEMPTS, 0);
        editor.apply();
    }



    /**
     * Método para mostrar un diálogo advirtiendo del penúltimo intento de inicio de sesión.
     */
    private void showPenultimateAttemptDialog() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.baseline_warning_24)
                .setTitle(getString(R.string.titleAttemptDialog))
                .setMessage(getString(R.string.messageAttemptDialog))
                .setPositiveButton(getString(R.string.optionTryAttemptDialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton(getString(R.string.optionExitAttemptDialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .show();
    }



    /**
     * Método para mostrar un diálogo cuando se ha alcanzado el máximo número de intentos de inicio de sesión.
     */
    private void showMaxAttemptsReachedDialog() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.baseline_warning_24)
                .setTitle(getString(R.string.titleAttemptDialog))
                .setMessage(getString(R.string.lastMessageAttemptDialog))
                .setPositiveButton(getString(R.string.optionOkAttemptDialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .show();
    }



    /**
     * Método para manejar la acción de "Olvidé mi contraseña".
     */
    private void handleForgotPassword() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int attempts = preferences.getInt(PREF_ATTEMPTS, 0);

        if (attempts >= MAX_ATTEMPTS) {
            showMaxAttemptsReachedDialog();
        }
        else {
            Intent intent = new Intent(LoginActivity.this, ForgetActivity.class);
            startActivity(intent);
        }
    }



    /**
     * Método para validar el campo de email.
     * @return true si el email es válido, false en caso contrario.
     */
    public Boolean validateEmail() {
        String validation = etLoginEmail.getText().toString();
        if (validation.isEmpty()) {
            etLoginEmail.setError(getString(R.string.validateEmailIsEmpty));
            return false;
        }
        else {
            etLoginEmail.setError(null);
            return true;
        }
    }



    /**
     * Método para validar el campo de contraseña.
     * @return true si la contraseña es válida, false en caso contrario.
     */
    public Boolean validatePassword() {
        String validation = etLoginPassword.getText().toString();
        if (validation.isEmpty()) {
            etLoginPassword.setError(getString(R.string.validatePasswordIsEmpty));
            return false;
        }
        else {
            etLoginPassword.setError(null);
            return true;
        }
    }
}
