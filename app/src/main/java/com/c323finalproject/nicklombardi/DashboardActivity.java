package com.c323finalproject.nicklombardi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText search;
    Button button;
    private static final String TAG = "searchApp";
    public static final int MY_EDIT_PROFILE = 0;
    String responseMessage = "", result = "";
    ArrayList<DashboardSearchResult> sResults;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    SharedPreferences myshprefs;
    MyDBHelperClass myDB;
    TextView nav_view_username, nav_view_email;
    ImageView nav_view_profile_pic;
    GestureDetectorCompat gDetector;
    private int responseCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);
        myDB = new MyDBHelperClass(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        CardView cardView = findViewById(R.id.dash_card);
        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(), "onLongClick", Toast.LENGTH_SHORT).show();
                Log.v("GestureDetector", "in onLongClick");
                return true;
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        myshprefs = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        String username = myshprefs.getString("username", "default");
        String email = myshprefs.getString("email", "default");
        View hView = navigationView.getHeaderView(0);
        nav_view_username = hView.findViewById(R.id.nav_header_username);
        nav_view_email = hView.findViewById(R.id.nav_header_email);
        nav_view_profile_pic = hView.findViewById(R.id.nav_header_profile_picture);

        nav_view_username.setText(username);
        nav_view_email.setText(email);
        nav_view_profile_pic.setImageBitmap(getCurrentProfilePic());


        search = findViewById(R.id.dash_searchQuery);
        button = findViewById(R.id.dash_searchButton);
        sResults = new ArrayList<>();

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sResults.clear();
                String noSpaces = search.getText().toString().replace(" ", "+");
                String key = "AIzaSyDxHnRv7stybScjd9poohAzBEyGtUIvuPI";
                String id = "008303847517799368783:gudlpwevuvk";
                String urlString = "https://www.googleapis.com/customsearch/v1?q=" + noSpaces + "&key=" + key + "&cx=" + id + "&alt=json&";

                URL url = null;
                try{
                    url = new URL(urlString);
                } catch (MalformedURLException e){
                    e.printStackTrace();
                }
                GoogleSearchAsyncTask searchTask = new GoogleSearchAsyncTask(getApplicationContext(), url);
                searchTask.execute(url);
            }
        });
        formatJSON();
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
            Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
            startActivityForResult(intent, MY_EDIT_PROFILE);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(DashboardActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_saved_links){
            Intent intent = new Intent(DashboardActivity.this, LinksActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout){
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MY_EDIT_PROFILE){
            Cursor res = myDB.getAllData();
            while(res != null && res.moveToNext()){
                if(res.getString(0).matches(myshprefs.getString("username", "default"))){
                    byte[] image = res.getBlob(4);
                    nav_view_username.setText(res.getString(0));
                    nav_view_email.setText(res.getString(2));
                    nav_view_profile_pic.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
                }
            }
        }
    }

    private void formatJSON() {
        try {
            JSONObject searchResults = new JSONObject(result);
            JSONArray items = searchResults.getJSONArray("items");
            Log.d(TAG, "items="+items);
            for(int i = 0; i<items.length(); i++){
                JSONObject current = (JSONObject) items.get(i);
                Log.d(TAG, "Title: "+current.getString("title"));
                Log.d(TAG, "Link: "+current.getString("link"));
                Log.d(TAG, "Snippet: "+current.getString("snippet"));
                sResults.add(new DashboardSearchResult(current.getString("title"),
                        current.getString("link"),
                        current.getString("snippet")));
            }
            populateRecyclerView();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void populateRecyclerView() {
        recyclerView = findViewById(R.id.dash_recycler);
        adapter = new DSRAdapter(sResults, this, this);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private class GoogleSearchAsyncTask extends AsyncTask<URL, Integer, String> {
        
        URL searchUrl;
        Context context;

        public GoogleSearchAsyncTask(Context context, URL url) {
            this.context = context;
            this.searchUrl = url;
        }

        @Override
        protected String doInBackground(URL... urls) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            boolean isConnected = ni != null && ni.isConnectedOrConnecting();
            if(hasActiveNetworkConnection(context)){
                URL url = urls[0];
                searchUrl = urls[0];
                Log.d(TAG, "AsyncTask - doInBackground, url=" + url);
                HttpURLConnection conn = null;
                try {
                    conn = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    Log.e(TAG, "Http connection ERROR " + e.toString());
                }
                try {
                    responseCode = conn.getResponseCode();
                    responseMessage = conn.getResponseMessage();
                } catch (IOException e) {
                    Log.e(TAG, "Http getting response code ERROR " + e.toString());
                }
                Log.d(TAG, "Http response code =" + responseCode + " message=" + responseMessage);
                try {
                    if(responseCode == 200) {
                        // response OK
                        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = rd.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        rd.close();
                        conn.disconnect();
                        result = sb.toString();
                        Log.d(TAG, "result=" + result);
                        return result;
                    } else {
                        // response problem
                        String errorMsg = "Http ERROR response " + responseMessage + "\n" + "Make sure to replace in code your own Google API key and Search Engine ID";
                        Log.e(TAG, errorMsg);
                        result = errorMsg;
                        return result;
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Http Response ERROR " + e.toString());
                }
            } else {
                Cursor res = myDB.getAllData();
                while(res != null && res.moveToNext()){
                    if(res.getString(0).matches(myshprefs.getString("username", "default"))){
                        if(res.getString(5).matches("off")){
                            return "off";
                        }
                    }
                }
                Log.v("NetworkChecking", "No network connection!");
            }
            return result;
        }

        protected void onPostExecute(String result) {

            Log.v(TAG, "result="+result);
            if(result.matches("off")){
                //start background thread
                Toast.makeText(getApplicationContext(), "Offline not enabled. Please go to settings and enable offline search to receive your results when your device gets an internet connection.", Toast.LENGTH_LONG).show();
            } else if(result.matches("")){
                Log.d(TAG, "AsyncTask - onPostExecute, result=" + result);
                new InternetBackgroundThread(getApplicationContext(), searchUrl).execute();
            } else {
                formatJSON();
            }
        }

        private boolean isNetworkAvailable(Context context) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null;
        }

        public boolean hasActiveNetworkConnection(Context context){
            boolean result = false;
            if(isNetworkAvailable(context)){
                try{
                    HttpURLConnection urlConnection = (HttpURLConnection) new URL("https://google.com").openConnection();
                    urlConnection.setRequestProperty("User-Agent", "Test");
                    urlConnection.setRequestProperty("Connection", "close");
                    urlConnection.setConnectTimeout(1500);
                    urlConnection.connect();
                    result = urlConnection.getResponseCode() == 200;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
    }

    private class InternetBackgroundThread extends AsyncTask<String, Void, String>{

        Context context;
        URL urlSearch;

        public InternetBackgroundThread(Context context, URL urlSearch) {
            this.context = context;
            this.urlSearch = urlSearch;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            while(true){
                if(hasActiveNetworkConnection(context))
                    break;
            }
            result = "working";

            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            new DashboardActivity.GoogleSearchAsyncTask(context, urlSearch).execute(urlSearch);
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
        }

        private boolean isNetworkAvailable(Context context) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null;
        }

        public boolean hasActiveNetworkConnection(Context context){
            boolean result = false;
            if(isNetworkAvailable(context)){
                try{
                    HttpURLConnection urlConnection = (HttpURLConnection) new URL("https://google.com").openConnection();
                    urlConnection.setRequestProperty("User-Agent", "Test");
                    urlConnection.setRequestProperty("Connection", "close");
                    urlConnection.setConnectTimeout(1500);
                    urlConnection.connect();
                    result = urlConnection.getResponseCode() == 200;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
    }
}