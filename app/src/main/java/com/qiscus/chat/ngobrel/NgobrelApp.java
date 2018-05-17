package com.qiscus.chat.ngobrel;

import android.app.Application;

import com.qiscus.chat.ngobrel.ui.homepagetab.HomePageTabActivity;
import com.qiscus.chat.ngobrel.util.ChatRoomNavigator;
import com.qiscus.chat.ngobrel.util.RealTimeChatroomHandler;
import com.qiscus.sdk.Qiscus;
import com.qiscus.sdk.event.QiscusCommentReceivedEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.realm.Realm;

/**
 * Created by omayib on 18/09/17.
 */
public class NgobrelApp extends Application {
    private RealTimeChatroomHandler chatroomHandler;
    private static NgobrelApp INSTANCE;

    private AppComponent component;

    public static NgobrelApp getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        component = new AppComponent(this);
        Qiscus.setEnableLog(BuildConfig.DEBUG);
        Qiscus.init(this, "ngobrel-bwk4c0g9fiwzg");
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
                .setEnableFcmPushNotification(true)
                .setNotificationBigIcon(R.drawable.ic_logo_qiscus)
                .setNotificationSmallIcon(R.drawable.ic_logo_qiscus)
                .setNotificationClickListener((context, qiscusComment) -> ChatRoomNavigator
                        .openChatQiscusCommentRoom(context, qiscusComment)
                        .withParentClass(HomePageTabActivity.class)
                        .start())
                .setEnableAddLocation(true)
                .setEmptyRoomTitleColor(R.color.orangeIcon)
                .setAccentColor(R.color.colorAccent)
                .setEnableEndToEndEncryption(true)
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
