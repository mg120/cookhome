package com.ncookhom.MyOrders;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.arturogutierrez.Badges;
import com.github.arturogutierrez.BadgesNotSupportedException;
import com.ncookhom.MainActivity;
import com.ncookhom.OrderedFromMe.VPagerAdapter;
import com.ncookhom.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ncookhom.NavFragments.AppController.TAG;

public class Orders extends AppCompatActivity {

    private String orders_url = "http://cookehome.com/CookApp/User/SearchMyOrders.php";
    private String update_order_type = "http://cookehome.com/CookApp/Other/UpdateType.php";
    private String open_chat_url = "http://cookehome.com/CookApp/getChat.php";

    private String Lat, Lan, email, Name, sender_name, recive_name;
    private ArrayList<OrdersModel> data_list = new ArrayList<>();
    private ArrayList<OrdersModel> finished_list = new ArrayList<>();
    private ArrayList<OrdersModel> completed_list = new ArrayList<>();
    private OrdersAdapter adapter;
    private int order_type = 0;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private OrdersPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        // To Clear Notication Badge from App icon
//        try {
//            Badges.removeBadge(this);
//            // Alternative way
//            Badges.setBadge(this, 0);
//        } catch (BadgesNotSupportedException badgesNotSupportedException) {
//            Log.d(TAG, badgesNotSupportedException.getMessage());
//        }

        TextView back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        tabLayout = findViewById(R.id.tab_layout_id);
        tabLayout.addTab(tabLayout.newTab().setText("طلبات نشطة"));
        tabLayout.addTab(tabLayout.newTab().setText("طلبات منتهية"));
        tabLayout.addTab(tabLayout.newTab().setText("جاهزة للاستلام"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        StringRequest orders_request = new StringRequest(Request.Method.POST, orders_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    data_list.clear();
                    finished_list.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
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

                            if (state_type.equals("3") || state_type.equals("4")) {
                                finished_list.add(new OrdersModel(Product_img, Product_Name, foodType, Price, Quantity, Order_id, state_type, Seller_id,
                                        Seller_name, Seller_mail, Customer_id, Customer_name, Customer_mail, Customer_phone));
                            } else if (state_type.equals("0") || state_type.equals("1")) {
                                data_list.add(new OrdersModel(Product_img, Product_Name, foodType, Price, Quantity, Order_id, state_type, Seller_id,
                                        Seller_name, Seller_mail, Customer_id, Customer_name, Customer_mail, Customer_phone));
                            } else if (state_type.equals("2")) {
                                completed_list.add(new OrdersModel(Product_img, Product_Name, foodType, Price, Quantity, Order_id, state_type, Seller_id,
                                        Seller_name, Seller_mail, Customer_id, Customer_name, Customer_mail, Customer_phone));
                            }
                        }
                    }

                    pagerAdapter = new OrdersPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), data_list, finished_list, completed_list);
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
//                    if (data_list.size() >0) {
//                        adapter = new OrdersAdapter(Orders.this, data_list);
//                        active_orders_listV.setAdapter(adapter);
//                    } else {
//
//                        Toast.makeText(Orders.this, "لا توجد طلبات حالية", Toast.LENGTH_SHORT).show();
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Orders.this, "Error Connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Customer_id", MainActivity.customer_id);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Orders.this);
        queue.add(orders_request);

//
//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        pagerAdapter = new OrdersPagerAdapter(getSupportFragmentManager());
//        pagerAdapter.AddFragment(new ActiveFragment(), "طلبات نشطة");
//        pagerAdapter.AddFragment(new FinishedFragment(), "طلبات منتهية");
//
//        // Add Adapter to ViewPager .....
//        viewPager.setAdapter(pagerAdapter);
//
//        // Set ViewPager to Tab Layout .....
//        tabLayout = (TabLayout) findViewById(R.id.tab_layout_id);
//        tabLayout.setupWithViewPager(viewPager);

        // set tabs indicator color ....
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FF0000"));
        tabLayout.setTabTextColors(Color.parseColor("#000000"), Color.parseColor("#ffffff"));
        viewPager.setCurrentItem(0);

    }

    public void updateOrder_type(final int type, final String order_id) {
        StringRequest update_order_request = new StringRequest(Request.Method.POST, update_order_type, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    int success = jsonObject1.getInt("success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                error.getMessage();
//                Toast.makeText(Orders.this, "Error Connection", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("type", type + "");
                params.put("Order_id", order_id);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Orders.this);
        queue.add(update_order_request);
    }

}