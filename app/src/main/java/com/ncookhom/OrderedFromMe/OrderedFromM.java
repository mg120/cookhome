package com.ncookhom.OrderedFromMe;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.ncookhom.Chat.Chat;
import com.ncookhom.Chat.ChatModel;
import com.ncookhom.FamilyProducts.FamilyProducts;
import com.ncookhom.MainActivity;
import com.ncookhom.MyOrders.OrdersModel;
import com.ncookhom.MyOrders.OrdersPagerAdapter;
import com.ncookhom.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderedFromM extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private VPagerAdapter pagerAdapter;

    public static ArrayList<OrdersModel> data_list = new ArrayList<>();
    public static ArrayList<OrdersModel> finished_list = new ArrayList<>();
    private String fromm_orders_url = "http://cookehome.com/CookApp/User/SearchSellerOrders.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered_from_m);

        TextView back = findViewById(R.id.back);
        viewPager = findViewById(R.id.viewpager_from_me);
        tabLayout = findViewById(R.id.tab_layout_id);

        tabLayout.addTab(tabLayout.newTab().setText("طلبات نشطة"));
        tabLayout.addTab(tabLayout.newTab().setText("طلبات منتهية"));
//        tabLayout.addTab(tabLayout.newTab().setText("جاهزة للاستلام"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST, fromm_orders_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    data_list.clear();
                    finished_list.clear();

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        boolean success = jsonObject.getBoolean("success");
//                        if (success) {
                        String Order_id = jsonObject.getString("Order_id");
                        String Product_Name = jsonObject.getString("Product_Name");
                        String Product_img = jsonObject.getString("Product_img");
                        String foodType = jsonObject.getString("FoodType");
                        String Quantity = jsonObject.getString("Quantity");
                        String Price = jsonObject.getString("Price");
                        String Seller_id = jsonObject.getString("Seller_id");
                        String Seller_name = jsonObject.getString("Seller_name");
                        String Seller_mail = jsonObject.getString("Seller_mail");
                        String Customer_id = jsonObject.getString("Customer_id");
                        String Customer_name = jsonObject.getString("Customer_name");
                        String Customer_mail = jsonObject.getString("Customer_mail");
                        String Customer_phone = jsonObject.getString("Customer_phone");
                        String state_type = jsonObject.getString("type");

                        if (state_type.equals("0") || state_type.equals("1")) {
                            data_list.add(new OrdersModel(Product_img, Product_Name, foodType, Price, Quantity, Order_id, state_type, Seller_id,
                                    Seller_name, Seller_mail, Customer_id, Customer_name, Customer_mail, Customer_phone));
                        } else if (state_type.equals("2") || state_type.equals("3") || state_type.equals("4")) {
                            finished_list.add(new OrdersModel(Product_img, Product_Name, foodType, Price, Quantity, Order_id, state_type, Seller_id,
                                    Seller_name, Seller_mail, Customer_id, Customer_name, Customer_mail, Customer_phone));
                        }
                    }
                    pagerAdapter = new VPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), data_list, finished_list);
                    viewPager.setAdapter(pagerAdapter);

                    // Set ViewPager to Tab Layout .....
                    viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            viewPager.setCurrentItem(tab.getPosition());
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {
                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("seller_id", MainActivity.customer_id);
                return params;
            }
        };
        Volley.newRequestQueue(this).

                add(stringRequest);

//        // set tabs indicator color ....
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FF0000"));
        tabLayout.setTabTextColors(Color.parseColor("#000000"), Color.parseColor("#ffffff"));
        viewPager.setCurrentItem(0);


    }
}