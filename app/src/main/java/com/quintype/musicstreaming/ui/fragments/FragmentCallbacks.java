package com.quintype.musicstreaming.ui.fragments;

import android.app.Fragment;
import android.support.v4.util.Pair;


public interface FragmentCallbacks  {

    public void addFragment(Fragment fragment, String mBackStack);

    public void replaceFragment(Fragment fragment, String mBackStack);

    public void clickAnalyticsEvent(String categoryId, String actionId, String labelId, long value);

    public Fragment getmFragment();

    public void popCurrentFragment();

    public void propagateEvent(Pair<String, Object> event);
}
