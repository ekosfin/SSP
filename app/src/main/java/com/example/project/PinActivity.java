package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class PinActivity extends AppCompatActivity {    //Pin validation

    private String pin = Integer.toString((int) (Math.round(Math.random()*((999999-100000)+1))+100000)); // Create random pin
    private EditText inputPin;
    private TextView showPin;
    private Button submitPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        showPin = findViewById(R.id.pinView);
        showPin.setText(pin); // Show pin

        addListenerOnButton(); // Button

    }

     public void addListenerOnButton(){ //define button

        inputPin = (EditText) findViewById(R.id.pinInput);
        submitPin = (Button) findViewById(R.id.pinButton);

        submitPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userPin = inputPin.getText().toString();
                if(userPin.equals(pin)){
                    Intent iToHome = new Intent(PinActivity.this,HomeActivity.class);
                    startActivity(iToHome);
                } else{
                    Toast.makeText(PinActivity.this,"Väärä koodi.",Toast.LENGTH_SHORT).show();
                }
            }
        });

     }

}
