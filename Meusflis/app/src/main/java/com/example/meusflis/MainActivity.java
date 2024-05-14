package com.example.meusflis;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private LinearLayout llSignIn, llSignUp, llForgotPass;
    private EditText etEmail, etPassword, etEmailSignUp, etPasswordSignUp, etNameSignUp, etTelephoneSignUp, etCardSignUp, etEmailForgot;
    private TextView tvForgotPassword, tvSignUp;
    private Button btnSignIn, btnSignUp, btnBackSignIn, btnSendForgotPassword, btnBackFromForgotPassword;
    private CheckBox chkRememberUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Evitar que el teclado aparezca automáticamente al iniciar la actividad
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        findIdViews();

        changeVisibilities();
    }

    private void findIdViews() {
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

        btnBackFromForgotPassword.setOnClickListener(view -> {
            llForgotPass.setVisibility(View.GONE);
            llSignIn.setVisibility(View.VISIBLE);
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
}