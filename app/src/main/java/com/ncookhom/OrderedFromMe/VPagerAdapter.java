package com.ncookhom.OrderedFromMe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ncookhom.MyOrders.CompleteFrag;
import com.ncookhom.MyOrders.OrdersModel;

import java.util.ArrayList;

/**
 * Created by Ma7MouD on 8/7/2018.
 */

public class VPagerAdapter extends FragmentPagerAdapter {

    int Numoftabs;
    ArrayList<OrdersModel> active_list;
    ArrayList<OrdersModel> finished_list;

    public VPagerAdapter(FragmentManager fm, int numoftabs, ArrayList<OrdersModel> active_list, ArrayList<OrdersModel> finished_list) {
        super(fm);
        this.Numoftabs = numoftabs;
        this.active_list = active_list;
        this.finished_list = finished_list;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FromMActiveFrag tab1 = new FromMActiveFrag();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("active_list", active_list);
                tab1.setArguments(bundle);
                return tab1;
            case 1:
                FromMFinishedFrag tab2 = new FromMFinishedFrag();
                Bundle bundle2 = new Bundle();
                bundle2.putParcelableArrayList("finished_list", finished_list);
                tab2.setArguments(bundle2);
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return Numoftabs;
    }
}
