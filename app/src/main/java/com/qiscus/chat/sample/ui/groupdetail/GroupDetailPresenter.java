package com.qiscus.chat.sample.ui.groupdetail;

import com.qiscus.chat.sample.data.model.User;
import com.qiscus.chat.sample.data.repository.ChatRoomRepository;

/**
 * Created on : May 16, 2018
 * Author     : zetbaitsu
 * Name       : Zetra
 * GitHub     : https://github.com/zetbaitsu
 */
public class GroupDetailPresenter {
    private View view;
    private ChatRoomRepository chatRoomRepository;

    public GroupDetailPresenter(View view, ChatRoomRepository chatRoomRepository) {
        this.view = view;
        this.chatRoomRepository = chatRoomRepository;
    }

    public void removeMember(long roomId, User user) {
        view.showLoading();
        chatRoomRepository.removeMember(roomId, user,
                aVoid -> {
                    view.dismissLoading();
                    view.onMemberRemoved(user);
                },
                throwable -> {
                    view.dismissLoading();
                    view.showErrorMessage(throwable.getMessage());
                });
    }

    public interface View {
        void onMemberRemoved(User user);

        void showLoading();

        void dismissLoading();

        void showErrorMessage(String errorMessage);
    }
}
