package com.c323finalproject.nicklombardi;

import android.content.ContentValues;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    EditText username, password, email, phone;
    Button register;
    MyDBHelperClass myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        myDB = new MyDBHelperClass(this);
        username = findViewById(R.id.signup_username);
        password = findViewById(R.id.signup_password);
        email = findViewById(R.id.signup_email);
        phone = findViewById(R.id.signup_phone_number);
        register = findViewById(R.id.signup_register);
    }

    public void register(View view) {
        String user = username.getText().toString();
        String pass = password.getText().toString();
        String emailAcc = email.getText().toString();
        String phoneNum = phone.getText().toString();
        if(user.matches("") || pass.matches("") || emailAcc.matches("") || phoneNum.matches("")){
            Toast.makeText(this, "Please fill out all fields before trying to register.", Toast.LENGTH_SHORT).show();
        } else {
            boolean isInserted = myDB.insertData(username.getText().toString(),
                    password.getText().toString(),
                    email.getText().toString(),
                    phone.getText().toString(),
                    MyDBHelperClass.getBitmapAsByteArray(BitmapFactory.decodeResource(getResources(), R.mipmap.empty_profile_picture)),
                    "off",
                    "off");
            if(isInserted){
                Toast.makeText(this, "User registered", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Could not register user", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
