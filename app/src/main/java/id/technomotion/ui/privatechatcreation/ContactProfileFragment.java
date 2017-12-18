package id.technomotion.ui.privatechatcreation;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiscus.sdk.Qiscus;
import com.squareup.picasso.Picasso;

import id.technomotion.R;
import id.technomotion.model.Person;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactProfileFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    private static final String PERSON_KEY = "PERSON_KEY";
    TextView contactName, contactEmail;
    ImageView contactAvatar;
    RelativeLayout startChat;
    // TODO: Rename and change types of parameters
    private Person inputContact;


    public ContactProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param contact Parameter 1
     * @return A new instance of fragment ContactProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactProfileFragment newInstance(Person contact) {
        ContactProfileFragment fragment = new ContactProfileFragment();
        Bundle bundle = new Bundle();

        bundle.putSerializable(PERSON_KEY, contact);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            inputContact = (Person) getArguments().getSerializable(
                    PERSON_KEY);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_profile, container, false);
        contactName = (TextView) view.findViewById(R.id.contact_display_name);
        contactEmail = (TextView) view.findViewById(R.id.contact_user_email);
        contactName.setText(inputContact.getName());
        contactEmail.setText(inputContact.getEmail());
        contactAvatar = (ImageView) view.findViewById(R.id.contact_picture);
        String avatarUrl = inputContact.getAvatarUrl();
        Picasso.with(this.contactAvatar.getContext()).load(avatarUrl).fit().centerCrop().into(contactAvatar);

        startChat = (RelativeLayout) view.findViewById(R.id.startChat);

        startChat.setOnClickListener(this);


        return view;
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
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });


    }
}
