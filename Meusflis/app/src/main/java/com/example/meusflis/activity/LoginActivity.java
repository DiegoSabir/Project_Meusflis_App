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

    private void initViews() {
        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        chkRemember = findViewById(R.id.chkRemember);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUpRedirect = findViewById(R.id.tvSignUpRedirect);
    }

    private void loadPreferences() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedEmail = preferences.getString(PREF_EMAIL, null);
        if (savedEmail != null) {
            etLoginEmail.setText(savedEmail);
            chkRemember.setChecked(true);
        }
    }

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
                } else {
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

    private void savePreferences(String email) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if (chkRemember.isChecked()) {
            editor.putString(PREF_EMAIL, email);
        } else {
            editor.remove(PREF_EMAIL);
        }
        editor.apply();
    }

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

    private void resetAttempts() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PREF_ATTEMPTS, 0);
        editor.apply();
    }

    private void showPenultimateAttemptDialog() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.baseline_warning_24)
                .setTitle("Warning")
                .setMessage("You have one try left.\n You can close the app to reset the number of attempts or try again.")
                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .show();
    }

    private void showMaxAttemptsReachedDialog() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.baseline_warning_24)
                .setTitle("Warning")
                .setMessage("Maximum number of attempts reached.\n The application will close.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .show();
    }

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
