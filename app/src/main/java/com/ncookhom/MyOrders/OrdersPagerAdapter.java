package com.ncookhom.MyOrders;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ncookhom.OrderedFromMe.FromMActiveFrag;
import com.ncookhom.OrderedFromMe.FromMFinishedFrag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ma7MouD on 5/21/2018.
 */

public class OrdersPagerAdapter extends FragmentPagerAdapter {

    int Numoftabs;
    ArrayList<OrdersModel> active_list;
    ArrayList<OrdersModel> finished_list;
    ArrayList<OrdersModel> complete_list;

    public OrdersPagerAdapter(FragmentManager fm, int Numoftabs, ArrayList<OrdersModel> active_list, ArrayList<OrdersModel> finished_list, ArrayList<OrdersModel> complete_list) {
        super(fm);
        this.Numoftabs = Numoftabs;
        this.active_list = active_list;
        this.finished_list = finished_list;
        this.complete_list = complete_list;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ActiveFragment tab1 = new ActiveFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("active_list", active_list);
                tab1.setArguments(bundle);
                return tab1;
            case 1:
                FinishedFragment tab2 = new FinishedFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putParcelableArrayList("finished_list", finished_list);
                tab2.setArguments(bundle2);
                return tab2;
            case 2:
                CompleteFrag tab3 = new CompleteFrag();
                Bundle bundle3 = new Bundle();
                bundle3.putParcelableArrayList("complete_list", complete_list);
                tab3.setArguments(bundle3);
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return Numoftabs;
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        return title_list.get(position);
//    }
//
//    public void AddFragment(Fragment fragment, String title) {
//        frag_list.add(fragment);
//        title_list.add(title);
//    }

}
