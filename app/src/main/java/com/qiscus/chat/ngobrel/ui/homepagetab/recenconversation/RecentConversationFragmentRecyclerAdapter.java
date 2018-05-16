package com.qiscus.chat.ngobrel.ui.homepagetab.recenconversation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import com.qiscus.chat.ngobrel.R;
import com.qiscus.chat.ngobrel.model.Room;
import com.qiscus.chat.ngobrel.ui.common.SortedRecyclerViewAdapter;

/**
 * Created by omayib on 30/10/17.
 */

public class RecentConversationFragmentRecyclerAdapter extends SortedRecyclerViewAdapter<Room, RecentConversationFragmentHolder> {
    private ArrayList<Room> rooms;

    public RecentConversationFragmentRecyclerAdapter(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public RecentConversationFragmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_conversation, parent, false);
        return new RecentConversationFragmentHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecentConversationFragmentHolder holder, int position) {
        Room room = rooms.get(position);
        holder.bindRecentConversation(room);
    }

    @Override
    protected Class<Room> getItemClass() {
        return Room.class;
    }

    @Override
    protected int compare(Room item1, Room item2) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return this.rooms.size();
    }
}
