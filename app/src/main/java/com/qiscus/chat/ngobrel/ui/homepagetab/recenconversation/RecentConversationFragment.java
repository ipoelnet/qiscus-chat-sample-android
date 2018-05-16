package com.qiscus.chat.ngobrel.ui.homepagetab.recenconversation;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.qiscus.chat.ngobrel.R;
import com.qiscus.chat.ngobrel.SampleApp;
import com.qiscus.chat.ngobrel.model.Room;
import com.qiscus.chat.ngobrel.ui.privatechatcreation.PrivateChatCreationActivity;
import com.qiscus.chat.ngobrel.util.RealTimeChatroomHandler;
import com.qiscus.sdk.Qiscus;
import com.qiscus.sdk.data.model.QiscusChatRoom;
import com.qiscus.sdk.data.model.QiscusComment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by asyrof on 17/11/17.
 */
public class RecentConversationFragment extends Fragment implements RealTimeChatroomHandler.Listener {
    private static final String TAG = "RecentConversationsActi";
    private FloatingActionButton fabCreateNewConversation;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Room> rooms = new ArrayList<>();
    private RecentConversationFragmentRecyclerAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout emptyRoomView;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat dateFormatToday = new SimpleDateFormat("hh:mm a");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recent_conversation_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View v = getView();
        recyclerView = v.findViewById(R.id.recyclerRecentConversation);
        linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);

        fabCreateNewConversation = v.findViewById(R.id.buttonCreateNewConversation);
        fabCreateNewConversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PrivateChatCreationActivity.class);
                startActivity(intent);
            }
        });
        emptyRoomView = v.findViewById(R.id.empty_room_view);
        adapter = new RecentConversationFragmentRecyclerAdapter(rooms);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadConversation();
    }

    private void loadConversation() {
        Qiscus.getDataStore()
                .getObservableChatRooms(1000)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<QiscusChatRoom>>() {
                    @Override
                    public void call(List<QiscusChatRoom> qiscusChatRooms) {
                        if (!qiscusChatRooms.isEmpty()) {
                            emptyRoomView.setVisibility(View.GONE);
                        }

                        for (QiscusChatRoom chatRoom : qiscusChatRooms) {
                            Room room = new Room(chatRoom.getId(), chatRoom.getName());
                            room.setLatestMessage(chatRoom.getLastComment().getMessage());
                            room.setAvatar(chatRoom.getAvatarUrl());
                            Date messageDate = chatRoom.getLastComment().getTime();
                            String finalDateFormat;
                            if (DateUtils.isToday(messageDate.getTime())) {
                                finalDateFormat = dateFormatToday.format(chatRoom.getLastComment().getTime());
                            } else {
                                finalDateFormat = dateFormat.format(chatRoom.getLastComment().getTime());
                            }
                            room.setLastMessageTime(finalDateFormat);
                            room.setUnreadCounter(chatRoom.getUnreadCount());
                            if (!rooms.contains(room)) {
                                rooms.add(room);
                            } else {
                                rooms.set(rooms.indexOf(room), room);
                            }
                        }

                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    @Override
    public void onStart() {
        SampleApp.getInstance().getChatroomHandler().setListener(this);
        super.onStart();

    }

    @Override
    public void onPause() {
        super.onPause();
        SampleApp.getInstance().getChatroomHandler().removeListener();
    }

    @Override
    public void onReceiveComment(QiscusComment comment) {
        int roomId = (int) comment.getRoomId();
        boolean isNewRoom = true;

        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            if (room.getId() == roomId) {
                int unread = room.getUnreadCounter();
                room.setUnreadCounter(unread + 1);
                room.setLatestMessage(comment.getMessage());

                String finalDateFormat;
                if (DateUtils.isToday(comment.getTime().getTime())) {
                    finalDateFormat = dateFormatToday.format(comment.getTime());
                } else {
                    finalDateFormat = dateFormat.format(comment.getTime());
                }
                room.setLastMessageTime(finalDateFormat);

                rooms.remove(i);
                rooms.add(0, room);
                adapter.notifyDataSetChanged();

                isNewRoom = false;
                break;
            }
        }

        if (isNewRoom) {
            Room room = new Room(roomId, comment.getRoomName());
            room.setLatestMessage(comment.getMessage());
            room.setAvatar(comment.getRoomAvatar());
            Date messageDate = comment.getTime();
            String finalDateFormat;
            if (DateUtils.isToday(messageDate.getTime())) {
                finalDateFormat = dateFormatToday.format(messageDate);
            } else {
                finalDateFormat = dateFormat.format(messageDate);
            }
            room.setLastMessageTime(finalDateFormat);
            room.setUnreadCounter(1);
            rooms.add(0, room);
            adapter.notifyDataSetChanged();
        }
    }
}