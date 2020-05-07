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

import java.util.Calendar;
import java.util.Objects;

public class WithdrawActivity extends AppCompatActivity {

    private static final String TAG = "WithdrawActivity";

    private Button withdrawButton;
    private ImageButton back;
    private EditText mFrom, mMoney;

    //firebase stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef, myAccountRef;
    private DataSnapshot tempData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        withdrawButton = findViewById(R.id.cardButton);
        back = findViewById(R.id.backButton);
        mFrom = findViewById(R.id.add_send);
        mMoney = findViewById(R.id.add_sum);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        myAccountRef = mFirebaseDatabase.getReference("accounts");
        //for debugging
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Object value = dataSnapshot.getValue();
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        // Read from the database
        myAccountRef.addValueEventListener(new ValueEventListener() {
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iToHome = new Intent(WithdrawActivity.this,HomeActivity.class);
                startActivity(iToHome);
            }
        });

        withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Attempting to add object to database.");
                String newSend = mFrom.getText().toString().trim();
                String newMoney = mMoney.getText().toString().trim();
                String newReceive = "";
                String newMessage = "Nosto";
                if((!newSend.isEmpty()) && (!newMoney.isEmpty())){
                    checkAccount(newSend, newReceive, newMoney, newMessage);
                } else {
                    toastMessage("Täytä kaikki kentät");
                }
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void transfer(DataSnapshot ds){
        this.tempData = ds;
    }

    private void checkAccount(String newSend, String newReceive, String newMoney, String newMessage){
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        String userID = user.getUid();
        for (DataSnapshot ds : tempData.getChildren()){
            if (newSend.equals(Objects.requireNonNull(ds.getValue(AccountInformation.class)).getNumber())){
                if(userID.equals(Objects.requireNonNull(ds.getValue(AccountInformation.class)).getOwner())){
                    String balance = Objects.requireNonNull(ds.getValue(AccountInformation.class)).getMoney();
                    if (!Objects.requireNonNull(ds.getValue(AccountInformation.class)).getType().equals("Disabled")){
                        if (Integer.parseInt(balance) >= Integer.parseInt(newMoney)){
                            AccountInformation aInfo = new AccountInformation();
                            aInfo.setName(Objects.requireNonNull(ds.getValue(AccountInformation.class)).getName()); // set name
                            aInfo.setOwner(Objects.requireNonNull(ds.getValue(AccountInformation.class)).getOwner()); //set owner aka userid
                            aInfo.setType(Objects.requireNonNull(ds.getValue(AccountInformation.class)).getType()); //set type
                            aInfo.setNumber(Objects.requireNonNull(ds.getValue(AccountInformation.class)).getNumber()); //set number
                            aInfo.setMoney(Integer.toString(Integer.parseInt(balance)-Integer.parseInt(newMoney))); //set new balance
                            myAccountRef.child(Objects.requireNonNull(ds.getKey())).setValue(aInfo);
                            TransactionInformation transaction = new TransactionInformation(newSend,newReceive,newMoney,newMessage, Calendar.getInstance().getTime());
                            String key = mFirebaseDatabase.getReference("transactions").push().getKey();
                            assert key != null;
                            myRef.child("transactions").child(key).setValue(transaction);
                            toastMessage("Nosto onnistui");
                            Intent iToBack = new Intent(WithdrawActivity.this,HomeActivity.class);
                            startActivity(iToBack);
                        } else {
                            toastMessage("Liian vähän rahaa");
                        }
                    } else {
                        toastMessage("Tili on lukittu");
                    }
                } else {
                    toastMessage("Et omista tiliä");
                }
            }
        }
        toastMessage("Tiliä ei löytynyt");
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
