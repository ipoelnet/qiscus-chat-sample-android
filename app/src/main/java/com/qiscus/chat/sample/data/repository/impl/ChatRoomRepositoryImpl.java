package com.qiscus.chat.sample.data.repository.impl;

import com.qiscus.chat.sample.data.model.User;
import com.qiscus.chat.sample.data.repository.ChatRoomRepository;
import com.qiscus.chat.sample.util.Action;
import com.qiscus.chat.sample.util.AvatarUtil;
import com.qiscus.sdk.Qiscus;
import com.qiscus.sdk.data.model.QiscusChatRoom;
import com.qiscus.sdk.data.model.QiscusComment;
import com.qiscus.sdk.data.remote.QiscusApi;

import java.util.ArrayList;
import java.util.Arrays;
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
    public void getChatRoom(long roomId, Action<QiscusChatRoom> onSuccess, Action<Throwable> onError) {
        QiscusChatRoom savedChatRoom = Qiscus.getDataStore().getChatRoom(roomId);
        if (savedChatRoom != null) {
            onSuccess.call(savedChatRoom);
            return;
        }

        QiscusApi.getInstance()
                .getChatRoom(roomId)
                .doOnNext(qiscusChatRoom -> {
                    QiscusComment lastComment = qiscusChatRoom.getLastComment();
                    if (lastComment != null) {
                        qiscusChatRoom.setLastComment(null);
                    }
                })
                .doOnNext(chatRoom -> Qiscus.getDataStore().addOrUpdate(chatRoom))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess::call, onError::call);
    }

    @Override
    public void getChatRooms(Action<List<QiscusChatRoom>> onSuccess, Action<Throwable> onError) {

        Observable<List<QiscusChatRoom>> api = QiscusApi.getInstance().getChatRooms(0, 100, true);

        Observable<List<QiscusChatRoom>> local = Observable.just(Qiscus.getDataStore().getChatRooms(100));

        Observable.merge(api, local)
                .flatMap(Observable::from)
                .doOnNext(qiscusChatRoom -> Qiscus.getDataStore().addOrUpdate(qiscusChatRoom))
                .doOnNext(qiscusChatRoom ->
                        qiscusChatRoom.setLastComment(Qiscus.getDataStore().getLatestComment(qiscusChatRoom.getId())))
                .filter(qiscusChatRoom -> qiscusChatRoom.isGroup() || qiscusChatRoom.getLastComment() != null)
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess::call, onError::call);
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
        QiscusApi.getInstance()
                .addRoomMember(roomId, Arrays.asList(user.getId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jsonObject -> onSuccess.call(null), onError::call);

    }

    @Override
    public void removeMember(long roomId, User user, Action<Void> onSuccess, Action<Throwable> onError) {
        QiscusApi.getInstance()
                .removeRoomMember(roomId, Arrays.asList(user.getId()))
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
