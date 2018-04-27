package com.qiscus.chat.sample.ui.homepagetab;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.qiscus.nirmana.Nirmana;
import com.qiscus.sdk.Qiscus;
import com.qiscus.sdk.data.local.QiscusCacheManager;
import com.qiscus.sdk.data.model.QiscusAccount;
import com.qiscus.sdk.data.remote.QiscusApi;
import com.qiscus.sdk.util.QiscusImageUtil;

import java.io.File;
import java.io.IOException;

import com.qiscus.chat.sample.R;
import com.qiscus.chat.sample.ui.login.LoginActivity;
import com.qiscus.chat.sample.util.FileUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView displayName,userId,logoutText;
    private QiscusAccount qiscusAccount;
    private ImageView logoutButton;
    private final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1000;
    private final int GET_GALLERY_IMAGE_REQUEST_CODE = 1001;
    com.qiscus.sdk.ui.view.QiscusCircularImageView uploadIcon;
    private com.qiscus.sdk.ui.view.QiscusCircularImageView picture;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        picture = (com.qiscus.sdk.ui.view.QiscusCircularImageView) findViewById(R.id.single_avatar);
        displayName = (TextView) findViewById(R.id.profile_display_name);
        userId = (TextView) findViewById(R.id.profile_user_id);
        mProgressView = findViewById(R.id.upload_progress);
        qiscusAccount = Qiscus.getQiscusAccount();
        displayName.setText(qiscusAccount.getUsername());
        this.setTitle("Profile");
        userId.setText(qiscusAccount.getEmail());
        logoutText =(TextView) findViewById(R.id.logout_text);
        logoutButton = (ImageView) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(this);
        logoutText.setOnClickListener(this);
        uploadIcon = (com.qiscus.sdk.ui.view.QiscusCircularImageView) findViewById(R.id.upload_icon);
        uploadIcon.setOnClickListener(this);
        String avatarUrl = qiscusAccount.getAvatar();
        Nirmana.getInstance().get().load(avatarUrl).centerCrop().into(picture);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.upload_icon:
                PopupMenu popup = new PopupMenu(this, uploadIcon);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.menu_upload, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.choose_picture) {
                            openGallery();
                        } else if (item.getItemId() == R.id.take_picture) {
                            openCamera();
                        }

                        return true;
                    }
                });

                popup.show();
                break;

            /*case R.id.nextFloatingButton:
                proceedCreateGroup();
                break;*/

            default:
                logout();
                break;

        }

    }

    private void logout() {
        Qiscus.clearUser();
        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = QiscusImageUtil.createImageFile();
            } catch (IOException ex) {
                showError("Failed to write temporary picture!","Unexpected Error");
            }

            if (photoFile != null) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                } else {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            FileProvider.getUriForFile(this, Qiscus.getProviderAuthorities(), photoFile));
                }
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(intent, GET_GALLERY_IMAGE_REQUEST_CODE);
        }
    }

    private void showError(String warning,String warningType) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle(warningType)
                .setMessage(warning)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                String imagePath = FileUtil.getRealPathFromUri(this, selectedImage);
                processImage(imagePath);
            }
        } else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri processedPhoto = (Uri.parse(QiscusCacheManager.getInstance().getLastImagePath()));
                String imagePath = FileUtil.getRealPathFromUri(this, processedPhoto);
                processImage(imagePath);
            }

        }
    }

    private void processImage(String imagePath) {
        File theFile = new File(imagePath);
        showProgress(true);
        sendFile(theFile, new ProfileActivity.Callback() {
            @Override
            public void onSuccessGetUri(final String uri) {
                Qiscus.updateUser(qiscusAccount.getUsername(), uri, new Qiscus.SetUserListener() {
                    @Override
                    public void onSuccess(QiscusAccount qiscusAccount) {
                        Nirmana.getInstance().get().load(uri).centerCrop().into(picture);
                        showProgress(false);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showProgress(false);
                        showError("An error occured please try again","Network Error");
                    }
                });

            }

            @Override
            public void onFailiedGetUri(Throwable throwable) {
                showProgress(false);
                showError("An error occured please try again","Network Error");
            }
        });
    }

    public void sendFile(File file, final ProfileActivity.Callback callback) {
        //TODO you can not use Qiscus uploader anymore, because the file encrypted
        /*QiscusApi.getInstance().uploadFile(file, new QiscusApi.ProgressListener() {
            @Override
            public void onProgress(long total) {

            }
        })
                .map(new Func1<Uri, String>() {
                    @Override
                    public String call(Uri uri) {
                        return uri.toString();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String uri) {
                        callback.onSuccessGetUri(uri);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        callback.onFailiedGetUri(throwable);
                    }
                });*/
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);


            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
    public interface Callback {
        void onSuccessGetUri(String uri);

        void onFailiedGetUri(Throwable throwable);
    }
}
