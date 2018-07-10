package com.qiscus.chat.sample.ui.homepagetab.recentconversation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.qiscus.chat.sample.R;
import com.qiscus.chat.sample.ui.common.OnItemClickListener;
import com.qiscus.chat.sample.ui.common.SortedRecyclerViewAdapter;
import com.qiscus.sdk.data.model.QiscusChatRoom;

/**
 * Created on : May 16, 2018
 * Author     : zetbaitsu
 * Name       : Zetra
 * GitHub     : https://github.com/zetbaitsu
 */
public class RecentConversationAdapter extends SortedRecyclerViewAdapter<QiscusChatRoom, RecentConversationViewHolder> {
    private Context context;
    private OnItemClickListener onItemClickListener;

    public RecentConversationAdapter(Context context, OnItemClickListener onItemClickListener) {
        super();
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    protected Class<QiscusChatRoom> getItemClass() {
        return QiscusChatRoom.class;
    }

    @Override
    protected int compare(QiscusChatRoom lhs, QiscusChatRoom rhs) {
        if (lhs.getLastComment() != null && rhs.getLastComment() != null) {
            return rhs.getLastComment().getTime().compareTo(lhs.getLastComment().getTime());
        }
        if (lhs.getLastComment() != null && rhs.getLastComment() == null) {
            return -1;
        }
        if (lhs.getLastComment() == null && rhs.getLastComment() != null) {
            return 1;
        }
        return lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
    }

    @NonNull
    @Override
    public RecentConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecentConversationViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_recent_conversation, parent, false), onItemClickListener
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecentConversationViewHolder holder, int position) {
        holder.bind(getData().get(position));
    }
}
