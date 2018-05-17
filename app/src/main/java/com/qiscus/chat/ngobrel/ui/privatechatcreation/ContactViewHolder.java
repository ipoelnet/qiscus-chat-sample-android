package com.qiscus.chat.ngobrel.ui.privatechatcreation;

import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiscus.chat.ngobrel.R;
import com.qiscus.chat.ngobrel.data.model.User;
import com.qiscus.chat.ngobrel.ui.common.OnItemClickListener;
import com.qiscus.nirmana.Nirmana;

/**
 * Created on : May 17, 2018
 * Author     : zetbaitsu
 * Name       : Zetra
 * GitHub     : https://github.com/zetbaitsu
 */
public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView itemName;
    private ImageView picture;

    private OnItemClickListener onItemClickListener;

    public ContactViewHolder(View itemView, OnItemClickListener onItemClickListener) {
        super(itemView);
        this.onItemClickListener = onItemClickListener;
        itemView.setOnClickListener(this);

        itemName = itemView.findViewById(R.id.textViewName);
        picture = itemView.findViewById(R.id.imageViewProfile);
    }

    public void bind(User user) {
        itemName.setText(user.getName());
        if (user.getId().equals(PrivateChatCreationActivity.GROUP_CHAT_ID)) {
            Nirmana.getInstance().get().load(R.drawable.ic_create_group).centerCrop().into(picture);
            picture.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.orangeIcon), PorterDuff.Mode.MULTIPLY);
        } else if (user.getId().equals(PrivateChatCreationActivity.STRANGER_CHAT_ID)) {
            Nirmana.getInstance().get().load(R.drawable.ic_stranger).centerCrop().into(picture);
            picture.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.orangeIcon), PorterDuff.Mode.MULTIPLY);
        } else {
            Nirmana.getInstance().get()
                    .load(user.getAvatarUrl())
                    .placeholder(R.drawable.ic_qiscus_avatar)
                    .error(R.drawable.ic_qiscus_avatar)
                    .dontAnimate()
                    .into(picture);
            picture.setColorFilter(null);
        }
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }
}
