package com.qiscus.chat.ngobrel;

import android.content.Context;

import com.qiscus.chat.ngobrel.data.repository.ChatRoomRepository;
import com.qiscus.chat.ngobrel.data.repository.UserRepository;
import com.qiscus.chat.ngobrel.data.repository.impl.ChatRoomRepositoryImpl;
import com.qiscus.chat.ngobrel.data.repository.impl.UserRepositoryImpl;

/**
 * Created on : January 30, 2018
 * Author     : zetbaitsu
 * Name       : Zetra
 * GitHub     : https://github.com/zetbaitsu
 */
public class AppComponent {
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    AppComponent(Context context){
        userRepository = new UserRepositoryImpl(context);
        chatRoomRepository = new ChatRoomRepositoryImpl();
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public ChatRoomRepository getChatRoomRepository() {
        return chatRoomRepository;
    }
}
