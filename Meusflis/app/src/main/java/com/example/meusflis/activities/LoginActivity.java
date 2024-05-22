package com.example.meusflis.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meusflis.R;
import com.example.meusflis.database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {
    EditText etLoginEmail, etLoginPassword;
    Button btnSignIn;
    TextView tvSignUpRedirect;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseHelper = new DatabaseHelper(this);

        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPassword);

        tvSignUpRedirect = findViewById(R.id.tvSignUpRedirect);

        btnSignIn = findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUsername() | !validatePassword()){
                }
                else {
                    String email = etLoginEmail.getText().toString();
                    String password = etLoginPassword.getText().toString();

                    boolean userExists = databaseHelper.checkUser(email, password);

                    if (userExists) {
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        // Navegar a la siguiente actividad
                        // Intent intent = new Intent(LoginActivity.this, NextActivity.class);
                        // startActivity(intent);
                        // finish();
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                }
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

    public Boolean validateUsername(){
        String validation = etLoginEmail.getText().toString();
        if (validation.isEmpty()){
            etLoginEmail.setError("Username cannot be empty");
            return false;
        }
        else {
            etLoginEmail.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(){
        String validation = etLoginPassword.getText().toString();
        if (validation.isEmpty()){
            etLoginPassword.setError("Password cannot be empty");
            return false;
        }
        else {
            etLoginPassword.setError(null);
            return true;
        }
    }
}