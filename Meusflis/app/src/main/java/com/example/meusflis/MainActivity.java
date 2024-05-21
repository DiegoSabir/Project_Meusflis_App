package com.example.meusflis;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import androidx.core.app.NotificationCompat;

import java.util.HashMap;

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
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new Database(this);

        // Obtiene las preferencias compartidas por defecto de la aplicación
        loginSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Configura la actividad para que el teclado virtual no aparezca automáticamente al iniciar
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initViews();
        checkSavedEmail();
        setupListeners();
    }

    /**
     * Inicializa las vistas vinculando los elementos de diseño a las variables de Java.
     */
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
        tvSignUp = findViewById(R.id.tvSignUp2);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnBackSignIn = findViewById(R.id.btnBackSignIn);
        btnSendForgotPassword = findViewById(R.id.btnSendForgotPassword);
        btnBackFromForgotPassword = findViewById(R.id.btnBackFromForgotPassword);

        chkRememberUsername = findViewById(R.id.chkRememberUsername);
    }



    /**
     * Comprueba si un correo electrónico está guardado en SharedPreferences y lo configura en
     * el correo electrónico EditText.
     */
    private void checkSavedEmail() {
        String savedEmail = loginSharedPreferences.getString("email", "");
        if (!savedEmail.isEmpty()) {
            etEmail.setText(savedEmail);
        }
        else {
            etEmail.setHint("Email");
        }
    }



    /**
     * Configura oyentes para varios elementos de la interfaz de usuario.
     */
    private void setupListeners() {
        setupVisibilityListeners();
        setupSignInButtonListener();
        setupSignUpButtonListener();
        setupForgotPasswordButtonListener();
    }



    /**
     * Configura oyentes para manejar cambios de visibilidad entre diferentes diseños.
     */
    private void setupVisibilityListeners() {
        tvForgotPassword.setOnClickListener(view -> showForgotPasswordLayout());
        btnBackFromForgotPassword.setOnClickListener(view -> showSignInLayout());
        tvSignUp.setOnClickListener(view -> showSignUpLayout());
        btnBackSignIn.setOnClickListener(view -> showSignInLayout());
    }



    /**
     * Muestra el diseño de contraseña olvidada y oculta el diseño de inicio de sesión.
     */
    private void showForgotPasswordLayout() {
        llForgotPass.setVisibility(View.VISIBLE);
        llSignIn.setVisibility(View.GONE);
    }



    /**
     * Muestra el diseño de inicio de sesión y oculta los diseños de registro y contraseña olvidada.
     */
    private void showSignInLayout() {
        llForgotPass.setVisibility(View.GONE);
        llSignIn.setVisibility(View.VISIBLE);
        llSignUp.setVisibility(View.GONE);
    }



    /**
     * Muestra el diseño de registro y oculta el diseño de inicio de sesión.
     */
    private void showSignUpLayout() {
        llSignUp.setVisibility(View.VISIBLE);
        llSignIn.setVisibility(View.GONE);
    }



    /**
     * Configura el oyente para el botón de inicio de sesión.
     */
    private void setupSignInButtonListener() {
        btnSignIn.setOnClickListener(view -> handleSignIn());
    }



    /**
     * Maneja el proceso de inicio de sesión, incluida la validación y la navegación.
     */
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



    /**
     * Guarda el correo electrónico en SharedPreferences si la casilla de verificación recordar
     * nombre de usuario está marcada.
     * @param email el correo electrónico para guardar
     */
    private void saveEmailToPreferences(String email) {
        SharedPreferences.Editor editor = loginSharedPreferences.edit();
        editor.putString("email", email);
        editor.apply();
    }



    /**
     * Navega a la actividad del catálogo y pasa el correo electrónico como extra.
     * @param email el correo electrónico para pasar a la siguiente actividad
     */
    private void navigateToCatalogue(String email) {
        Intent intent = new Intent(MainActivity.this, Catalogue.class);
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
    }



    /**
     * Maneja un intento fallido de inicio de sesión aumentando el contador y
     * mostrando los mensajes apropiados.
     */
    private void handleFailedSignInAttempt() {
        failedAttempts++;
        if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
            showMaxAttemptsDialog();
        }
        else {
            Toast.makeText(MainActivity.this, R.string.toast_incorrect_credentials, Toast.LENGTH_SHORT).show();
        }
    }



    /**
     * Muestra un cuadro de diálogo cuando se alcanza el número máximo de intentos fallidos
     * de inicio de sesión.
     */
    private void showMaxAttemptsDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_max_attempts_title)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(R.string.dialog_max_attempts_message)
                .setPositiveButton(R.string.dialog_positive_button, (dialog, which) -> {
                    failedAttempts = 0;
                    finish();
                })
                .setCancelable(false)
                .show();
    }



    /**
     * Configura el oyente para el botón de registro.
     */
    private void setupSignUpButtonListener() {
        btnSignUp.setOnClickListener(view -> handleSignUp());
    }



    /**
     * Maneja el proceso de registro, incluida la validación y la creación de usuarios.
     */
    private void handleSignUp() {
        String email = etEmailSignUp.getText().toString();
        String password = etPasswordSignUp.getText().toString();
        String name = etNameSignUp.getText().toString();
        String telephone = etTelephoneSignUp.getText().toString();
        String card = etCardSignUp.getText().toString();

        if (validateSignUpFields(email, password, name, telephone, card)) {
            if (database.addUser(email, password, name, telephone, card)) {
                Toast.makeText(MainActivity.this, R.string.toast_user_added, Toast.LENGTH_SHORT).show();
                clearSignUpFields();
                showSignInLayout();
            }
            else {
                Toast.makeText(MainActivity.this, R.string.toast_error_adding_user, Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(MainActivity.this, R.string.toast_complete_all_fields, Toast.LENGTH_SHORT).show();
        }
    }



    /**
     * Valida los campos de registro para garantizar que ninguno esté vacío.
     * @param email el correo electrónico
     * @param password la contraseña
     * @param name el nombre
     * @param telephone el teléfono
     * @param card el numero de tarjeta del banco
     * @return verdadero si todos los campos están llenos, falso en caso contrario
     */
    private boolean validateSignUpFields(String email, String password, String name, String telephone, String card) {
        return !email.isEmpty() && !password.isEmpty() && !name.isEmpty() && !telephone.isEmpty() && !card.isEmpty();
    }



    /**
     * Borra los campos de registro después de un registro exitoso.
     */
    private void clearSignUpFields() {
        etEmailSignUp.setText("");
        etPasswordSignUp.setText("");
        etNameSignUp.setText("");
        etTelephoneSignUp.setText("");
        etCardSignUp.setText("");
    }



    /**
     * Configura el oyente para el botón de contraseña olvidada.
     */
    private void setupForgotPasswordButtonListener() {
        btnSendForgotPassword.setOnClickListener(view -> handleForgotPassword());
    }



    /**
     * Maneja el proceso de olvido de contraseña, incluida la recuperación y notificación al usuario de su contraseña.
     */
    private void handleForgotPassword() {
        String email = etEmailForgot.getText().toString();

        if (!email.isEmpty()) {
            HashMap<String, String> userDetails = database.getUserDetails(email);
            if (userDetails != null && !userDetails.isEmpty()) {
                String password = userDetails.get("password");
                sendPasswordNotification(password);
            }
            else {
                Toast.makeText(MainActivity.this, R.string.toast_email_not_found, Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(MainActivity.this, R.string.toast_enter_email, Toast.LENGTH_SHORT).show();
        }
    }



    /**
     * Envía una notificación que contiene la contraseña del usuario.
     * @param password la contraseña a incluir en la notificación
     */
    private void sendPasswordNotification(String password) {
        String channelId = "password_recovery_channel";
        String channelName = getString(R.string.notification_title);
        int notificationId = 1;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_content, password))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify(notificationId, builder.build());
    }
}
