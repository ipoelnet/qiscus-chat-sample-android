package com.qiscus.chat.ngobrel.ui.privatechatcreation;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiscus.chat.ngobrel.R;
import com.qiscus.chat.ngobrel.model.Person;
import com.qiscus.nirmana.Nirmana;
import com.qiscus.sdk.Qiscus;

/**
 * Created by asyrof on 18/12/17.
 */
public class ContactDialogProfileFragment extends DialogFragment implements View.OnClickListener {
    private static final String PERSON_KEY = "PERSON_KEY";

    private Person inputContact;


    public static ContactDialogProfileFragment newInstance(Person user) {
        ContactDialogProfileFragment fragment = new ContactDialogProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PERSON_KEY, user);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_profile, container, false);
        TextView contactName = rootView.findViewById(R.id.contact_display_name);
        TextView contactEmail = rootView.findViewById(R.id.contact_user_email);

        inputContact = (Person) getArguments().getSerializable(PERSON_KEY);
        if (inputContact == null) {
            throw new RuntimeException("Please provide a person");
        }

        contactName.setText(inputContact.getName());
        contactEmail.setText(inputContact.getEmail());
        ImageView contactAvatar = rootView.findViewById(R.id.contact_picture);
        String avatarUrl = inputContact.getAvatarUrl();
        Nirmana.getInstance().get().load(avatarUrl).centerCrop().into(contactAvatar);

        rootView.findViewById(R.id.startChat).setOnClickListener(this);


        return rootView;
    }

    @Override
    public void onClick(View view) {
        startSingleChat();
    }

    private void startSingleChat() {
        final String userEmail = inputContact.getEmail();
        Qiscus.buildChatWith(userEmail)
                .build(getActivity().getBaseContext(), new Qiscus.ChatActivityBuilderListener() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivity(intent);
                        getDialog().dismiss();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });


    }

}