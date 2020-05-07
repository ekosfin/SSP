package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class AccountActivity extends AppCompatActivity {

    private static final String TAG = "AccountActivity";

    private String accountNumber,accountID;
    private TextView textMoney;
    private TextView accountBalance,accountType;

    private ArrayList<String> mSender, mReceiver, mAmount, mMessage;
    private ArrayList<Date> mDate;

    private ArrayList<String> mCardType,mCardULimit,mCardName;

    private DatabaseReference myAccountRef;
    private DataSnapshot tempData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        accountNumber = getIntent().getStringExtra("AccountID");

        //firebase stuff
        DatabaseReference myTransactionsRef = FirebaseDatabase.getInstance().getReference("transactions");
        DatabaseReference myCardsRef = FirebaseDatabase.getInstance().getReference("cards");
        myAccountRef = FirebaseDatabase.getInstance().getReference("accounts");


        // Read from the database
        myTransactionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                showTransferData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        myCardsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                showCardData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        myAccountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                storeAccounts(dataSnapshot);
                updateItems(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        Button change = findViewById(R.id.changeButton);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AccountActivity.this,ChangeAccountActivity.class);
                i.putExtra("accountNumber",accountNumber);
                i.putExtra("accountID",accountID);
                startActivity(i);
            }
        });


        Button transfer = findViewById(R.id.transferButton);
        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AccountActivity.this,TransferActivity.class);
                startActivity(i);
            }
        });

        ImageButton back = findViewById((R.id.backButton));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iToBack = new Intent(AccountActivity.this,HomeActivity.class);
                startActivity(iToBack);
            }
        });

        Button newCard = findViewById(R.id.cardButton);
        newCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent iNewCard = new Intent(AccountActivity.this,CardActivity.class);
                startActivity(iNewCard);
            }

        });

        Button delete = findViewById(R.id.deleteButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (DataSnapshot ds : tempData.getChildren()){
                    if (accountNumber.equals(Objects.requireNonNull(ds.getValue(AccountInformation.class)).getNumber())){
                        myAccountRef.child(Objects.requireNonNull(ds.getKey())).removeValue();
                        toastMessage("tili on poistettu");
                        Intent iToBack = new Intent(AccountActivity.this,HomeActivity.class);
                        startActivity(iToBack);
                    }
                }


            }
        });

        accountBalance = findViewById(R.id.accountBalance);
        accountType = findViewById(R.id.accountType);

        Button addMoney = findViewById(R.id.addMoneyButton);
        textMoney = findViewById(R.id.inputMoney);
        addMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int money = Integer.parseInt(textMoney.getText().toString());
                if ((money > 0) && (!Integer.toString(money).isEmpty())){
                    for (DataSnapshot ds : tempData.getChildren()){
                        if (accountNumber.equals(Objects.requireNonNull(ds.getValue(AccountInformation.class)).getNumber())){
                            AccountInformation aInfo = new AccountInformation();
                            aInfo.setType(Objects.requireNonNull(ds.getValue(AccountInformation.class)).getType());
                            String balance = Objects.requireNonNull(ds.getValue(AccountInformation.class)).getMoney();
                            aInfo.setMoney(Integer.toString(Integer.parseInt(balance)+money));
                            aInfo.setOwner(Objects.requireNonNull(ds.getValue(AccountInformation.class)).getOwner());
                            aInfo.setName(Objects.requireNonNull(ds.getValue(AccountInformation.class)).getName());
                            aInfo.setNumber(Objects.requireNonNull(ds.getValue(AccountInformation.class)).getNumber());
                            myAccountRef.child(Objects.requireNonNull(ds.getKey())).setValue(aInfo);
                            break;
                        }
                    }
                    toastMessage("Lisäys onnistui");
                    textMoney.setText("");
                } else {
                    toastMessage("Syötä haluttu summa");
                }
            }
        });
    }

    private void storeAccounts(DataSnapshot ds){
        this.tempData = ds;
        for (DataSnapshot dsAccounts : ds.getChildren()){
            if (accountNumber.equals(Objects.requireNonNull(dsAccounts.getValue(AccountInformation.class)).getNumber())){
                accountID = dsAccounts.getKey();
            }
        }
    }

    private void updateItems(DataSnapshot ds){
        for (DataSnapshot dsItems : ds.getChildren()){
            if (accountNumber.equals(Objects.requireNonNull(Objects.requireNonNull(dsItems.getValue(AccountInformation.class)).getNumber()))){
                accountBalance.setText(Objects.requireNonNull(dsItems.getValue(AccountInformation.class)).getMoney());
                accountType.setText(Objects.requireNonNull(dsItems.getValue(AccountInformation.class)).getType());
                break;
            }
        }
    }

    private void showTransferData(DataSnapshot dataSnapshot){
        mSender = new ArrayList<>();
        mReceiver = new ArrayList<>();
        mAmount = new ArrayList<>();
        mMessage = new ArrayList<>();
        mDate = new ArrayList<>();
        for(DataSnapshot dsTransactions : dataSnapshot.getChildren()) {
            if ((Objects.requireNonNull(dsTransactions.getValue(TransactionInformation.class)).getSender().equals(accountNumber)) || (Objects.requireNonNull(dsTransactions.getValue(TransactionInformation.class)).getReceiver().equals(accountNumber))){
                TransactionInformation tInfo = new TransactionInformation();
                tInfo.setAmount(Objects.requireNonNull(dsTransactions.getValue(TransactionInformation.class)).getAmount());
                tInfo.setMessage(Objects.requireNonNull(dsTransactions.getValue(TransactionInformation.class)).getMessage());
                tInfo.setReceiver(Objects.requireNonNull(dsTransactions.getValue(TransactionInformation.class)).getReceiver());
                tInfo.setSender(Objects.requireNonNull(dsTransactions.getValue(TransactionInformation.class)).getSender());
                tInfo.setTransactionDate(Objects.requireNonNull(dsTransactions.getValue(TransactionInformation.class)).getDate());
                mSender.add(tInfo.getSender());
                mReceiver.add(tInfo.getReceiver());
                mAmount.add(tInfo.getAmount());
                mMessage.add(tInfo.getMessage());
                mDate.add(tInfo.getDate());
            }
        }

        initTransferRecyclerView();
    }

    private void initTransferRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.accountHistory);
        TransferRecyclerViewAdapter adapter = new TransferRecyclerViewAdapter(mSender,mReceiver,mAmount,mMessage,mDate,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void showCardData(DataSnapshot dataSnapshot){
        mCardType = new ArrayList<>();
        mCardULimit = new ArrayList<>();
        mCardName = new ArrayList<>();
        for(DataSnapshot dsCards : dataSnapshot.getChildren()) {
            if ((accountNumber.equals(Objects.requireNonNull(dsCards.getValue(CardInformation.class)).getLinkedAccount()))){
                CardInformation cInfo = new CardInformation();
                cInfo.setType(Objects.requireNonNull(dsCards.getValue(CardInformation.class)).getType());
                cInfo.setUsageLimit(Objects.requireNonNull(dsCards.getValue(CardInformation.class)).getUsageLimit());
                cInfo.setNimi(Objects.requireNonNull(dsCards.getValue(CardInformation.class)).getNimi());
                mCardType.add(cInfo.getType());
                mCardULimit.add(cInfo.getUsageLimit());
                mCardName.add(cInfo.getNimi());
            }
        }

        initCardsRecyclerView();
    }

    private void initCardsRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.cardList);
        CardsRecyclerViewAdapeter adapter = new CardsRecyclerViewAdapeter(mCardName,mCardType,mCardULimit,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
