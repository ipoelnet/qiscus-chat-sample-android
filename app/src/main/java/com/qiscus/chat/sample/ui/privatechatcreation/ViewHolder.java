package com.qiscus.chat.sample.ui.privatechatcreation;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import com.qiscus.chat.sample.R;
import com.qiscus.chat.sample.model.User;


/**
 * Created by omayib on 18/09/17.
 */

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final String TAG = "ViewHolder";
    private TextView itemName;
    private TextView itemJob;
    private com.qiscus.sdk.ui.view.QiscusCircularImageView picture;
    private User selectedContact;
    private CheckBox checkBox;
    private final OnContactClickedListener listener;

    public ViewHolder(View itemView, OnContactClickedListener listener) {
        super(itemView);
        checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
        itemName = (TextView) itemView.findViewById(R.id.textViewName);
        itemJob = (TextView) itemView.findViewById(R.id.textViewJob);
        picture = (com.qiscus.sdk.ui.view.QiscusCircularImageView) itemView.findViewById(R.id.imageViewProfile);
        this.listener = listener;

        itemView.setOnClickListener(this);
        checkBox.setVisibility(View.GONE);
    }

    public void bindAlumni(User user){
        this.selectedContact = user;
        this.itemName.setText(user.getName());
        this.itemJob.setText(user.getJob());
        Context context = this.picture.getContext();
        if (user.getEmail().equals(PrivateChatCreationActivity.GROUP_CHAT_ID)) {
            Picasso.with(context).load(R.drawable.ic_create_group).fit().centerCrop().into(picture);
            picture.setColorFilter(ContextCompat.getColor(context, R.color.orangeIcon), PorterDuff.Mode.MULTIPLY);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                itemName.setTextAppearance(R.style.TextAppearance_AppCompat_Large);
            }
        }
        else if (user.getEmail().equals(PrivateChatCreationActivity.STRANGER_CHAT_ID))
        {
            Picasso.with(context).load(R.drawable.ic_stranger).fit().centerCrop().into(picture);
            picture.setColorFilter(ContextCompat.getColor(context, R.color.orangeIcon), PorterDuff.Mode.MULTIPLY);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                itemName.setTextAppearance(R.style.TextAppearance_AppCompat_Large);
            }
        }
        else {
            String avatarUrl = user.getAvatarUrl();
            Picasso.with(this.picture.getContext()).load(avatarUrl).fit().centerCrop().into(picture);
        }
    }

    @Override
    public void onClick(final View v) {
        this.listener.onContactClicked(this.selectedContact);
    }

    public interface OnContactClickedListener{
        public void onContactClicked(User user);
    }
}
