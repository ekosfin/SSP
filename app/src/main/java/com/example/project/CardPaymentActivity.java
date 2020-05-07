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

public class CardPaymentActivity extends AppCompatActivity {

    private static final String TAG = "CardPaymentActivity";

    private Button mPayment;
    private ImageButton back;
    private EditText mCard, mMoney;

    //firebase stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myCardRef, myAccountRef, myRef;
    private DataSnapshot tempDataAccount, tempDataCards;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payment);

        mPayment = findViewById(R.id.addPayment);
        mCard = findViewById(R.id.add_name);
        mMoney = findViewById(R.id.add_sum);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myCardRef = mFirebaseDatabase.getReference("cards");
        myRef = mFirebaseDatabase.getReference();
        myAccountRef = mFirebaseDatabase.getReference("accounts");

        // Read from the database
        myAccountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                transferAccount(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        // Read from the database
        myCardRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                transferCards(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        mPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Attempting to add object to database.");
                String newCard = mCard.getText().toString().trim();
                String newMoney = mMoney.getText().toString().trim();
                if((!newCard.isEmpty())  && (!newMoney.isEmpty())){
                    check(newCard,newMoney);
                } else {
                    toastMessage("Täytä kaikki kentät");
                }
            }
        });

        back = findViewById((R.id.backButton));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iToBack = new Intent(CardPaymentActivity.this,HomeActivity.class);
                startActivity(iToBack);
            }
        });
    }

    private void transferAccount(DataSnapshot ds){
        this.tempDataAccount = ds;
    }

    private void transferCards(DataSnapshot ds){
        this.tempDataCards = ds;
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    private void check(String newCard,String newMoney){
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        String userID = user.getUid();
        for (DataSnapshot dsCard : tempDataCards.getChildren()){
            if (newCard.equals(Objects.requireNonNull(dsCard.getValue(CardInformation.class)).getNimi())){
                String limit = Objects.requireNonNull(dsCard.getValue(CardInformation.class)).getUsageLimit();
                if(Integer.parseInt(limit) >= Integer.parseInt(newMoney)){
                    for (DataSnapshot dsAccount : tempDataAccount.getChildren()){
                        if (Objects.requireNonNull(dsCard.getValue(CardInformation.class)).getAccountID().equals(dsAccount.getKey())){
                            if (!Objects.requireNonNull(dsCard.getValue(AccountInformation.class)).getType().equals("Disabled")){
                                AccountInformation aInfo = new AccountInformation();
                                String balance = Objects.requireNonNull(dsAccount.getValue(AccountInformation.class)).getMoney();
                                aInfo.setName(Objects.requireNonNull(dsAccount.getValue(AccountInformation.class)).getName()); // set name
                                aInfo.setOwner(Objects.requireNonNull(dsAccount.getValue(AccountInformation.class)).getOwner()); //set owner aka userid
                                aInfo.setType(Objects.requireNonNull(dsAccount.getValue(AccountInformation.class)).getType()); //set type
                                aInfo.setNumber(Objects.requireNonNull(dsAccount.getValue(AccountInformation.class)).getNumber()); //set number
                                aInfo.setMoney(Integer.toString(Integer.parseInt(balance)-Integer.parseInt(newMoney))); //set new balance
                                myAccountRef.child(dsAccount.getKey()).setValue(aInfo);
                                String newSend = aInfo.getNumber();
                                String newReceive = "Card payment";
                                String newMessage = "Authorized card payment";
                                TransactionInformation transaction = new TransactionInformation(newSend,newReceive,newMoney,newMessage, Calendar.getInstance().getTime());
                                String key = mFirebaseDatabase.getReference("transactions").push().getKey();
                                assert key != null;
                                myRef.child("transactions").child(key).setValue(transaction);
                                toastMessage("Siirto onnistui");
                                Intent iToBack = new Intent(CardPaymentActivity.this,HomeActivity.class);
                                startActivity(iToBack);
                            } else {
                                toastMessage("tili on lukittu");
                                break;
                            }
                        }
                    }
                } else{
                    toastMessage("limitti liian pieni");
                }
            }
        }
        toastMessage("korttia ei löytynyt");
    }
}
