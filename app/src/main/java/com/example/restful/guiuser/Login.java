package com.example.restful.guiuser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.restful.R;
import com.example.restful.guiadmin.ManageListProduct;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        Button loginButton = findViewById(R.id.buttonLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (authenticate(editTextEmail.getText().toString(), editTextPassword.getText().toString())) {
                    Intent intent = new Intent(Login.this, ManageListProduct.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Login.this, "Incorrect account email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean authenticate(String email, String password) {
        String defaultUsername = "admin";
        String defaultPassword = "123";
        return email.equals(defaultUsername) && password.equals(defaultPassword);
    }
}
