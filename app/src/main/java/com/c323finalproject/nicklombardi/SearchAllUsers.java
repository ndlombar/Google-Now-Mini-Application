package com.c323finalproject.nicklombardi;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.SearchView;

import java.util.ArrayList;

public class SearchAllUsers extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    MyDBHelperClass myDB;
    ArrayList<User> users;
    SharedPreferences myshprefs;
    SearchView query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_all_users);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myDB = new MyDBHelperClass(this);
        myshprefs = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        users = new ArrayList<>();
        query = findViewById(R.id.search_query);

        query.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!query.matches("")){
                    users.clear();
                    populateUsers(query);
                    populateRecyclerView();
                } else {
                    users.clear();
                    populateRecyclerView();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.matches("")){
                    users.clear();
                    populateUsers(newText);
                    populateRecyclerView();
                } else {
                    users.clear();
                    populateRecyclerView();
                }
                return true;
            }
        });
    }

    private void populateRecyclerView() {
        recyclerView = findViewById(R.id.search_recycler);
        adapter = new SearchAdapter(users, this);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

    }

    private void populateUsers(String newText) {
        Cursor res = myDB.getAllData();
        while(res != null && res.moveToNext()){
            String username = res.getString(0);
            byte[] image = res.getBlob(4);
            Bitmap profile_pic = BitmapFactory.decodeByteArray(image, 0, image.length);
            if((!username.matches(myshprefs.getString("username", "default"))) &&
                    username.contains(newText)){
                users.add(new User(res.getString(0), profile_pic, res.getString(3)));
            }
        }
    }
}
