package com.qiscus.chat.ngobrel.ui.privatechatcreation;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.qiscus.chat.ngobrel.NgobrelApp;
import com.qiscus.chat.ngobrel.R;
import com.qiscus.chat.ngobrel.data.model.User;
import com.qiscus.chat.ngobrel.data.repository.ChatRoomRepository;
import com.qiscus.sdk.ui.QiscusChatActivity;

/**
 * Created by omayib on 05/11/17.
 */
public class ChatWithStrangerDialogFragment extends DialogFragment {
    private EditText editText;

    private ChatRoomRepository chatRoomRepository;

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_stranger_name, container, false);
        getDialog().setTitle("Stranger name");
        editText = rootView.findViewById(R.id.editText);

        chatRoomRepository = NgobrelApp.getInstance().getComponent().getChatRoomRepository();

        Button buttonOk = rootView.findViewById(R.id.confirm_button);
        Button buttonCancel = rootView.findViewById(R.id.cancel_button);

        buttonCancel.setOnClickListener(view -> getDialog().dismiss());

        buttonOk.setOnClickListener(view -> {
            if (!editText.getText().toString().isEmpty()) {
                chatRoomRepository.createChatRoom(new User(editText.getText().toString(), "", ""),
                        qiscusChatRoom -> {
                            startActivity(QiscusChatActivity.generateIntent(getActivity(), qiscusChatRoom));
                            dismiss();
                        },
                        Throwable::printStackTrace);
                dismiss();
            }
        });
        return rootView;
    }
}
