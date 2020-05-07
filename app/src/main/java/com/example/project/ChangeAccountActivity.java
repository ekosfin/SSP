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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ChangeAccountActivity extends AppCompatActivity {

    private static final String TAG = "ChangeAccountActivity";

    private TextView mName,mType;
    private String accountNumber,accountID;

    private DatabaseReference myRef;
    private DataSnapshot tempData;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_account);

        mName = findViewById(R.id.textName);
        mType = findViewById(R.id.textType);

        accountNumber = getIntent().getStringExtra("AccountID");
        accountID = getIntent().getStringExtra("accountID");

        //firebase stuff
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("accounts");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                saveData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        ImageButton back = findViewById((R.id.backButton));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iToBack = new Intent(ChangeAccountActivity.this,AccountActivity.class);
                startActivity(iToBack);
            }
        });

        Button change = findViewById(R.id.changeButton);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = mName.getText().toString();
                String newType = mType.getText().toString();
                if((!newType.isEmpty()) && (!newName.isEmpty())){
                    for (DataSnapshot ds : tempData.getChildren()){
                        if (accountID.equals(Objects.requireNonNull(ds.getKey()))){
                            AccountInformation aInfo = new AccountInformation();
                            aInfo.setNumber(Objects.requireNonNull(ds.getValue(AccountInformation.class)).getNumber());
                            aInfo.setOwner(Objects.requireNonNull(ds.getValue(AccountInformation.class)).getOwner());
                            aInfo.setMoney(Objects.requireNonNull(ds.getValue(AccountInformation.class)).getMoney());
                            aInfo.setName(newName);
                            aInfo.setType(newType);
                            myRef.child(Objects.requireNonNull(ds.getKey())).setValue(aInfo);
                            Log.d(TAG,"tiedot muutettu ##################################"+accountNumber);
                            toastMessage("Tiedot muutettu");
                            Intent i = new Intent(ChangeAccountActivity.this,HomeActivity.class);
                            startActivity(i);
                        }
                    }
                } else {
                    toastMessage("Täytä vaaditut kentät");
                }
            }
        });

    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    private void saveData(DataSnapshot ds){
        this.tempData = ds;
    }
}
