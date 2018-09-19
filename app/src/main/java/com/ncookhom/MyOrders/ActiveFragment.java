package com.ncookhom.MyOrders;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.ncookhom.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ActiveFragment extends Fragment {

    private RecyclerView active_orders_recycler;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<OrdersModel> data_list = new ArrayList<>();
    private ArrayList<OrdersModel> finished_list = new ArrayList<>();
    private OrdersAdapter adapter;
    private int order_type = 0;

    private ProgressBar progressBar ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            data_list = getArguments().getParcelableArrayList("active_list");
//            Toast.makeText(getActivity(), "" + data_list.size(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_active, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView check_txt = getActivity().findViewById(R.id.check_txt);
        progressBar =  getActivity().findViewById(R.id.active_progress);
        active_orders_recycler = getActivity().findViewById(R.id.active_orders_recycler);
        layoutManager = new LinearLayoutManager(getActivity());

        if (data_list.size() > 0) {

            check_txt.setVisibility(View.GONE);
            active_orders_recycler.setVisibility(View.VISIBLE);

            active_orders_recycler.setLayoutManager(layoutManager);
            active_orders_recycler.setHasFixedSize(true);
            active_orders_recycler.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            adapter = new OrdersAdapter(getActivity(), data_list);
            active_orders_recycler.setAdapter(adapter);
        } else {
            active_orders_recycler.setVisibility(View.GONE);
            check_txt.setVisibility(View.VISIBLE);
        }


//        StringRequest orders_request = new StringRequest(Request.Method.POST, orders_url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                try {
//                    JSONArray jsonArray = new JSONArray(response);
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        boolean success = jsonObject.getBoolean("success");
//                        if (success) {
//                            String Order_id = jsonObject.getString("Order_id");
//                            String Product_Name = jsonObject.getString("Product_Name");
//                            String Product_img = jsonObject.getString("Product_img");
//                            String foodType = jsonObject.getString("FoodType");
//                            String Quantity = jsonObject.getString("Quantity");
//                            String Price = jsonObject.getString("Price");
//                            String Seller_id = jsonObject.getString("Seller_id");
//                            String Seller_name = jsonObject.getString("Seller_name");
//                            String Seller_mail = jsonObject.getString("Seller_mail");
//                            String Customer_id = jsonObject.getString("Customer_id");
//                            String Customer_name = jsonObject.getString("Customer_name");
//                            String Customer_mail = jsonObject.getString("Customer_mail");
//                            String Customer_phone = jsonObject.getString("Customer_phone");
//                            String state_type = jsonObject.getString("type");
//
//                            if (state_type.equals("3") || state_type.equals("2") || state_type.equals("4")) {
//                                finished_list.add(new OrdersModel(Product_img, Product_Name, foodType, Price, Quantity, Order_id, state_type, Seller_id,
//                                        Seller_name, Seller_mail, Customer_id, Customer_name, Customer_mail, Customer_phone));
//                            } else {
//                                data_list.add(new OrdersModel(Product_img, Product_Name, foodType, Price, Quantity, Order_id, state_type, Seller_id,
//                                        Seller_name, Seller_mail, Customer_id, Customer_name, Customer_mail, Customer_phone));
//                            }
//                        }
//                    }
//                    if (data_list.size() >0) {
//                        adapter = new OrdersAdapter(getActivity(), data_list);
//                        active_orders_listV.setAdapter(adapter);
//                    } else {
//
//                        Toast.makeText(getActivity(), "لا توجد طلبات حالية", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity(), "Error Connection", Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("Customer_id", MainActivity.customer_id);
//                return params;
//            }
//        };
//        RequestQueue queue = Volley.newRequestQueue(getActivity());
//        queue.add(orders_request);
//
//    }
//
//    public void updateOrder_type(final int type, final String order_id) {
//        StringRequest update_order_request = new StringRequest(Request.Method.POST, update_order_type, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                try {
//                    JSONObject jsonObject1 = new JSONObject(response);
//                    int success = jsonObject1.getInt("success");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Toast.makeText(getActivity(), "Error Connection", Toast.LENGTH_SHORT).show();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("type", type + "");
//                params.put("Order_id", order_id);
//                return params;
//            }
//        };
//        RequestQueue queue = Volley.newRequestQueue(getActivity());
//        queue.add(update_order_request);
    }
}
