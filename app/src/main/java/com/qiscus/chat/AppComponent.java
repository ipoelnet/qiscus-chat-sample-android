package com.qiscus.chat;

import android.content.Context;

import com.qiscus.chat.sample.repository.ChatRoomRepository;
import com.qiscus.chat.sample.repository.UserRepository;
import com.qiscus.chat.sample.repository.impl.ChatRoomRepositoryImpl;
import com.qiscus.chat.sample.repository.impl.UserRepositoryImpl;

public class AppComponent {
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    public AppComponent(Context context){
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
