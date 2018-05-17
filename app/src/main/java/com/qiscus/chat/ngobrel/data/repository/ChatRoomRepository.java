package com.qiscus.chat.ngobrel.data.repository;

import com.qiscus.chat.ngobrel.data.model.User;
import com.qiscus.chat.ngobrel.util.Action;
import com.qiscus.sdk.data.model.QiscusChatRoom;

import java.util.List;

/**
 * Created on : January 31, 2018
 * Author     : zetbaitsu
 * Name       : Zetra
 * GitHub     : https://github.com/zetbaitsu
 */
public interface ChatRoomRepository {
    void getChatRooms(Action<List<QiscusChatRoom>> onSuccess, Action<Throwable> onError);

    void createChatRoom(User user, Action<QiscusChatRoom> onSuccess, Action<Throwable> onError);

    void createGroupChatRoom(String name, List<User> members, Action<QiscusChatRoom> onSuccess, Action<Throwable> onError);

    void addMember(long roomId, User user, Action<Void> onSuccess, Action<Throwable> onError);

    void removeMember(long roomId, User user, Action<Void> onSuccess, Action<Throwable> onError);

    void updateGroupChatRoomName(long roomId, String name, Action<QiscusChatRoom> onSuccess, Action<Throwable> onError);
}
