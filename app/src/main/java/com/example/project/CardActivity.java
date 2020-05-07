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

import java.util.Objects;

public class CardActivity extends AppCompatActivity {

    private static final String TAG = "CardActivity";

    private Button mAddToDB;
    private ImageButton back;
    private EditText mName, mType, mAccount, mUsage;

    //firebase stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myAcoountsRef, myCardsRef;
    private DataSnapshot tempData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        mAddToDB = findViewById(R.id.addCardButton);
        mName = findViewById(R.id.add_name);
        mType = findViewById(R.id.add_type);
        mAccount = findViewById(R.id.add_account);
        mUsage = findViewById(R.id.add_usage);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myCardsRef = mFirebaseDatabase.getReference("cards");
        myAcoountsRef = mFirebaseDatabase.getReference("accounts");

        // Read from the database
        myAcoountsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                transfer(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        mAddToDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Attempting to add object to database.");
                String newType = mType.getText().toString().trim();
                String newName = mName.getText().toString().trim();
                String newAccount = mAccount.getText().toString().trim();
                String newUsage = mUsage.getText().toString().trim();
                if((!newType.isEmpty()) && (!newName.isEmpty()) && (!newAccount.isEmpty())  && (!newUsage.isEmpty())){
                    FirebaseUser user = mAuth.getCurrentUser();
                    assert user != null;
                    String userID = user.getUid();
                    for (DataSnapshot ds : tempData.getChildren()){
                        if (newAccount.equals(Objects.requireNonNull(ds.getValue(AccountInformation.class)).getNumber())){
                            if (userID.equals(Objects.requireNonNull(ds.getValue(AccountInformation.class)).getOwner())){
                                CardInformation card = new CardInformation(newAccount,newType,newUsage,newName,ds.getKey());
                                String key = mFirebaseDatabase.getReference("cards").push().getKey();
                                assert key != null;
                                myCardsRef.child(key).setValue(card);
                                Intent iToBack = new Intent(CardActivity.this,HomeActivity.class);
                                startActivity(iToBack);
                            } else {
                                toastMessage("et omista tiliä");
                            }
                        }
                    }
                } else {
                    toastMessage("Täytä kaikki kentät");
                }
            }
        });
        back = findViewById((R.id.backButton));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iToBack = new Intent(CardActivity.this,HomeActivity.class);
                startActivity(iToBack);
            }
        });

    }


    private void transfer(DataSnapshot ds){
        this.tempData = ds;
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
