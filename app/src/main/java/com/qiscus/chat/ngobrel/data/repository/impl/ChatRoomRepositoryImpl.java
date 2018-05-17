package com.qiscus.chat.ngobrel.data.repository.impl;

import com.qiscus.chat.ngobrel.data.model.User;
import com.qiscus.chat.ngobrel.data.repository.ChatRoomRepository;
import com.qiscus.chat.ngobrel.data.repository.remote.QiscusRestApi;
import com.qiscus.chat.ngobrel.util.Action;
import com.qiscus.chat.ngobrel.util.AvatarUtil;
import com.qiscus.sdk.Qiscus;
import com.qiscus.sdk.data.model.QiscusChatRoom;
import com.qiscus.sdk.data.remote.QiscusApi;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created on : January 31, 2018
 * Author     : zetbaitsu
 * Name       : Zetra
 * GitHub     : https://github.com/zetbaitsu
 */
public class ChatRoomRepositoryImpl implements ChatRoomRepository {

    @Override
    public void getChatRooms(Action<List<QiscusChatRoom>> onSuccess, Action<Throwable> onError) {
        Observable.fromCallable(() -> Qiscus.getDataStore().getChatRooms(100))
                .flatMap(Observable::from)
                .filter(chatRoom -> chatRoom.getLastComment() != null)
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess::call, onError::call);

        Qiscus.synchronizeData();
    }

    @Override
    public void createChatRoom(User user, Action<QiscusChatRoom> onSuccess, Action<Throwable> onError) {
        QiscusChatRoom savedChatRoom = Qiscus.getDataStore().getChatRoom(user.getId());
        if (savedChatRoom != null) {
            onSuccess.call(savedChatRoom);
            return;
        }

        QiscusApi.getInstance()
                .getChatRoom(user.getId(), null, null)
                .doOnNext(chatRoom -> Qiscus.getDataStore().addOrUpdate(chatRoom))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess::call, onError::call);
    }

    @Override
    public void createGroupChatRoom(String name, List<User> members, Action<QiscusChatRoom> onSuccess, Action<Throwable> onError) {
        List<String> ids = new ArrayList<>();
        for (User member : members) {
            ids.add(member.getId());
        }

        QiscusApi.getInstance()
                .createGroupChatRoom(name, ids, AvatarUtil.generateAvatar(name), null)
                .doOnNext(chatRoom -> Qiscus.getDataStore().addOrUpdate(chatRoom))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess::call, onError::call);
    }

    @Override
    public void addMember(long roomId, User user, Action<Void> onSuccess, Action<Throwable> onError) {
        QiscusRestApi.INSTANCE.addMember(roomId, user.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jsonObject -> onSuccess.call(null), onError::call);

    }

    @Override
    public void removeMember(long roomId, User user, Action<Void> onSuccess, Action<Throwable> onError) {
        QiscusRestApi.INSTANCE.removeMember(roomId, user.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jsonObject -> onSuccess.call(null), onError::call);
    }

    @Override
    public void updateGroupChatRoomName(long roomId, String name, Action<QiscusChatRoom> onSuccess, Action<Throwable> onError) {
        Observable.fromCallable(() -> Qiscus.getDataStore().getChatRoom(roomId))
                .flatMap(qiscusChatRoom ->
                        QiscusApi.getInstance().updateChatRoom(roomId, name, qiscusChatRoom.getAvatarUrl(), qiscusChatRoom.getOptions()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess::call, onError::call);
    }
}
