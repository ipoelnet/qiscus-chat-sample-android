package com.qiscus.chat.sample.ui.groupdetail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiscus.chat.sample.R;
import com.qiscus.chat.sample.ui.common.OnItemClickListener;
import com.qiscus.nirmana.Nirmana;
import com.qiscus.sdk.chat.core.data.model.QiscusRoomMember;

/**
 * Created on : May 16, 2018
 * Author     : zetbaitsu
 * Name       : Zetra
 * GitHub     : https://github.com/zetbaitsu
 */
public class MemberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView nameTextView;
    private ImageView avatarImageView;

    private OnItemClickListener onItemClickListener;

    public MemberViewHolder(View itemView, OnItemClickListener onItemClickListener) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.onItemClickListener = onItemClickListener;
        nameTextView = itemView.findViewById(R.id.textViewName);
        avatarImageView = itemView.findViewById(R.id.imageViewProfile);
    }

    public void bind(QiscusRoomMember qiscusRoomMember) {
        nameTextView.setText(qiscusRoomMember.getUsername());
        Nirmana.getInstance().get().load(qiscusRoomMember.getAvatar()).into(avatarImageView);
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }
}
