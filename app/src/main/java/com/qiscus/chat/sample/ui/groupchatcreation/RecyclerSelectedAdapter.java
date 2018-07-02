package com.qiscus.chat.sample.ui.groupchatcreation;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import com.qiscus.chat.sample.R;
import com.qiscus.chat.sample.model.User;

/**
 * Created by asyrof on 04/12/17.
 */


public class RecyclerSelectedAdapter extends RecyclerView.Adapter<SelectedViewHolder> {
    private ArrayList<User> selectedContacts = new ArrayList<>();
    private final SelectedViewHolder.OnContactClickedListener listener;

    public RecyclerSelectedAdapter(ArrayList<User> users, SelectedViewHolder.OnContactClickedListener listener) {
        this.listener = listener;
        selectedContacts = users;
    }

    @Override
    public SelectedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected, parent, false);
        return new SelectedViewHolder(inflatedView, listener);
    }

    @Override
    public void onBindViewHolder(SelectedViewHolder holder, int position) {
        final User user = selectedContacts.get(position);
        holder.bindSelected(user);
    }


    @Override
    public int getItemCount() {
        return this.selectedContacts.size();
    }


}
