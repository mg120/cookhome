package com.ncookhom.ProductDetails;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ncookhom.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Ma7MouD on 4/12/2018.
 */

public class ProDetailsAdapter extends PagerAdapter {

    Context context;
    String[] images;

    public ProDetailsAdapter(Context context, String[] images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.vpager_item, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.pager_img);
        Picasso.with(context).load("http://cookehome.com/CookApp/images/" + images[position]).into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
