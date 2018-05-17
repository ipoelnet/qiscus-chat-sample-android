package com.qiscus.chat.ngobrel.ui.homepagetab.recentconversation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.qiscus.chat.ngobrel.R;
import com.qiscus.chat.ngobrel.ui.common.OnItemClickListener;
import com.qiscus.chat.ngobrel.ui.common.SortedRecyclerViewAdapter;
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
    protected int compare(QiscusChatRoom item1, QiscusChatRoom item2) {
        return item2.getLastComment().getTime().compareTo(item1.getLastComment().getTime());
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
