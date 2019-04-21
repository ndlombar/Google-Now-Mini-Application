package com.c323finalproject.nicklombardi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText username, password;
    Button sign_in;
    TextView sign_up;
    MyDBHelperClass myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new MyDBHelperClass(this);
        username = findViewById(R.id.signin_username);
        password = findViewById(R.id.signin_password);
        sign_up = findViewById(R.id.signin_signup);
        sign_in = findViewById(R.id.signin_button);



        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    public void verify(View view) {
        Log.i("verifying", "in verifying");
        Cursor res = myDB.getAllData();
        boolean existingUser = false;
        while(res != null && res.moveToNext()){
            if(res.getString(0).matches(username.getText().toString()) &&
                    res.getString(1).matches(password.getText().toString())){
                existingUser = true;
            }
        }
        if(existingUser){
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            SharedPreferences myshprefs = getSharedPreferences("CurrentUser", MODE_PRIVATE);
            SharedPreferences.Editor editor = myshprefs.edit();
            editor.putString("username", username.getText().toString());
            editor.putString("email", getCurrentUserEmail());
            editor.putString("phone", getCurrentUserPhone());
            editor.putString("password", password.getText().toString());
            editor.commit();
            startActivity(intent);
        } else {
            Toast.makeText(this, "Username or password is incorrect.", Toast.LENGTH_SHORT).show();
        }
    }

    public String getCurrentUserEmail(){
        String emailResult = "";
        Cursor res = myDB.getAllData();
        while(res != null && res.moveToNext()){
            if(res.getString(0).matches(username.getText().toString())){
                emailResult = res.getString(2);
            }
        }
        return emailResult;
    }

    public int getCurrentUserProfilePicture(){
        int picResult = R.mipmap.empty_profile_picture;
        Cursor res = myDB.getAllData();
        while(res != null && res.moveToNext()){
            if(res.getString(0).matches(username.getText().toString())){
                picResult = res.getInt(4);
            }
        }
        return picResult;
    }

    public String getCurrentUserPhone(){
        String phoneResult = "";
        Cursor res = myDB.getAllData();
        while(res != null && res.moveToNext()){
            if(res.getString(0).matches(username.getText().toString())){
                phoneResult = res.getString(3);
            }
        }
        return phoneResult;
    }
}
