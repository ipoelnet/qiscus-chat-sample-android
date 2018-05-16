package com.qiscus.chat.ngobrel.model;

import com.qiscus.sdk.util.QiscusNumberUtil;

/**
 * Created by omayib on 30/10/17.
 */
public class Room {
    private final long id;
    private final String name;
    private String avatar = "";
    private int unreadCounter = 0;
    private String latestMessage = "";
    private String lastMessageTime = "";

    public Room(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(String latestMessage) {
        this.latestMessage = latestMessage;
    }


    public void setAvatar(String image) {
        this.avatar = image;
    }

    public String getAvatar() {
        return avatar;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getUnreadCounter() {
        return unreadCounter;
    }

    public void setUnreadCounter(int unreadCounter) {
        this.unreadCounter = unreadCounter;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Room room = (Room) o;

        return id == room.id;
    }

    @Override
    public int hashCode() {
        return QiscusNumberUtil.convertToInt(id);
    }
}
