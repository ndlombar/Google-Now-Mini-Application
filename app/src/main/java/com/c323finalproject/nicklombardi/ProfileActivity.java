package com.c323finalproject.nicklombardi;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


public class ProfileActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    EditText username, email, password, phone;
    ImageButton edit_profile_pic;
    ImageView profile_pic;
    Button save;
    SharedPreferences myshprefs;
    SharedPreferences.Editor editor;
    MyDBHelperClass myDB;
    Bitmap new_profile_pic;
    String offlineState, youtubeState;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        myshprefs = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        editor = myshprefs.edit();
        myDB = new MyDBHelperClass(this);
        new_profile_pic = getCurrentProfilePic();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getCurrentUserSettings();

        username = findViewById(R.id.profile_username);
        email = findViewById(R.id.profile_email);
        password = findViewById(R.id.profile_password);
        phone = findViewById(R.id.profile_phone);
        edit_profile_pic = findViewById(R.id.profile_edit);
        profile_pic = findViewById(R.id.profile_pic);
        save = findViewById(R.id.profile_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myDB.deleteRow(myshprefs.getString("username", "default"))){
                    myDB.insertData(username.getText().toString(),
                            password.getText().toString(),
                            email.getText().toString(),
                            phone.getText().toString(),
                            MyDBHelperClass.getBitmapAsByteArray(new_profile_pic),
                            offlineState,
                            youtubeState);
                    editor.putString("username", username.getText().toString());
                    editor.putString("password", password.getText().toString());
                    editor.putString("email", email.getText().toString());
                    editor.putString("phone", phone.getText().toString());
                    editor.commit();
                    finishActivity(DashboardActivity.MY_EDIT_PROFILE);
                    Toast.makeText(getApplicationContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Profile couldn't be updated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        username.setText(myshprefs.getString("username", "not found"));
        email.setText(myshprefs.getString("email", "not found"));
        password.setText(myshprefs.getString("password", "not found"));
        phone.setText(myshprefs.getString("phone", "not found"));
        profile_pic.setImageBitmap(getCurrentProfilePic());
    }

    public Bitmap getCurrentProfilePic(){
        Cursor res = myDB.getAllData();
        while(res != null && res.moveToNext()){
            if(res.getString(0).matches(myshprefs.getString("username", "default"))){
                byte[] image = res.getBlob(4);
                return BitmapFactory.decodeByteArray(image, 0, image.length);
            }
        }
        return null;
    }

    public void getCurrentUserSettings(){
        Cursor res = myDB.getAllData();
        offlineState = "off";
        youtubeState = "off";
        while(res != null && res.moveToNext()){
            if(res.getString(0).matches(myshprefs.getString("username", "default"))){
                offlineState = res.getString(5);
                youtubeState = res.getString(6);
            }
        }
    }

    public void openCamera(View view) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        } else {
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takePicture.resolveActivity(getPackageManager()) != null){
                startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            new_profile_pic = (Bitmap) extras.get("data");
            profile_pic.setImageBitmap(new_profile_pic);
//            if(myDB.deleteRow(myshprefs.getString("username", "default"))){
//                myDB.insertData(myshprefs.getString("username", "default"),
//                        myshprefs.getString("password", "default"),
//                        myshprefs.getString("email", "default"),
//                        myshprefs.getString("phone", "default"),
//                        MyDBHelperClass.getBitmapAsByteArray(new_profile_pic),
//                        offlineState,
//                        youtubeState);
//                Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "couldn't update", Toast.LENGTH_SHORT).show();
//            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if(takePicture.resolveActivity(getPackageManager()) != null){
                        startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
                    }
                } else {
                    Toast.makeText(this, "I'm sorry, I do not have permission to use the camera.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
