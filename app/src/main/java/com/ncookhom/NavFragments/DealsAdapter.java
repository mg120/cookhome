package com.ncookhom.NavFragments;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ncookhom.MyOrders.OrdersModel;
import com.ncookhom.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ma7MouD on 8/15/2018.
 */

public class DealsAdapter extends BaseAdapter {

    Context context ;
    List<OrdersModel> list ;

    public DealsAdapter(Context context, List<OrdersModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.last_deals_item, viewGroup, false);

        Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/font.ttf");
        ImageView imageView = (ImageView) view.findViewById(R.id.deals_item_img);
        TextView title = (TextView) view.findViewById(R.id.deals_item_name);
//        title.setTypeface(custom_font);
        TextView deals_item_totalprice = (TextView) view.findViewById(R.id.deals_item_totalprice);
//        deals_item_totalprice.setTypeface(custom_font);
        TextView price = (TextView) view.findViewById(R.id.deals_item_price);
        TextView quantity = (TextView) view.findViewById(R.id.deals_item_quantity);
//        price.setTypeface(custom_font);

        //  set Data to items ....
        if (list != null){

            Picasso.with(context).load("http://cookehome.com/CookApp/images/"+ list.get(i).getOrder_img()).into(imageView);
            title.setText(list.get(i).getOrder_name());
            deals_item_totalprice.setText("" + Float.parseFloat(list.get(i).getOrder_price()) * Integer.parseInt(list.get(i).getOrder_quantity()) + " ريال ");
            quantity.setText(list.get(i).getOrder_quantity());
//            ratingBar.setRating(Float.parseFloat(list.get(i).getFamily_rate()));
            price.setText(list.get(i).getOrder_price());
        }
        return view;
    }
}
