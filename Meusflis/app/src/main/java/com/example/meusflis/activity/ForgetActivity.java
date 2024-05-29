package com.example.meusflis.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.meusflis.R;
import com.example.meusflis.database.DatabaseHelper;

/**
 * La actividad ForgetActivity maneja la recuperación de la contraseña de usuario a través del email.
 */
public class ForgetActivity extends AppCompatActivity {

    EditText etForgetEmail;
    Button btnSend;
    TextView tvLoginRedirect;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        initializeViews();
        setListeners();
    }



    /**
     * Inicializa las vistas vinculándolas a sus respectivos elementos de la interfaz de usuario en el diseño.
     */
    private void initializeViews() {
        etForgetEmail = findViewById(R.id.etForgetEmail);
        btnSend = findViewById(R.id.btnSend);
        tvLoginRedirect = findViewById(R.id.tvLoginRedirect);
    }



    /**
     * Establece los listeners para el botón de envío y la vista de redirección al inicio de sesión.
     */
    private void setListeners() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etForgetEmail.getText().toString().trim();
                if (!email.isEmpty()) {
                    databaseHelper = new DatabaseHelper(ForgetActivity.this);
                    String password = databaseHelper.getPasswordByEmail(email);
                    if (password != null) {
                        showNotification(password);
                    }
                    else {
                        Toast.makeText(ForgetActivity.this, getString(R.string.tvEmailForgetNotExist), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(ForgetActivity.this, getString(R.string.tvEmailForgetIsEmpty), Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvLoginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgetActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }



    /**
     * Muestra una notificación con la contraseña del usuario.
     * @param password La contraseña del usuario.
     */
    private void showNotification(String password) {
        String channelId = "default_channel_id";
        String channelDescription = "Default Channel";
        int notificationId = 1;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelDescription, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.baseline_info_24)
                .setContentTitle(getString(R.string.notifyTitleForgetPass))
                .setContentText(getString(R.string.notifyMessageForgetPass) + password)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(notificationId, builder.build());
    }
}
