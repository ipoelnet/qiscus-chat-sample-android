package com.qiscus.chat.sample.ui.homepagetab;

/**
 * Created by asyrof on 17/11/17.
 */


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiscus.chat.sample.util.ChatRoomNavigator;
import com.qiscus.chat.sample.util.ChatRoomProvider;
import com.qiscus.chat.sample.util.Configuration;
import com.qiscus.sdk.data.model.QiscusChatRoom;
import com.qiscus.sdk.ui.view.QiscusCircularImageView;
import com.squareup.picasso.Picasso;


import com.qiscus.chat.sample.R;
import com.qiscus.chat.sample.model.Room;

/**
 * Created by omayib on 30/10/17.
 */

public class RecentConversationFragmentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final String TAG = "ViewHolder";
    private final ImageView picturePlatformType;
    private TextView itemName;
    private TextView itemJob;
    private com.qiscus.sdk.ui.view.QiscusCircularImageView picture;
    private Room selectedRoom;
    private TextView lastMessageTime;
    private TextView unreadCounter;
    private FrameLayout unreadFrame;

    public RecentConversationFragmentHolder(View itemView) {
        super(itemView);
        itemName = (TextView) itemView.findViewById(R.id.textViewRoomName);
        itemJob = (TextView) itemView.findViewById(R.id.textViewJob);
        picture = (QiscusCircularImageView) itemView.findViewById(R.id.imageViewRoomAvatar);
        lastMessageTime = (TextView) itemView.findViewById(R.id.textViewRoomTime);
        unreadCounter = (TextView) itemView.findViewById(R.id.unreadCounterView);
        unreadFrame = (FrameLayout) itemView.findViewById(R.id.unreadCounterFrame);
        picturePlatformType = (QiscusCircularImageView) itemView.findViewById(R.id.img_platfrom_type);
        itemView.setOnClickListener(this);
    }

    public void bindRecentConversation(Room room) {
        this.selectedRoom = room;
        this.itemName.setText(room.getName());
        String latestConversation = room.getLatestConversation();
        if (latestConversation.contains("[file]")) {
            latestConversation = "File Attachment";
        }
        this.itemJob.setText(latestConversation);

        this.lastMessageTime.setText(room.getLastMessageTime());
        int unread = room.getUnreadCounter();
        if (unread > 0) {
            this.unreadFrame.setVisibility(View.VISIBLE);
            this.unreadCounter.setText(String.valueOf(unread));
        } else {
            this.unreadFrame.setVisibility(View.GONE);
        }
        String imagePath = "http://lorempixel.com/200/200/people/" + room.getName();
        imagePath = room.getOnlineImage();
        Picasso.with(this.picture.getContext()).load(imagePath).fit().centerCrop().into(picture);
        loadImageBasedOnPlatformType(imagePath);
    }

    private void loadImageBasedOnPlatformType(String imagePath) {
        if (imagePath.contains(Configuration.LINE_TYPE)) {
            picturePlatformType.setVisibility(View.VISIBLE);
            picturePlatformType.setImageResource(R.drawable.ic_line);
        } else if (imagePath.contains(Configuration.FB_TYPE)) {
            picturePlatformType.setVisibility(View.VISIBLE);
            picturePlatformType.setImageResource(R.drawable.ic_fb);
        } else {
            picturePlatformType.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(final View v) {

        final Activity currentActivity = (HomePageTabActivity) v.getContext();

        ChatRoomProvider.getChatRoom(selectedRoom.getId(), new ChatRoomProvider.Callback<QiscusChatRoom>() {
            @Override
            public void onCall(QiscusChatRoom qiscusChatRoom) {
                ChatRoomNavigator
                        .openChatRoom(currentActivity, qiscusChatRoom)
                        .start();
            }
        }, new ChatRoomProvider.Callback<Throwable>() {
            @Override
            public void onCall(Throwable call) {
                call.printStackTrace();
            }
        });
    }
}

