package com.quintype.musicstreaming.ui.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.quintype.musicstreaming.R;
import com.quintype.musicstreaming.ui.fragments.FragmentCallbacks;

import java.util.ArrayList;

public abstract class BaseFragmentActivity extends AppCompatActivity implements FragmentCallbacks,
        FragmentManager.OnBackStackChangedListener {
    ArrayList<Fragment> mFragmentList = new ArrayList<>();
    Fragment mFragment;
    AppCompatActivity mContext;


    public void addFragment(Fragment fragment, String mBackStack) {
        if (mContext == null) {
            return;
        }
        mFragmentList.add(fragment);

        FragmentTransaction fragmentTransaction = mContext.getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.home_container, fragment);

        if (mBackStack != null) {
            fragmentTransaction.addToBackStack(mBackStack);
        }
        mFragment = fragment;
        fragmentTransaction.commit();
    }

    public void replaceFragment(Fragment fragment, String mBackStack) {
        if (mContext == null) {
            return;
        }
        mFragmentList.add(fragment);

        FragmentTransaction fragmentTransaction = mContext.getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_container, fragment);

        if (mBackStack != null) {
            fragmentTransaction.addToBackStack(mBackStack);
        }
        mFragment = fragment;
        fragmentTransaction.commit();
    }

    public Fragment getmFragment() {
        if (mFragmentList.size() > 0) {
            return mFragmentList.get(mFragmentList.size() - 1);
        }
        return null;
    }

    public void popCurrentFragment() {
        if (mFragmentList.size() > 0) {
            mFragmentList.remove(mFragmentList.size() - 1);
            if (mFragmentList.size() > 0) {
                mFragmentList.remove(mFragmentList.size() - 1);
            }
        }
        mContext.getSupportFragmentManager().popBackStack();
    }
}
