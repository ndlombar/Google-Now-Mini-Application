package com.c323finalproject.nicklombardi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class DSRAdapter extends RecyclerView.Adapter<DSRAdapter.RecycleViewHolder>{

    ArrayList<DashboardSearchResult> searchResults;
    Context context;
    Activity activity;
    SharedPreferences myshprefs;

    public DSRAdapter(ArrayList<DashboardSearchResult> searchResults, Context context, Activity activity){
        this.searchResults = searchResults;
        this.context = context;
        this.activity = activity;
    }


    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_results_layout, parent, false);
        RecycleViewHolder recycleViewHolder = new RecycleViewHolder(view);
        return recycleViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecycleViewHolder holder, int position) {
        myshprefs = context.getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        holder.title.setText(searchResults.get(position).getTitle());
        holder.snippet.setText(searchResults.get(position).getSnippet());
        holder.link.setText(searchResults.get(position).getLink());
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String filename = "saved-"+ myshprefs.getString("username", "default") +"-files.txt";
                String contents = holder.title.getText().toString() +"\n"+holder.link.getText().toString()+"\n";
                FileOutputStream fileOutputStream;
                try{
                    fileOutputStream = context.openFileOutput(filename, Context.MODE_APPEND);
                    fileOutputStream.write(contents.getBytes());
                    fileOutputStream.close();
                    Toast.makeText(context, "Link saved successfully", Toast.LENGTH_SHORT).show();
                } catch(Exception e){
                    Toast.makeText(context, "Error saving link", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        holder.cardView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    SharedPreferences.Editor editor = myshprefs.edit();
                    editor.putString("sms_link", holder.link.getText().toString());
                    editor.putString("sms_title", holder.title.getText().toString());
                    editor.commit();
                    Intent intent = new Intent(activity, SearchAllUsers.class);
                    context.startActivity(intent);
                    return true;
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(gestureDetector.onTouchEvent(event))
                    return true;
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder{

        TextView title, snippet, link;
        CardView cardView;

        public RecycleViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_textview);
            snippet = itemView.findViewById(R.id.snippet_textview);
            link = itemView.findViewById(R.id.link_textview);
            cardView = itemView.findViewById(R.id.search_dash);
        }
    }
}
