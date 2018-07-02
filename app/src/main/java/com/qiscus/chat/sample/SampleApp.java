package com.qiscus.chat.sample;

import android.app.Application;
import android.content.Context;

import com.qiscus.chat.AppComponent;
import com.qiscus.chat.sample.ui.homepagetab.HomePageTabActivity;
import com.qiscus.chat.sample.util.ChatRoomNavigator;
import com.qiscus.sdk.Qiscus;
import com.qiscus.sdk.data.model.NotificationClickListener;
import com.qiscus.sdk.data.model.QiscusComment;
import com.qiscus.sdk.event.QiscusCommentReceivedEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import com.qiscus.chat.sample.util.Configuration;
import com.qiscus.chat.sample.util.RealTimeChatroomHandler;

import io.realm.Realm;

/**
 * Created by omayib on 18/09/17.
 */

public class SampleApp extends Application {
    private RealTimeChatroomHandler chatroomHandler;
    private static SampleApp INSTANCE;
    private AppComponent component;
    public static SampleApp getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        component = new AppComponent(this);
        Qiscus.init(this, Configuration.QISCUS_APP_ID);
        chatroomHandler = new RealTimeChatroomHandler();

        Qiscus.getChatConfig()
                .setStatusBarColor(R.color.colorPrimaryDark)
                .setAppBarColor(R.color.colorPrimary)
                .setLeftBubbleColor(R.color.emojiSafeYellow)
                .setRightBubbleColor(R.color.colorPrimary)
                .setRightBubbleTextColor(R.color.qiscus_white)
                .setRightBubbleTimeColor(R.color.qiscus_white)
                .setReadIconColor(R.color.colorAccent)
                .setEmptyRoomImageResource((R.drawable.ic_room_empty))
                .setNotificationBigIcon(R.drawable.ic_logo_qiscus)
                .setNotificationClickListener(new NotificationClickListener() {
                    @Override
                    public void onClick(Context context, QiscusComment qiscusComment) {
                        ChatRoomNavigator
                                .openChatQiscusCommentRoom(context, qiscusComment)
                                .withParentClass(HomePageTabActivity.class)
                                .start();
                    }
                })
                .setEnableAddLocation(true)
                .setEmptyRoomTitleColor(R.color.orangeIcon)
                .setAccentColor(R.color.colorAccent)
                .getDeleteCommentConfig().setEnableDeleteComment(true);

        Realm.init(this);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe
    public void onReceivedComment(QiscusCommentReceivedEvent event) {
        chatroomHandler.updateChatrooms(event.getQiscusComment());
    }

    public RealTimeChatroomHandler getChatroomHandler() {
        return chatroomHandler;
    }

    public AppComponent getComponent() {
        return component;
    }
}
