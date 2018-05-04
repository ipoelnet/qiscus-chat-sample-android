package com.qiscus.chat.ngobrel.ui.homepagetab;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.qiscus.chat.ngobrel.R;
import com.qiscus.chat.ngobrel.model.Room;
import com.qiscus.chat.ngobrel.util.ChatRoomNavigator;
import com.qiscus.chat.ngobrel.util.ChatRoomProvider;
import com.qiscus.nirmana.Nirmana;
import com.qiscus.sdk.data.model.QiscusChatRoom;
import com.qiscus.sdk.ui.view.QiscusCircularImageView;

/**
 * Created by omayib on 30/10/17.
 */
public class RecentConversationFragmentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final String TAG = "ViewHolder";
    private TextView itemName;
    private TextView itemJob;
    private com.qiscus.sdk.ui.view.QiscusCircularImageView picture;
    private Room selectedRoom;
    private TextView lastMessageTime;
    private TextView unreadCounter;
    private FrameLayout unreadFrame;

    public RecentConversationFragmentHolder(View itemView) {
        super(itemView);
        itemName = itemView.findViewById(R.id.textViewRoomName);
        itemJob = itemView.findViewById(R.id.textViewJob);
        picture = itemView.findViewById(R.id.imageViewRoomAvatar);
        lastMessageTime = itemView.findViewById(R.id.textViewRoomTime);
        unreadCounter = itemView.findViewById(R.id.unreadCounterView);
        unreadFrame = itemView.findViewById(R.id.unreadCounterFrame);
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
        String imagePath = room.getOnlineImage();
        Nirmana.getInstance().get().load(imagePath).centerCrop().into(picture);
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

