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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserActivity extends AppCompatActivity { //User information

    private static final String TAG = "UserActivity";

    private Button logoutButton;
    private Button changePasswordButton;
    private Button changeUserInformation;
    private Button deleteButton;
    private ImageButton back;
    private String userID;

    private TextView namefirst,namelast;
    private TextView showAddress;


    //firebase stuff
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        namefirst = findViewById(R.id.namefirst);
        namelast = findViewById(R.id.namelast);
        showAddress = findViewById(R.id.showAddress);
        deleteButton = findViewById(R.id.deleteButton);

        //firebase stuff
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("users");
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                toastMessage("Olet uloskirjautunut");
                Intent iToMain = new Intent(UserActivity.this,MainActivity.class);
                startActivity(iToMain);
            }
        });

        back = findViewById((R.id.backButton));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iToBack = new Intent(UserActivity.this,HomeActivity.class);
                startActivity(iToBack);
            }
        });

        changePasswordButton = findViewById((R.id.passwordButton));
        changePasswordButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iToPassword = new Intent(UserActivity.this,ChangePasswordActivity.class);
                startActivity(iToPassword);
            }
        }));

        changeUserInformation = findViewById((R.id.infoChange));
        changeUserInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iToInfoChange = new Intent(UserActivity.this,ChangeUserActivity.class);
                startActivity(iToInfoChange);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child(userID).removeValue();
                toastMessage("Käyttäjä on poistetttu");
                FirebaseAuth.getInstance().signOut();
                Intent iToMain = new Intent(UserActivity.this,MainActivity.class);
                startActivity(iToMain);
            }
        });

    }

    private void showData(DataSnapshot ds){
        for (DataSnapshot dsUser : ds.getChildren()){
            if (userID.equals(dsUser.getValue(UserInformation.class).getUserID())){
                namefirst.setText(dsUser.getValue(UserInformation.class).getFirstName());
                namelast.setText((dsUser.getValue(UserInformation.class).getLastName()));
                showAddress.setText((dsUser.getValue(UserInformation.class).getAddress()));
                break;
            }
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
