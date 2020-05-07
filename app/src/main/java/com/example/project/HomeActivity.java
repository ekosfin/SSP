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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {   // Home
    //sources: https://github.com/mitchtabian/Firebase-Save-User-Information/blob/master/FirebaseSaveUserData/app/src/main/java/com/tabian/firebasesaveuserdata/ViewDatabase.java
    // and https://github.com/mitchtabian/Recyclerview/blob/master/RecyclerView/app/src/main/java/codingwithmitch/com/recyclerview/MainActivity.java

    private static final String TAG = "HomeActivity";

    private Button logoutButton;
    private Button userButton;
    private Button paymentButton;
    private Button withdrawButton;
    private Button transferButton;
    private Button addAccountButton;
    private Button addCardButton;
    private String userID;


    private ArrayList<String> mType;
    private ArrayList<String> mName;
    private ArrayList<String> mMoney;
    private ArrayList<String> mNumber;

    //firebase stuff
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //firebase stuff
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("accounts");
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        userID = user.getUid();

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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
                Intent iToMain = new Intent(HomeActivity.this,MainActivity.class);
                startActivity(iToMain);
            }
        });

        userButton = findViewById((R.id.userButton));
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iToUser = new Intent(HomeActivity.this,UserActivity.class);
                startActivity(iToUser);
            }
        });

        paymentButton = findViewById((R.id.paymentButton));
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iToPayment = new Intent(HomeActivity.this,CardPaymentActivity.class);
                startActivity(iToPayment);
            }
        });

        withdrawButton = findViewById((R.id.withdrawButton));
        withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iToWithdraw = new Intent(HomeActivity.this, WithdrawActivity.class);
                startActivity(iToWithdraw);
            }
        });

        transferButton = findViewById((R.id.transferButton));
        transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iToTransfer = new Intent(HomeActivity.this,TransferActivity.class);
                startActivity(iToTransfer);
            }
        });

        addAccountButton = findViewById((R.id.addAccountButton));
        addAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iToAddAccount = new Intent(HomeActivity.this,AddAccountActivity.class);
                startActivity(iToAddAccount);
            }
        });

        addCardButton = findViewById((R.id.addCardButton));
        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iToAddCard = new Intent(HomeActivity.this,CardActivity.class);
                startActivity(iToAddCard);
            }
        });

    }

    private void showData(DataSnapshot dataSnapshot){   //Show accounts
        mType = new ArrayList<>();
        mName = new ArrayList<>();
        mMoney = new ArrayList<>();
        mNumber = new ArrayList<>();
        for(DataSnapshot ds : dataSnapshot.getChildren()) {

            //only including accounts that are owned by the user
            if (userID.equals(Objects.requireNonNull(ds.getValue(AccountInformation.class)).getOwner())) {
                AccountInformation aInfo = new AccountInformation();
                aInfo.setName(Objects.requireNonNull(ds.getValue(AccountInformation.class)).getName()); // set name
                aInfo.setOwner(Objects.requireNonNull(ds.getValue(AccountInformation.class)).getOwner()); //set owner aka userid
                aInfo.setMoney(Objects.requireNonNull(ds.getValue(AccountInformation.class)).getMoney()); //set money
                aInfo.setType(Objects.requireNonNull(ds.getValue(AccountInformation.class)).getType()); //set type
                aInfo.setNumber(Objects.requireNonNull(ds.getValue(AccountInformation.class)).getNumber()); //set number
                mType.add(aInfo.getType());
                mName.add(aInfo.getName());
                mMoney.add(aInfo.getMoney());
                mNumber.add(aInfo.getNumber());
            }
        }

        initRecyclerView();
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.showAccounts);
        AccountRecyclerViewAdapter adapter = new AccountRecyclerViewAdapter(mType,mName,mMoney,mNumber,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
