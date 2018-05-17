package com.qiscus.chat.ngobrel.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qiscus.chat.ngobrel.NgobrelApp;
import com.qiscus.chat.ngobrel.R;
import com.qiscus.chat.ngobrel.ui.homepagetab.HomePageTabActivity;

public class LoginActivity extends AppCompatActivity implements LoginPresenter.View {
    private EditText name;
    private EditText email;
    private EditText password;
    private Button loginButton;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name = findViewById(R.id.display_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait!");

        LoginPresenter loginPresenter = new LoginPresenter(this,
                NgobrelApp.getInstance().getComponent().getUserRepository());

        loginButton.setOnClickListener(v -> {
            if (TextUtils.isEmpty(name.getText().toString())) {
                name.setError("Must not empty!");
            } else if (TextUtils.isEmpty(email.getText().toString())) {
                email.setError("Must not empty!");
            } else if (TextUtils.isEmpty(password.getText().toString())) {
                password.setError("Must not empty!");
            } else {
                loginPresenter.login(
                        name.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString()
                );
            }
        });
    }

    @Override
    public void showHomePage() {
        startActivity(new Intent(this, HomePageTabActivity.class));
    }

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void dismissLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}

