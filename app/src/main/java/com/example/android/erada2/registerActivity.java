package com.example.android.erada2;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class registerActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText regEmailText;
    private EditText regPassText;
    private EditText regConfirmPassText;
    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.regi_progressBar);
        regEmailText = (EditText) findViewById(R.id.register);
        regPassText = (EditText) findViewById(R.id.regi_pass);
        regConfirmPassText = (EditText) findViewById(R.id.regi_confirm);
        register = (Button) findViewById(R.id.reg_button);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = regEmailText.getText().toString();
                String pass = regPassText.getText().toString();
                String ConfirmPass = regConfirmPassText.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(ConfirmPass)) {

                    if (pass.equals(ConfirmPass)) {

                        progressBar.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {

                                    Intent setupIntent = new Intent(registerActivity.this, FormActivity.class);
                                    startActivity(setupIntent);
                                } else {

                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(registerActivity.this, "Error :" + errorMessage, Toast.LENGTH_LONG).show();
                                    finish();
                                }

                                progressBar.setVisibility(View.INVISIBLE);
                            }

                        });

                    } else {

                        Toast.makeText(registerActivity.this, "Confirm Password doesn't match the password .. Type again", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            Intent intent = new Intent(registerActivity.this, FormActivity.class);
            startActivity(intent);

        }

    }
}
