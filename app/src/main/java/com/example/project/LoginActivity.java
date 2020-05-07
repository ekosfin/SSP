package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {  // Log in

    private FirebaseAuth.AuthStateListener mFirebaceListner;
    private FirebaseAuth mFirebaceAuth;

    EditText textInputEmail, textInputPassword;

    Button signupButton;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaceAuth = FirebaseAuth.getInstance(); //connection to firebase
        textInputEmail = findViewById(R.id.emailInput);
        textInputPassword = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);

        mFirebaceListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaceAuth.getCurrentUser();
                if ( mFirebaseUser != null){
                    Intent i = new Intent(LoginActivity.this, PinActivity.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(LoginActivity.this,"Kirjaudu sisään",Toast.LENGTH_SHORT).show();
                }
            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = textInputEmail.getText().toString();
                String password = textInputPassword.getText().toString();
                if (email.isEmpty()) {
                    textInputEmail.setError("Syötä sähköposti");
                    textInputEmail.requestFocus();
                }
                else if (password.isEmpty()) {
                    textInputPassword.setError("Syötä salasana");
                    textInputPassword.requestFocus();
                }
                else {
                    mFirebaceAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this,"Sisään kirjautumisessa virhe",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent iToPin = new Intent(LoginActivity.this,PinActivity.class);
                                startActivity(iToPin);
                            }
                        }
                    });
                }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iSignUp = new Intent(LoginActivity.this,PinActivity.class);
                startActivity(iSignUp);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaceAuth.addAuthStateListener(mFirebaceListner);
    }
}
