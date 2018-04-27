package com.qiscus.chat.sample.ui.privatechatcreation;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.SearchView;

import com.qiscus.sdk.Qiscus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.qiscus.chat.sample.R;
import com.qiscus.chat.sample.model.Person;
import com.qiscus.chat.sample.repository.AlumnusRepository;
import com.qiscus.chat.sample.repository.RepositoryTransactionListener;
import com.qiscus.chat.sample.ui.groupchatcreation.GroupChatCreationActivity;
import retrofit2.HttpException;

/**
 * Created by omayib on 18/09/17.
 */

public class PrivateChatCreationActivity extends AppCompatActivity implements RepositoryTransactionListener, ViewHolder.OnContactClickedListener, ChatWithStrangerDialogFragment.onStrangerNameInputtedListener {
    private static final String TAG = "PrivateChatCreationActivity";
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerAdapter mAdapter;
    private ArrayList<Person> alumnusList;
    private AlumnusRepository alumnusRepository;
    public static String GROUP_CHAT_ID="GROUP_CHAT_ID";
    public static String STRANGER_CHAT_ID="STRANGER_CHAT_ID";
    private Person groupChatHolder = new Person(GROUP_CHAT_ID,"Create Group Chat",GROUP_CHAT_ID,"placeholder");
    private Person strangerChatHolder = new Person(STRANGER_CHAT_ID,"Chat With Stranger",STRANGER_CHAT_ID,"placeholder");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumni_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //toolbar.setTitle("Select contact");
        //toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setVisibility(View.GONE);

        this.setTitle("Create New Chat");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewAlumni);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        alumnusRepository = new AlumnusRepository();
        alumnusRepository.setListener(this);
        ArrayList<Person> alumnusListTemp = alumnusRepository.getCachedData();
        alumnusList = new ArrayList<Person>(alumnusListTemp);
        //alumnusList = alumnusRepository.getCachedData();
        //TODO enable group chat when sdk support group e2e encryption
        //alumnusList.add(0,groupChatHolder);
        alumnusList.add(0,strangerChatHolder);

        mAdapter = new RecyclerAdapter(alumnusList, this);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //alumnusRepository.loadAll();
    }

    @Override
    public void onLoadAlumnusSucceeded(List<Person> alumnus) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });

    }


    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override

            public boolean onQueryTextSubmit(String query) {

                query = query.toString().toLowerCase();

                final ArrayList<Person> filteredList = new ArrayList<>();

                for (int i = 0; i < alumnusList.size(); i++) {

                    final String text = alumnusList.get(i).getEmail().toLowerCase();
                    if (text.contains(query)) {

                        filteredList.add(alumnusList.get(i));
                    }
                }
                mAdapter = new RecyclerAdapter(filteredList,PrivateChatCreationActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();  // data set changed

                searchView.clearFocus();



                return true;

            }



            @Override

            public boolean onQueryTextChange(String newText) {
                newText = newText.toString().toLowerCase();

                final ArrayList<Person> filteredList = new ArrayList<>();

                for (int i = 0; i < alumnusList.size(); i++) {

                    final String text = alumnusList.get(i).getName().toLowerCase();
                    if (text.contains(newText)) {

                        filteredList.add(alumnusList.get(i));
                    }
                }
                mAdapter = new RecyclerAdapter(filteredList,PrivateChatCreationActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();  // data set changed

                return true;

            }

        });

        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public void onContactClicked(final Person user) {
        if (user.getEmail().equals(GROUP_CHAT_ID))
        {
            startActivity(new Intent(this, GroupChatCreationActivity.class));
            finish();
        }

        else if (user.getEmail().equals(STRANGER_CHAT_ID)) {
            ChatWithStrangerDialogFragment dialogFragment = new ChatWithStrangerDialogFragment(this);
            dialogFragment.show(getFragmentManager(),"show_group_name");
        }
        else {

            ContactDialogProfileFragment dialogFragment = new ContactDialogProfileFragment(user);
            dialogFragment.show(getFragmentManager(),"ea");

            /*new AlertDialog.Builder(this)
                    .setTitle("Confirmation")
                    .setMessage("Are you sure to make a conversation with " + user + " ?")
                    .setCancelable(true)
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Qiscus.buildChatWith(user.getEmail())
                                    .build(PrivateChatCreationActivity.this, new Qiscus.ChatActivityBuilderListener() {
                                        @Override
                                        public void onSuccess(Intent intent) {
                                            startActivity(intent);
                                            finish();
                                        }

                                        @Override
                                        public void onError(Throwable throwable) {
                                            throwable.printStackTrace();
                                        }
                                    });
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();*/
        }

    }


    @Override
    public void onStrangerNameInputted(String email) {
        Qiscus.buildChatWith(email)
                .build(this, new Qiscus.ChatActivityBuilderListener() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivity(intent);
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        if (throwable instanceof HttpException) { //Error response from server
                            HttpException e = (HttpException) throwable;
                            try {
                                String errorMessage = e.response().errorBody().string();
                                JSONObject json = new JSONObject(errorMessage).getJSONObject("error");
                                String finalError = json.getString("message");
                                if (json.has("detailed_messages") ) {
                                    JSONArray detailedMessages = json.getJSONArray("detailed_messages");
                                    finalError = (String) detailedMessages.get(0);
                                }
                                showError(finalError,"Chat Error");
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        } else if (throwable instanceof IOException) { //Error from network
                            showError("Can not connect to qiscus server!","Network Error");
                        } else { //Unknown error
                            showError("Unexpected error!","Unknown Error");
                        }
                    }
                });
    }

    private void showError(String warning,String warningType) {
        android.support.v7.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new android.support.v7.app.AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new android.support.v7.app.AlertDialog.Builder(this);
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
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.action_search)
        {

        }
        else {
            finish();
        }

        return true;
    }

}
