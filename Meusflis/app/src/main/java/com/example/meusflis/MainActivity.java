package com.example.meusflis;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private LinearLayout llSignIn, llSignUp, llForgotPass;
    private EditText etEmail, etPassword, etEmailSignUp, etPasswordSignUp, etNameSignUp,
            etTelephoneSignUp, etCardSignUp, etEmailForgot;
    private TextView tvForgotPassword, tvSignUp;
    private Button btnSignIn, btnSignUp, btnBackSignIn, btnSendForgotPassword, btnBackFromForgotPassword;
    private CheckBox chkRememberUsername;
    private SharedPreferences loginSharedPreferences;
    private static final int MAX_FAILED_ATTEMPTS = 3;
    private int failedAttempts = 0;
    DataBase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Evitar que el teclado aparezca automáticamente al iniciar la actividad
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initViews();

        String savedEmail = loginSharedPreferences.getString("email", "");
        if (!savedEmail.isEmpty()) {
            etEmail.setText(savedEmail);
        }
        else {
            etEmail.setHint("Email");
        }

        //Comprobacion de conexion a la base de datos
        database = new DataBase(MainActivity.this);
        if (database.getReadableDatabase() != null) {
            Toast.makeText(MainActivity.this, "La base de datos está conectada", Toast.LENGTH_SHORT).show();
        }

        changeVisibilities();

        setupListeners();
    }

    private void initViews() {
        llSignIn = findViewById(R.id.llSignIn);
        llSignUp = findViewById(R.id.llSignUp);
        llForgotPass = findViewById(R.id.llForgotPass);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etEmailSignUp = findViewById(R.id.etEmailSignUp);
        etPasswordSignUp = findViewById(R.id.etPasswordSignUp);
        etNameSignUp = findViewById(R.id.etNameSignUp);
        etTelephoneSignUp = findViewById(R.id.etTelephoneSignUp);
        etCardSignUp = findViewById(R.id.etCardSignUp);
        etEmailForgot = findViewById(R.id.etEmailForgot);

        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUp = findViewById(R.id.tvSignUp);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnBackSignIn = findViewById(R.id.btnBackSignIn);
        btnSendForgotPassword = findViewById(R.id.btnSendForgotPassword);
        btnBackFromForgotPassword = findViewById(R.id.btnBackFromForgotPassword);

        chkRememberUsername = findViewById(R.id.chkRememberUsername);
    }

    private void changeVisibilities(){
        tvForgotPassword.setOnClickListener(view -> {
            llForgotPass.setVisibility(View.VISIBLE);
            llSignIn.setVisibility(View.GONE);
        });

        btnBackFromForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llForgotPass.setVisibility(View.GONE);
                llSignIn.setVisibility(View.VISIBLE);
            }
        });

        tvSignUp.setOnClickListener(view -> {
            llSignUp.setVisibility(View.VISIBLE);
            llSignIn.setVisibility(View.GONE);
        });

        btnBackSignIn.setOnClickListener(view -> {
            llSignUp.setVisibility(View.GONE);
            llSignIn.setVisibility(View.VISIBLE);
        });
    }

    private void setupListeners(){
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                // Guardar el email en las preferencias compartidas si el CheckBox está marcado
                if (chkRememberUsername.isChecked()) {
                    SharedPreferences.Editor editor = loginSharedPreferences.edit();
                    editor.putString("email", email);
                    editor.apply();
                }

                if (database.checkUser(email, password)) {
                    Intent intent = new Intent(MainActivity.this, Catalogue.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                }

                else {
                    failedAttempts++;
                    if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
                        showMaxAttemptsDialog();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Incorrect credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmailSignUp.getText().toString();
                String password = etPasswordSignUp.getText().toString();
                String name = etNameSignUp.getText().toString();
                String telephone = etTelephoneSignUp.getText().toString();
                String card = etCardSignUp.getText().toString();

                if (email.isEmpty() || password.isEmpty() || name.isEmpty() || telephone.isEmpty() || card.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please, complete all fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (database.addUser(email, password, name, telephone, card)) {
                        Toast.makeText(MainActivity.this, "User successfully added!", Toast.LENGTH_SHORT).show();

                        // Limpiar campos de entrada después del registro
                        etEmailSignUp.setText("");
                        etPasswordSignUp.setText("");
                        etNameSignUp.setText("");
                        etTelephoneSignUp.setText("");
                        etCardSignUp.setText("");

                        // Cambiar a la vista de inicio de sesión
                        llSignUp.setVisibility(View.GONE);
                        llSignIn.setVisibility(View.VISIBLE);
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Error adding user", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void showMaxAttemptsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Número máximo de intentos alcanzado")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Se ha excedido el número máximo de intentos de inicio de sesión fallidos.\n " +
                        "La aplicación se cerrará.")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        failedAttempts = 0;
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }
}