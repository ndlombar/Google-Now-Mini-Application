package com.c323finalproject.nicklombardi;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    Switch offline, youtube;
    MyDBHelperClass myDB;
    String offlineState, youtubeState;
    SharedPreferences myshprefs;
    Bitmap profile_pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myDB = new MyDBHelperClass(this);
        myshprefs = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        profile_pic = getCurrentProfilePic();

        offline = findViewById(R.id.settings_offline_switch);
        youtube = findViewById(R.id.settings_youtube_switch);
        getCurrentUserSettings();

        if(offlineState.matches("off")){
            offline.setChecked(false);
        } else {
            offline.setChecked(true);
        }

        if(youtubeState.matches("off")){
            youtube.setChecked(false);
        } else {
            youtube.setChecked(true);
        }

        offline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    offlineState = "on";
                    if(myDB.deleteRow(myshprefs.getString("username", "default"))){
                        myDB.insertData(myshprefs.getString("username", "default"),
                                myshprefs.getString("password", "default"),
                                myshprefs.getString("email", "default"),
                                myshprefs.getString("phone", "default"),
                                MyDBHelperClass.getBitmapAsByteArray(profile_pic),
                                "on",
                                youtubeState);
                    }
                } else {
                    offlineState = "off";
                    if(myDB.deleteRow(myshprefs.getString("username", "default"))){
                        myDB.insertData(myshprefs.getString("username", "default"),
                                myshprefs.getString("password", "default"),
                                myshprefs.getString("email", "default"),
                                myshprefs.getString("phone", "default"),
                                MyDBHelperClass.getBitmapAsByteArray(profile_pic),
                                "off",
                                youtubeState);
                    }
                }
            }
        });

        youtube.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                youtubeState = "on";
                if(isChecked){
                    if(myDB.deleteRow(myshprefs.getString("username", "default"))){
                        myDB.insertData(myshprefs.getString("username", "default"),
                                myshprefs.getString("password", "default"),
                                myshprefs.getString("email", "default"),
                                myshprefs.getString("phone", "default"),
                                MyDBHelperClass.getBitmapAsByteArray(profile_pic),
                                offlineState,
                                "on");
                    }
                } else {
                    youtubeState = "off";
                    if(myDB.deleteRow(myshprefs.getString("username", "default"))){
                        myDB.insertData(myshprefs.getString("username", "default"),
                                myshprefs.getString("password", "default"),
                                myshprefs.getString("email", "default"),
                                myshprefs.getString("phone", "default"),
                                MyDBHelperClass.getBitmapAsByteArray(profile_pic),
                                offlineState,
                                "off");
                    }
                }
            }
        });
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
}
