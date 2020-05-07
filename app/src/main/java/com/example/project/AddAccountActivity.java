package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddAccountActivity extends AppCompatActivity {
    //source: https://github.com/mitchtabian/Firebase-Database-REAL-TIME-/blob/master/FirebaseAddToDatabase/app/src/main/java/com/tabian/firebaseaddtodatabase/AddToDatabase.java

    private static final String TAG = "AddToDatabase";

    private Button mAddToDB;
    private ImageButton back;
    private EditText mName, mType, mNumber;

    //firebase stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        mAddToDB = findViewById(R.id.cardButton);
        mName = findViewById(R.id.add_account_name);
        mType = findViewById(R.id.add_account_type);
        mNumber = findViewById(R.id.add_account_number);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("accounts");


        mAddToDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Attempting to add object to database.");
                String newType = mType.getText().toString().trim();
                String newName = mName.getText().toString().trim();
                String newNumber = mNumber.getText().toString().trim();
                if((!newType.isEmpty()) && (!newName.isEmpty()) && (!newNumber.isEmpty())){
                    FirebaseUser user = mAuth.getCurrentUser();
                    assert user != null;
                    String userID = user.getUid();
                    String newMoney = String.valueOf(0);
                    AccountInformation account = new AccountInformation(newType,newMoney,userID,newName,newNumber);
                    String key = mFirebaseDatabase.getReference("accounts").push().getKey();
                    assert key != null;
                    myRef.child(key).setValue(account);
                    Intent iToBack = new Intent(AddAccountActivity.this,HomeActivity.class);
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
                Intent iToBack = new Intent(AddAccountActivity.this,HomeActivity.class);
                startActivity(iToBack);
            }
        });

    }


    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
