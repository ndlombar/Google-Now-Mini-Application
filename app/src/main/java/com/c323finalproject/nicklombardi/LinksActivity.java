package com.c323finalproject.nicklombardi;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LinksActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<SavedLinks> savedLinks;
    SharedPreferences myshprefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        savedLinks = new ArrayList<>();
        populateSavedLinks();
        Log.v("SavedLinks", savedLinks.toString());
        populateRecyclerView();
    }

    private void populateRecyclerView() {
        recyclerView = findViewById(R.id.links_recycler);
        adapter = new LinksAdapter(savedLinks, this, this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void populateSavedLinks() {
        myshprefs = getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        String filename = "saved-"+ myshprefs.getString("username", "default") +"-files.txt";
        try {
            FileInputStream fis = openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line = "";
            while((line = br.readLine()) != null){
                savedLinks.add(new SavedLinks(line, br.readLine()));
            }
            Log.v("hdugihufdhdi", sb.toString());
            Toast.makeText(this, savedLinks.toString(), Toast.LENGTH_SHORT).show();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
