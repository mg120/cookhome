package com.ncookhom.Card;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ncookhom.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ma7MouD on 4/15/2018.
 */

public class CardAdapter extends BaseAdapter {

    Context context;
    public static ArrayList<CardModel> list;
    private String delete_url = "http://cookehome.com/CookApp/Other/DeleteOrder.php";

    public static ArrayList<CardModel> final_card_list = new ArrayList<>();

    public static String final_prod_price;

    float total;
    static int number;
    CardAdapter adapter;

    public CardAdapter(Context context, ArrayList<CardModel> list, float total) {
        this.context = context;
        this.list = list;
        this.total = total;
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
    public View getView(final int i, View view, final ViewGroup viewGroup) {

        view = LayoutInflater.from(context).inflate(R.layout.card_item, viewGroup, false);

//        Toast.makeText(context, "total adapter: " + total, Toast.LENGTH_SHORT).show();
        final ImageView delete_card_item = view.findViewById(R.id.delete_card_item);
        final ImageView imageView = view.findViewById(R.id.order_pro_img);
        final ImageView card_increase_img = view.findViewById(R.id.card_increase_img);
        final ImageView card_decrease_img = view.findViewById(R.id.card_decrease_img);
        final TextView card_prod_name = view.findViewById(R.id.card_pro_name);
        final TextView card_prod_price = view.findViewById(R.id.card_pro_price);
        final TextView card_prod_num = view.findViewById(R.id.card_prod_num);


        final CardModel cardModel = list.get(i);
        Picasso.with(context).load("http://cookehome.com/CookApp/images/" + list.get(i).getOrder_pro_img()).into(imageView);
        card_prod_name.setText(cardModel.getOrder_pro_name());
//        card_prod_price.setText(cardModel.getOrder_pro_price() + " ريال ");
        card_prod_price.setText( Float.parseFloat("" +list.get(i).getOrder_pro_price()) * Integer.parseInt(list.get(i).getOrder_pro_quantity()) + " ريال ");
        card_prod_num.setText(cardModel.getOrder_pro_quantity());

        MyCard.total_tv.setText(total + "");

        final int[] num = {Integer.parseInt(card_prod_num.getText().toString())};

        number = Integer.parseInt(card_prod_num.getText().toString());
//        //////////////////////////////////////
        card_increase_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final float one_piece_price = Float.parseFloat(list.get(i).getOrder_pro_price()) / Integer.parseInt(list.get(i).getOrder_pro_quantity());
                num[0] = num[0] + 1;
                cardModel.setOrder_pro_quantity(num[0] + "");
                cardModel.setOrder_pro_price((one_piece_price * num[0]) + "");
                total += one_piece_price;
                CardAdapter.this.notifyDataSetChanged();

            }
        });

        card_decrease_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (num[0] > 1) {
                    final float one_piece_price = Float.parseFloat(list.get(i).getOrder_pro_price()) / Integer.parseInt(list.get(i).getOrder_pro_quantity());
                    num[0] = num[0] - 1;
                    cardModel.setOrder_pro_quantity(num[0] + "");
                    cardModel.setOrder_pro_price((one_piece_price * num[0]) + "");
                    total -= one_piece_price;
                    CardAdapter.this.notifyDataSetChanged();
                }
            }
        });

        //////////////////////////////////
        delete_card_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //will show popup menu here
                //creating a popup menu
//                final int item_count = Integer.parseInt(list.get(i).getOrder_pro_quantity());
                final float one_piece_price = Float.parseFloat(list.get(i).getOrder_pro_price()) / Integer.parseInt(list.get(i).getOrder_pro_quantity());
                StringRequest delete_request = new StringRequest(Request.Method.POST, delete_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            if (success == 1) {
                                list.remove(i);
                                total -= num[0] * one_piece_price ;
                                CardAdapter.this.notifyDataSetChanged();
                                Toast.makeText(context, "تم حذف المنتج", Toast.LENGTH_SHORT).show();
                                if (list.size() == 0) {
                                    MyCard.card_products_layout.setVisibility(View.GONE);
                                    MyCard.no_products_layout.setVisibility(View.VISIBLE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("Order_id", list.get(i).getOrder_pro_id());
                        return params;
                    }
                };
                Volley.newRequestQueue(context).add(delete_request);

            }
        });
        return view;
    }

    public static ArrayList<CardModel> getlist() {

        return final_card_list;
    }
}