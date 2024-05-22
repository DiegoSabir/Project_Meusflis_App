package com.example.meusflis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meusflis.R;
import com.example.meusflis.model.User;

public class SignUpActivity extends AppCompatActivity {
    EditText etSignUpEmail, etSignUpPassword, etSignUpName, etSignUpTelephone, etSignUpCard;
    TextView tvLoginRedirect;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        etSignUpEmail = findViewById(R.id.etSignUpEmail);
        etSignUpPassword = findViewById(R.id.etSignUpPassword);
        etSignUpName = findViewById(R.id.etSignUpName);
        etSignUpTelephone = findViewById(R.id.etSignUpTelephone);
        etSignUpCard = findViewById(R.id.etSignUpCard);

        btnSignUp = findViewById(R.id.btnSignUp);

        tvLoginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
