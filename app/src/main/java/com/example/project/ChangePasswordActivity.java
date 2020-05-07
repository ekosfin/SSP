package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private static final String TAG = "ChangePasswordActivity";

    private ImageButton back;
    private Button change;
    private TextView newpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        newpass = findViewById(R.id.newInput);

        back = findViewById((R.id.backButton));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iToBack = new Intent(ChangePasswordActivity.this,UserActivity.class);
                startActivity(iToBack);
            }
        });

        change = findViewById(R.id.changeButton);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPass = newpass.getText().toString();
                user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Password updated");
                            toastMessage("Salasanan vaihto onnistui");
                            Intent iToBack = new Intent(ChangePasswordActivity.this,UserActivity.class);
                            startActivity(iToBack);
                        } else {
                            Log.d(TAG, "Error password not updated");
                            toastMessage("Salasanan vaihto epäonnistui");
                            Intent iToBack = new Intent(ChangePasswordActivity.this,UserActivity.class);
                            startActivity(iToBack);
                        }
                    }
                });
            }

        });
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
