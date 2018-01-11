package com.qiscus.chat.sample.ui.homepagetab;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import com.qiscus.chat.sample.R;
import com.qiscus.chat.sample.model.Room;

/**
 * Created by omayib on 30/10/17.
 */

public class RecentConversationFragmentRecyclerAdapter extends RecyclerView.Adapter<com.qiscus.chat.sample.ui.homepagetab.RecentConversationFragmentHolder> {
    private ArrayList<Room> rooms;

    public RecentConversationFragmentRecyclerAdapter(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public com.qiscus.chat.sample.ui.homepagetab.RecentConversationFragmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_conversation, parent, false);
        return new com.qiscus.chat.sample.ui.homepagetab.RecentConversationFragmentHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(com.qiscus.chat.sample.ui.homepagetab.RecentConversationFragmentHolder holder, int position) {
        Room room = rooms.get(position);
        holder.bindRecentConversation(room);
    }

    @Override
    public int getItemCount() {
        return this.rooms.size();
    }
}
