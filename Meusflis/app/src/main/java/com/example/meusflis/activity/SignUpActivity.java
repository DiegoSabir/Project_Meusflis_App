package com.example.meusflis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meusflis.R;
import com.example.meusflis.database.DatabaseHelper;

public class SignUpActivity extends AppCompatActivity {
    EditText etSignUpEmail, etSignUpPassword, etSignUpName, etSignUpTelephone, etSignUpCard;
    Button btnSignUp;
    TextView tvLoginRedirect;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        databaseHelper = new DatabaseHelper(this);

        initializeViews();
        setListeners();
    }



    /**
     * Inicializa las vistas vinculándolas a sus respectivos elementos de la interfaz de usuario en el diseño.
     */
    private void initializeViews() {
        etSignUpEmail = findViewById(R.id.etSignUpEmail);
        etSignUpPassword = findViewById(R.id.etSignUpPassword);
        etSignUpName = findViewById(R.id.etSignUpName);
        etSignUpTelephone = findViewById(R.id.etSignUpTelephone);
        etSignUpCard = findViewById(R.id.etSignUpCard);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvLoginRedirect = findViewById(R.id.tvLoginRedirect);
    }



    /**
     * Establece los listeners para el botón de registro y la vista de redirección al inicio de sesión.
     */
    private void setListeners() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInput()) {
                    String email = etSignUpEmail.getText().toString();
                    String password = etSignUpPassword.getText().toString();
                    String name = etSignUpName.getText().toString();
                    String telephone = etSignUpTelephone.getText().toString();
                    String card = etSignUpCard.getText().toString();

                    boolean isInserted = databaseHelper.insertUser(email, password, name, telephone, card);

                    if (isInserted) {
                        Toast.makeText(SignUpActivity.this, getString(R.string.setListenerUserExists), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(SignUpActivity.this, getString(R.string.setListenerUserExists), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tvLoginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }



    /**
     * Valida la entrada del usuario para el correo electrónico, la contraseña, el nombre, el teléfono y el número de tarjeta.
     * Establece mensajes de error y muestra mensajes Toast para entradas inválidas.
     * @return true si todas las entradas son válidas, false de lo contrario.
     */
    private boolean validateInput() {
        String email = etSignUpEmail.getText().toString();
        String password = etSignUpPassword.getText().toString();
        String name = etSignUpName.getText().toString();
        String telephone = etSignUpTelephone.getText().toString();
        String card = etSignUpCard.getText().toString();

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etSignUpEmail.setError(getString(R.string.validationEmail));
            Toast.makeText(SignUpActivity.this, getString(R.string.validationEmailMessage), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            etSignUpPassword.setError(getString(R.string.validationPassword));
            Toast.makeText(SignUpActivity.this, getString(R.string.validationPasswordMessage), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(name)) {
            etSignUpName.setError(getString(R.string.validationName));
            Toast.makeText(SignUpActivity.this, getString(R.string.validationNameMessage), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isValidTelephone(telephone)) {
            etSignUpTelephone.setError(getString(R.string.validationTelephone));
            Toast.makeText(SignUpActivity.this, getString(R.string.validationTelephoneMessage), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isValidCardNumber(card)) {
            etSignUpCard.setError(getString(R.string.validationCard));
            Toast.makeText(SignUpActivity.this, getString(R.string.validationCardMessage), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



    /**
     * Valida el formato del número de teléfono.
     * @param telephone El número de teléfono a validar.
     * @return true si el número de teléfono es válido, false de lo contrario.
     */
    private boolean isValidTelephone(String telephone) {
        // Elimina cualquier espacio en blanco
        telephone = telephone.replaceAll("\\s", "");

        // Verifica si el número de teléfono tiene el formato correcto
        return telephone.matches("\\+\\d{1,3}\\d{9}");
    }



    /**
     * Valida el número de tarjeta utilizando el algoritmo de Luhn.
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
