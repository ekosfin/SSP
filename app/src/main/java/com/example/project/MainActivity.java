package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@SuppressWarnings("static-access")
public class MainActivity extends AppCompatActivity {   //Create account

    private static final String TAG = "MainActivity";

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference myRef;
    private FirebaseDatabase mFirebaseDatabase;

    EditText textInputEmail, textInputPassword,textInputFirstName,textInputLastName,textInputAddress;
    Button signupButton, loginButton;
    Validator validator = Validator.buildValidator(true,true,true,12,32);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAuth = FirebaseAuth.getInstance(); //connection to firebase
        textInputEmail = findViewById(R.id.emailInput);
        textInputPassword = findViewById(R.id.passwordInput);
        signupButton = findViewById(R.id.signupButton);
        loginButton = findViewById(R.id.loginButton);
        textInputFirstName = findViewById(R.id.firstInput);
        textInputLastName = findViewById(R.id.lastInput);
        textInputAddress = findViewById(R.id.addressInput);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = textInputEmail.getText().toString();
                String password = textInputPassword.getText().toString();
                final String first = textInputFirstName.getText().toString();
                final String last = textInputLastName.getText().toString();
                final String address = textInputAddress.getText().toString();
                if (email.isEmpty()) {
                    textInputEmail.setError("Syötä sähköposti");
                    textInputEmail.requestFocus();
                }
                else if (password.isEmpty()) {
                    textInputPassword.setError("Syötä salasana");
                    textInputPassword.requestFocus();
                }
                else if(first.isEmpty()){
                    textInputFirstName.setError("Syötä etunimi");
                    textInputFirstName.requestFocus();
                }
                else if(last.isEmpty()){
                    textInputLastName.setError("Syötä sukunimi");
                    textInputLastName.requestFocus();
                }
                else if(address.isEmpty()){
                    textInputAddress.setError("Syötä osoite");
                    textInputAddress.requestFocus();
                }
                else {
                    if (validator.validatePassword(password)){
                        mFirebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    toastMessage("Käyttäjän teko epäonnistui");
                                }
                                else {
                                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                                    myRef = mFirebaseDatabase.getReference("users");
                                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                                    assert user != null;
                                    String userID = user.getUid();
                                    UserInformation uInfo = new UserInformation(first,last,address,userID);
                                    myRef.child(userID).setValue(uInfo);
                                    startActivity(new Intent(MainActivity.this,PinActivity.class));
                                }
                            }
                        });
                    } else {
                        Log.d(TAG,"Salasana on: " + password);
                        toastMessage("Salasana ei täytä vaatimuskia");
                    }
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iToLogIn = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(iToLogIn);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if (currentUser != null){
            Intent i = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(i);
        }
    }


    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


}
