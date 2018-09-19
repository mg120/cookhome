package com.ncookhom.NavFragments;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Ma7MouD on 6/24/2018.
 */

interface RecyclerTouchHelperListner {

    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
}
