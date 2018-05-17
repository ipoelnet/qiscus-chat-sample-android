package com.qiscus.chat.ngobrel.ui.homepagetab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.qiscus.chat.ngobrel.ui.homepagetab.contact.ContactFragment;
import com.qiscus.chat.ngobrel.ui.homepagetab.recentconversation.RecentConversationFragment;

/**
 * Created by asyrof on 17/11/17.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new RecentConversationFragment();
            case 1:
                return new ContactFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
