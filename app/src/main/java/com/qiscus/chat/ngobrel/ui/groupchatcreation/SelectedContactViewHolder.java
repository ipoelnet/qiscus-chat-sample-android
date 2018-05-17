package com.qiscus.chat.ngobrel.ui.groupchatcreation;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiscus.chat.ngobrel.R;
import com.qiscus.chat.ngobrel.ui.common.OnItemClickListener;
import com.qiscus.nirmana.Nirmana;

/**
 * Created on : May 17, 2018
 * Author     : zetbaitsu
 * Name       : Zetra
 * GitHub     : https://github.com/zetbaitsu
 */
public class SelectedContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView itemName;
    private ImageView picture;

    private OnItemClickListener onItemClickListener;

    public SelectedContactViewHolder(View itemView, OnItemClickListener onItemClickListener) {
        super(itemView);
        this.onItemClickListener = onItemClickListener;
        itemView.setOnClickListener(this);

        itemName = itemView.findViewById(R.id.textViewName);
        picture = itemView.findViewById(R.id.imageViewProfile);
    }

    public void bind(SelectableUser selectableUser) {
        Nirmana.getInstance().get()
                .load(selectableUser.getUser().getAvatarUrl())
                .placeholder(R.drawable.ic_qiscus_avatar)
                .error(R.drawable.ic_qiscus_avatar)
                .dontAnimate()
                .into(picture);

        itemName.setText(selectableUser.getUser().getName());
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }
}
