package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeUserActivity extends AppCompatActivity {

    private static final String TAG = "ChangeUserActivity";

    private Button change;
    private ImageButton back;
    private TextView newFirst, newLast, newAddress;

    //firebase stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user);

        newFirst = findViewById(R.id.editFirst);
        newLast = findViewById(R.id.editLast);
        newAddress = findViewById(R.id.editAddress);
        change = findViewById(R.id.changeButton);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("users");

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Attempting to add object to database.");
                String first = newFirst.getText().toString().trim();
                String last = newLast.getText().toString().trim();
                String address = newAddress.getText().toString().trim();
                if((!first.isEmpty()) && (!last.isEmpty()) && (!address.isEmpty())){
                    FirebaseUser user = mAuth.getCurrentUser();
                    assert user != null;
                    String userID = user.getUid();
                    UserInformation uInfo = new UserInformation(first,last,address,userID);
                    myRef.child(userID).setValue(uInfo);
                    Intent iToBack = new Intent(ChangeUserActivity.this,UserActivity.class);
                    startActivity(iToBack);
                } else {
                    toastMessage("Täytä kaikki kentät");
                }
            }
        });

        back = findViewById((R.id.backButton));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iToBack = new Intent(ChangeUserActivity.this,UserActivity.class);
                startActivity(iToBack);
            }
        });
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
