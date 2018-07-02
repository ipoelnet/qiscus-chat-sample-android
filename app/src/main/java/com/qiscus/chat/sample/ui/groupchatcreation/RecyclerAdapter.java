package com.qiscus.chat.sample.ui.groupchatcreation;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import com.qiscus.chat.sample.R;
import com.qiscus.chat.sample.model.User;
import com.qiscus.chat.sample.model.SelectableContact;

public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {
    private final ArrayList<SelectableContact> contacts = new ArrayList<>();
    private final ViewHolder.OnContactClickedListener listener;

    public RecyclerAdapter(ArrayList<User> users, ViewHolder.OnContactClickedListener listener) {
        this.listener = listener;
        for (User user :
                users) {
            this.contacts.add(new SelectableContact(user, user.isSelected()));
            }
    }

    public RecyclerAdapter(ArrayList<User> users, ViewHolder.OnContactClickedListener listener, boolean isSelected) {
        this.listener = listener;
        for (User user :
                users) {
            this.contacts.add(new SelectableContact(user,isSelected));
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alumni, parent, false);
        return new ViewHolder(inflatedView, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SelectableContact person = contacts.get(position);
        holder.bindAlumni(person);
    }

    @Override
    public int getItemCount() {
        return this.contacts.size();
    }


}