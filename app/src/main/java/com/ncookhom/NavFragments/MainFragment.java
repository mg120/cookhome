package com.ncookhom.NavFragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ncookhom.R;

public class MainFragment extends Fragment {

    public  static String selected_item ;
    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            selected_item = getArguments().getString("selected_item");
        }
        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);

        // getChildFragmentManager ... NOT .... getSupportFragmentManager ...
        // work corrctly with tabs in fragment and with recyclerview ...
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.AddFragment(new HomeFragment(), "عرض قائمة");
        adapter.AddFragment(new HomeMapFrag(), "الخريطة");
        viewPager.setAdapter(adapter);
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout_id);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ffffff"));
        viewPager.setCurrentItem(0);
    }

    private void displaySelectedFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}
