package com.c323finalproject.nicklombardi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.RecycleViewHolder> {

    ArrayList<User> users;
    Context context;
    SharedPreferences myshprefs;


    public SearchAdapter(ArrayList<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_search, parent, false);
        RecycleViewHolder recycleViewHolder = new RecycleViewHolder(view);
        return recycleViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecycleViewHolder holder, final int position) {
        myshprefs = context.getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        holder.username.setText(users.get(position).getUsername());
        holder.profile_pic.setImageBitmap(users.get(position).getProfile_pic());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "Hey " + holder.username.getText().toString() + ", check out " +
                        myshprefs.getString("sms_title", "default") + ". " + myshprefs.getString("sms_link", "default");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + users.get(position).getNumber()));
                intent.putExtra("sms_body", message);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public class RecycleViewHolder extends RecyclerView.ViewHolder{

        TextView username;
        ImageView profile_pic;
        CardView cardView;

        public RecycleViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.results_username);
            profile_pic = itemView.findViewById(R.id.results_profile_pic);
            cardView = itemView.findViewById(R.id.results_card);
        }
    }
}
