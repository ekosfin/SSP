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

public class TransferActivity extends AppCompatActivity {

    private static final String TAG = "TransferActivity";

    private Button mAddToDB;
    private EditText mSend, mReceive, mMoney, mMessage;
    private ImageButton back;

    //firebase stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef, myAccountRef;
    private DataSnapshot tempData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        mAddToDB = findViewById(R.id.cardButton);
        mSend = findViewById(R.id.add_send);
        mReceive = findViewById(R.id.add_recive);
        mMoney = findViewById(R.id.add_sum);
        mMessage = findViewById(R.id.add_message);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        myAccountRef = mFirebaseDatabase.getReference("accounts");

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
        mAddToDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Attempting to add object to database.");
                String newSend = mSend.getText().toString().trim();
                String newReceive = mReceive.getText().toString().trim();
                String newMoney = mMoney.getText().toString().trim();
                String newMessage = mMessage.getText().toString().trim();
                if((!newSend.isEmpty()) && (!newReceive.isEmpty()) && (!newMoney.isEmpty()) && (!newMessage.isEmpty())){
                    checkAccont(newSend,newReceive,newMoney,newMessage);
                } else {
                    toastMessage("Täytä kaikki kentät");
                }
            }
        });

        back = findViewById((R.id.backButton));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iToBack = new Intent(TransferActivity.this,HomeActivity.class);
                startActivity(iToBack);
            }
        });

    }

    private void transfer(DataSnapshot ds){
        this.tempData = ds;
    }

    private void checkAccont(String newSend, String newReceive, String newMoney, String newMessage){
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        String userID = user.getUid();
        DataSnapshot tempData2 = tempData;
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
                            for (DataSnapshot ds2 : tempData2.getChildren()){
                                if (newReceive.equals(Objects.requireNonNull(ds2.getValue(AccountInformation.class)).getNumber())){
                                    AccountInformation aInfo2 = new AccountInformation();
                                    String balance2 = Objects.requireNonNull(ds2.getValue(AccountInformation.class)).getMoney();
                                    aInfo2.setName(Objects.requireNonNull(ds2.getValue(AccountInformation.class)).getName()); // set name
                                    aInfo2.setOwner(Objects.requireNonNull(ds2.getValue(AccountInformation.class)).getOwner()); //set owner aka userid
                                    aInfo2.setType(Objects.requireNonNull(ds2.getValue(AccountInformation.class)).getType()); //set type
                                    aInfo2.setNumber(Objects.requireNonNull(ds2.getValue(AccountInformation.class)).getNumber()); //set number
                                    aInfo2.setMoney(Integer.toString(Integer.parseInt(balance2)+Integer.parseInt(newMoney))); //set new balance
                                    myAccountRef.child(Objects.requireNonNull(ds2.getKey())).setValue(aInfo2);
                                }
                            }
                            myAccountRef.child(Objects.requireNonNull(ds.getKey())).setValue(aInfo);
                            TransactionInformation transaction = new TransactionInformation(newSend,newReceive,newMoney,newMessage, Calendar.getInstance().getTime());
                            String key = mFirebaseDatabase.getReference("transactions").push().getKey();
                            assert key != null;
                            myRef.child("transactions").child(key).setValue(transaction);
                            toastMessage("Siirto onnistui");
                            Intent iToBack = new Intent(TransferActivity.this,HomeActivity.class);
                            startActivity(iToBack);
                        } else {
                            toastMessage("Liian vähän rahaa");
                        }
                    } else {
                        toastMessage("Tili lukittu");
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

