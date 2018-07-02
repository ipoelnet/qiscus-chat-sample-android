package com.qiscus.chat.sample.ui.privatechatcreation;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import com.qiscus.chat.sample.R;
import com.qiscus.chat.sample.model.User;

public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {
    private final ArrayList<User> users;
    private final ViewHolder.OnContactClickedListener listener;

    public RecyclerAdapter(ArrayList<User> users, ViewHolder.OnContactClickedListener listener) {
        this.users = users;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alumni, parent, false);
        return new ViewHolder(inflatedView, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = users.get(position);
        holder.bindAlumni(user);
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }


}