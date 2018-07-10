package com.qiscus.chat.sample.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiscus.chat.sample.SampleApp;
import com.qiscus.chat.sample.R;
import com.qiscus.chat.sample.data.model.User;
import com.qiscus.chat.sample.ui.login.LoginActivity;
import com.qiscus.nirmana.Nirmana;

public class ProfileActivity extends AppCompatActivity implements ProfilePresenter.View {

    private TextView displayName;
    private TextView userId;
    private ImageView picture;

    private ProfilePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        picture = findViewById(R.id.single_avatar);
        displayName = findViewById(R.id.profile_display_name);
        userId = findViewById(R.id.profile_user_id);

        presenter = new ProfilePresenter(this, SampleApp.getInstance().getComponent().getUserRepository());
        presenter.loadUser();

        findViewById(R.id.logout_text).setOnClickListener(view -> presenter.logout());
    }

    @Override
    public void showUser(User user) {
        Nirmana.getInstance().get().load(user.getAvatarUrl()).into(picture);
        displayName.setText(user.getName());
        userId.setText(user.getId());
    }

    @Override
    public void showLoginPage() {
        startActivity(
                new Intent(ProfileActivity.this, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        );
    }
}
