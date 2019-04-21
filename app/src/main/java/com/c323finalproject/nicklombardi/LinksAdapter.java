package com.c323finalproject.nicklombardi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class LinksAdapter extends RecyclerView.Adapter<LinksAdapter.RecyclerViewHolder> {

    SharedPreferences myshprefs;
    ArrayList<SavedLinks> savedLinks;
    Context context;
    Activity activity;

    public LinksAdapter(ArrayList<SavedLinks> savedLinks, Context context, Activity activity) {
        this.savedLinks = savedLinks;
        this.context = context;
        this.activity = activity;
    }


    @NonNull
    @Override
    public LinksAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.links_layout, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final LinksAdapter.RecyclerViewHolder holder, int position) {
        myshprefs = context.getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        holder.title.setText(savedLinks.get(position).getLinkTitle());
        holder.link.setText(savedLinks.get(position).getLinkLink());
        holder.cardView1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
        holder.cardView1.setOnTouchListener(new View.OnTouchListener() {
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
        return savedLinks.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView title, link;
        CardView cardView1;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.links_title);
            link = itemView.findViewById(R.id.links_link);
            cardView1 = itemView.findViewById(R.id.links_card_view);
        }
    }
}
