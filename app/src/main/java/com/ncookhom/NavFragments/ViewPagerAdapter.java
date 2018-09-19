package com.ncookhom.NavFragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ma7MouD on 5/6/2018.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> frag_list= new ArrayList<>();
    private List<String> title_list = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return frag_list.get(position);
    }

    @Override
    public int getCount() {
        return frag_list.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title_list.get(position);
    }

    public void AddFragment(Fragment fragment, String title){

        frag_list.add(fragment);
        title_list.add(title);
    }
}
