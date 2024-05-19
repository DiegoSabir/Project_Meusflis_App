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
    private DataBase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initViews();

        checkSavedEmail();

        checkDatabaseConnection();

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

    private void checkSavedEmail() {
        String savedEmail = loginSharedPreferences.getString("email", "");
        if (!savedEmail.isEmpty()) {
            etEmail.setText(savedEmail);
        }
        else {
            etEmail.setHint("Email");
        }
    }

    private void checkDatabaseConnection() {
        database = new DataBase(MainActivity.this);
        if (database.getReadableDatabase() != null) {
            Toast.makeText(MainActivity.this, "Database connected", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupListeners() {
        setupVisibilityListeners();
        setupSignInButtonListener();
        setupSignUpButtonListener();
    }

    private void setupVisibilityListeners() {
        tvForgotPassword.setOnClickListener(view -> showForgotPasswordLayout());
        btnBackFromForgotPassword.setOnClickListener(view -> showSignInLayout());
        tvSignUp.setOnClickListener(view -> showSignUpLayout());
        btnBackSignIn.setOnClickListener(view -> showSignInLayout());
    }

    private void showForgotPasswordLayout() {
        llForgotPass.setVisibility(View.VISIBLE);
        llSignIn.setVisibility(View.GONE);
    }

    private void showSignInLayout() {
        llForgotPass.setVisibility(View.GONE);
        llSignIn.setVisibility(View.VISIBLE);
        llSignUp.setVisibility(View.GONE);
    }

    private void showSignUpLayout() {
        llSignUp.setVisibility(View.VISIBLE);
        llSignIn.setVisibility(View.GONE);
    }

    private void setupSignInButtonListener() {
        btnSignIn.setOnClickListener(view -> handleSignIn());
    }

    private void handleSignIn() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (chkRememberUsername.isChecked()) {
            saveEmailToPreferences(email);
        }

        if (database.checkUser(email, password)) {
            navigateToCatalogue(email);
        }
        else {
            handleFailedSignInAttempt();
        }
    }

    private void saveEmailToPreferences(String email) {
        SharedPreferences.Editor editor = loginSharedPreferences.edit();
        editor.putString("email", email);
        editor.apply();
    }

    private void navigateToCatalogue(String email) {
        Intent intent = new Intent(MainActivity.this, Catalogue.class);
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
    }

    private void handleFailedSignInAttempt() {
        failedAttempts++;
        if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
            showMaxAttemptsDialog();
        } else {
            Toast.makeText(MainActivity.this, "Incorrect credentials", Toast.LENGTH_SHORT).show();
        }
    }

    private void showMaxAttemptsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Maximum number of attempts reached")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("The maximum number of failed login attempts has been exceeded.\nThe application will close.")
                .setPositiveButton("Ok", (dialog, which) -> {
                    failedAttempts = 0;
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    private void setupSignUpButtonListener() {
        btnSignUp.setOnClickListener(view -> handleSignUp());
    }

    private void handleSignUp() {
        String email = etEmailSignUp.getText().toString();
        String password = etPasswordSignUp.getText().toString();
        String name = etNameSignUp.getText().toString();
        String telephone = etTelephoneSignUp.getText().toString();
        String card = etCardSignUp.getText().toString();

        if (validateSignUpFields(email, password, name, telephone, card)) {
            if (database.addUser(email, password, name, telephone, card)) {
                showSignUpSuccessMessage();
                clearSignUpFields();
                showSignInLayout();
            }
            else {
                showSignUpErrorMessage();
            }
        }
        else {
            showIncompleteFieldsMessage();
        }
    }

    private boolean validateSignUpFields(String email, String password, String name, String telephone, String card) {
        return !email.isEmpty() && !password.isEmpty() && !name.isEmpty() && !telephone.isEmpty() && !card.isEmpty();
    }

    private void showSignUpSuccessMessage() {
        Toast.makeText(MainActivity.this, "User successfully added!", Toast.LENGTH_SHORT).show();
    }

    private void clearSignUpFields() {
        etEmailSignUp.setText("");
        etPasswordSignUp.setText("");
        etNameSignUp.setText("");
        etTelephoneSignUp.setText("");
        etCardSignUp.setText("");
    }

    private void showSignUpErrorMessage() {
        Toast.makeText(MainActivity.this, "Error adding user", Toast.LENGTH_SHORT).show();
    }

    private void showIncompleteFieldsMessage() {
        Toast.makeText(MainActivity.this, "Please, complete all fields", Toast.LENGTH_SHORT).show();
    }
}
