package com.qiscus.chat.sample.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.qiscus.chat.sample.R;
import com.qiscus.chat.sample.SampleApp;
import com.qiscus.sdk.Qiscus;
import com.qiscus.sdk.data.local.QiscusCacheManager;
import com.qiscus.sdk.data.model.QiscusChatRoom;
import com.qiscus.sdk.data.model.QiscusComment;
import com.qiscus.sdk.data.model.QiscusNotificationBuilderInterceptor;
import com.qiscus.sdk.data.model.QiscusPushNotificationMessage;
import com.qiscus.sdk.data.model.QiscusRoomMember;
import com.qiscus.sdk.service.QiscusPushNotificationClickReceiver;
import com.qiscus.sdk.util.QiscusAndroidUtil;
import com.qiscus.sdk.util.QiscusSpannableBuilder;
import com.qiscus.sdk.util.QiscusTextUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by catur on 1/11/18.
 */

public class QiscusInterceptBuilder implements QiscusNotificationBuilderInterceptor {

    @Override
    public boolean intercept(final NotificationCompat.Builder notificationBuilder, final QiscusComment qiscusComment) {
        String messageText = "";
        int smallNotificationIcon = R.drawable.ic_logo_qiscus_small;
        QiscusChatRoom room = Qiscus.getDataStore().getChatRoom(qiscusComment.getRoomId());

        if (qiscusComment.getRoomAvatar().contains(Configuration.LINE_TYPE)) {
            smallNotificationIcon = R.drawable.ic_line;
        } else if (qiscusComment.getRoomAvatar().contains(Configuration.FB_TYPE)) {
            smallNotificationIcon = R.drawable.ic_fb;
        } else {
            smallNotificationIcon = R.drawable.ic_logo_qiscus_small;
        }

        PendingIntent pendingIntent;
        Intent openIntent = new Intent("com.qiscus.OPEN_COMMENT_PN");
        openIntent.putExtra("data", qiscusComment);
        pendingIntent = PendingIntent.getBroadcast(SampleApp.getInstance().getBaseContext(),
                (int)qiscusComment.getRoomId(), openIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Map<String, QiscusRoomMember> members = new HashMap<>();
        if (room != null) {
            for (QiscusRoomMember member : room.getMember()) {
                members.put(member.getEmail(), member);
            }
        }

        messageText += qiscusComment.isAttachment() ?
                QiscusTextUtil.getString(R.string.qiscus_send_attachment) :
                new QiscusSpannableBuilder(qiscusComment.getMessage(),
                        members).build().toString();

        Bitmap largeIcon = BitmapFactory.decodeResource(SampleApp.getInstance().getBaseContext().getResources(),
                Qiscus.getChatConfig().getNotificationBigIcon());

        notificationBuilder.setContentTitle(Qiscus.getChatConfig().getNotificationTitleHandler().getTitle(qiscusComment))
                .setContentIntent(pendingIntent)
                .setContentText(messageText)
                .setTicker(messageText)
                .setSmallIcon(smallNotificationIcon)
                .setLargeIcon(largeIcon)
                .setColor(ContextCompat.getColor(SampleApp.getInstance().getBaseContext(), Qiscus.getChatConfig().getInlineReplyColor()))
                .setGroupSummary(true)
                .setGroup("CHAT_NOTIF_" + qiscusComment.getRoomId())
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        List<QiscusPushNotificationMessage> cachedNotifItems = QiscusCacheManager.getInstance()
                .getMessageNotifItems(qiscusComment.getRoomId());
        List<QiscusPushNotificationMessage> notifItems = new ArrayList<>();

        if (cachedNotifItems != null) {
            for (QiscusPushNotificationMessage cachedNotifItem : cachedNotifItems) {
                if (!TextUtils.isEmpty(cachedNotifItem.getMessage())) {
                    notifItems.add(cachedNotifItem);
                }
            }
        }

        int notifSize = 5;
        if (notifItems.size() < notifSize) {
            notifSize = notifItems.size();
        }
        if (notifItems.size() > notifSize) {
            inboxStyle.addLine(".......");
        }
        int start = notifItems.size() - notifSize;
        for (int i = start; i < notifItems.size(); i++) {
            inboxStyle.addLine(notifItems.get(i).getMessage());
        }
        inboxStyle.setSummaryText(QiscusTextUtil.getString(com.qiscus.sdk.R.string.qiscus_notif_count, notifItems.size()));
        notificationBuilder.setStyle(inboxStyle);

        if (notifSize <= 3) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }

        QiscusAndroidUtil.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                try {
                    NotificationManagerCompat.from(SampleApp
                            .getInstance().getApplicationContext())
                            .notify((int)qiscusComment.getRoomId(), notificationBuilder.build());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        return false;
    }
}
