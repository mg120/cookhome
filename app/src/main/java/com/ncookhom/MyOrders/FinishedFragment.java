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


public class FinishedFragment extends Fragment {

    private String open_chat_url = "http://cookehome.com/CookApp/getChat.php";
    private String Lat, Lan, email, Name, sender_name, recive_name;
    private String orders_url = "http://cookehome.com/CookApp/User/SearchMyOrders.php";

    public static TextView check_txt ;
    public static RecyclerView finished_orders_recycler;
    private RecyclerView.LayoutManager layoutManager;
    private OrdersFinishedAdapter adapter;

    private ArrayList<OrdersModel> finished_list = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            finished_list = getArguments().getParcelableArrayList("finished_list");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_finished, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        check_txt = getActivity().findViewById(R.id.finished_check_txt);
        finished_orders_recycler = (RecyclerView) getActivity().findViewById(R.id.finished_orders_recycler);
        layoutManager = new LinearLayoutManager(getActivity());

        if (finished_list.size() > 0) {
            check_txt.setVisibility(View.GONE);
            finished_orders_recycler.setVisibility(View.VISIBLE);

            finished_orders_recycler.setLayoutManager(layoutManager);
            finished_orders_recycler.setHasFixedSize(true);
            finished_orders_recycler.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

            adapter = new OrdersFinishedAdapter(getActivity(), finished_list);
            finished_orders_recycler.setAdapter(adapter);
        } else {
            check_txt.setVisibility(View.VISIBLE);
            finished_orders_recycler.setVisibility(View.GONE);
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
//                            }
//                        }
//                    }
//
//                    if (finished_list.size() > 0) {
//                        adapter = new OrdersAdapter(getActivity(), finished_list);
//                        finished_orders_listV.setAdapter(adapter);
//
//                        ///// handle list click listner ...
//                        finished_orders_listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                                final OrdersModel ordersModel = finished_list.get(i);
//                                final Dialog dialog = new Dialog(getActivity());
//                                TextView txt_close, order_user_name, order_user_email, order_user_phone;
//                                TextView order_item_name, order_itm_price, user_order_state;
//                                ImageView order_item_img;
//                                Button sure_add_order, reject_order, order_completed, cancel_order, family_chat;
//                                LinearLayout user_state_layout, osra_state_layout, user_info_layout;
//
//                                dialog.setContentView(R.layout.order_state_popup);
//                                txt_close = dialog.findViewById(R.id.txt_close);
//                                order_user_name = dialog.findViewById(R.id.order_user_name);
//                                order_user_email = dialog.findViewById(R.id.order_user_email);
//                                order_user_phone = dialog.findViewById(R.id.order_user_phone);
//                                order_item_name = dialog.findViewById(R.id.order_item_name);
//                                user_order_state = dialog.findViewById(R.id.user_order_state);
//                                order_itm_price = dialog.findViewById(R.id.order_item_price);
//                                order_item_img = dialog.findViewById(R.id.order_item_imgage);
//                                user_state_layout = dialog.findViewById(R.id.user_state_layout);
//                                osra_state_layout = dialog.findViewById(R.id.osra_state_layout);
//                                user_info_layout = dialog.findViewById(R.id.user_info);
//                                family_chat = dialog.findViewById(R.id.family_chat);
//                                sure_add_order = dialog.findViewById(R.id.accept_order);
//                                reject_order = dialog.findViewById(R.id.reject_order);
//                                order_completed = dialog.findViewById(R.id.complete_order);
//                                cancel_order = dialog.findViewById(R.id.cancel_order);
//
//                                txt_close.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        dialog.dismiss();
//                                    }
//                                });
//
//                                user_state_layout.setVisibility(View.VISIBLE);
//                                osra_state_layout.setVisibility(View.GONE);
//                                user_info_layout.setVisibility(View.GONE);
//                                family_chat.setVisibility(View.GONE);
//
//                                Picasso.with(getActivity()).load("http://cookehome.com/CookApp/images/" + ordersModel.getOrder_img()).into(order_item_img);
//                                order_item_name.setText(ordersModel.getOrder_name());
//                                order_user_name.setText(ordersModel.getCustomer_name());
//                                order_user_email.setText(ordersModel.getCustomer_mail());
//                                order_user_phone.setText(ordersModel.getCustomer_phone());
//                                order_itm_price.setText(ordersModel.getOrder_price() + " ريال ");
//                                if (ordersModel.getState_type().equals("0")) {
//                                    user_order_state.setText("قيد الانتظار");
//                                } else if (ordersModel.getState_type().equals("1")) {
//                                    user_order_state.setText("تم قبول طلبك");
//                                } else if (ordersModel.getState_type().equals("2")) {
//                                    user_order_state.setText("تم تجهيز طلبك");
//                                } else if (ordersModel.getState_type().equals("3")) {
//                                    user_order_state.setText("تم انهاء طلبك");
//                                } else if (ordersModel.getState_type().equals("4")) {
//                                    user_order_state.setText("تم رفض طلبك");
//                                }
//                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//                                dialog.show();
//
//                                family_chat.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        StringRequest open_chat_request = new StringRequest(Request.Method.POST, open_chat_url, new Response.Listener<String>() {
//                                            @Override
//                                            public void onResponse(String response) {
//
//                                                FamilyProducts.chat_msgs_list.clear();
//                                                try {
//                                                    JSONObject jsonObject = new JSONObject(response);
//                                                    boolean success = jsonObject.getBoolean("success");
//                                                    JSONArray jsonArray = jsonObject.getJSONArray("chat");
//                                                    for (int i = 0; i < jsonArray.length(); i++) {
//                                                        JSONObject data_obj = jsonArray.getJSONObject(i);
//                                                        String chat_id = data_obj.getString("chat_id");
//                                                        String sender_id = data_obj.getString("sender_id");
//                                                        sender_name = data_obj.getString("sender_name");
//                                                        String reciver_id = data_obj.getString("reciver_id");
//                                                        recive_name = data_obj.getString("recive_name");
//                                                        String message = data_obj.getString("message");
//                                                        String send_at_time = data_obj.getString("send_at");
//
//                                                        FamilyProducts.chat_msgs_list.add(new ChatModel(sender_id, sender_name, message, send_at_time));
//                                                    }
//
//                                                } catch (JSONException e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//                                        }, new Response.ErrorListener() {
//                                            @Override
//                                            public void onErrorResponse(VolleyError error) {
//                                                Toast.makeText(getActivity(), "لا يوجد اتصال بالانترنت", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }) {
//                                            @Override
//                                            protected Map<String, String> getParams() throws AuthFailureError {
//
//                                                Map<String, String> params = new HashMap<>();
//                                                params.put("sender_id", MainActivity.customer_id);
//                                                params.put("reciver_id", ordersModel.getCustomer_id());
//                                                return params;
//                                            }
//                                        };
//                                        Volley.newRequestQueue(getActivity()).add(open_chat_request);
//
//                                        new Handler().postDelayed(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                if (!MainActivity.customer_id.equals("")) {
//                                                    Intent intent1 = new Intent(getActivity(), Chat.class);
//                                                    intent1.putExtra("family_id", ordersModel.getCustomer_id());
//                                                    intent1.putExtra("family_name", ordersModel.getCustomer_name());
//                                                    startActivity(intent1);
//                                                } else {
//                                                    Toast.makeText(getActivity(), "يجب تسجيل الدخول اولا", Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                        }, 1000);
//                                    }
//                                });
//                                sure_add_order.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//
//                                        order_type = 1;
//                                        updateOrder_type(order_type, ordersModel.getOrder_id());
//                                        dialog.dismiss();
//                                    }
//                                });
//                                reject_order.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        order_type = 4;
//                                        updateOrder_type(order_type, ordersModel.getOrder_id());
//                                        dialog.dismiss();
//                                    }
//                                });
//                                order_completed.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        order_type = 2;
//                                        updateOrder_type(order_type, ordersModel.getOrder_id());
//                                        dialog.dismiss();
//                                    }
//                                });
//                                cancel_order.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        order_type = 3;
//                                        updateOrder_type(order_type, ordersModel.getOrder_id());
//                                        dialog.dismiss();
//                                    }
//                                });
//                            }
//                        });
//                    } else {
////                        Toast.makeText(getActivity(), "لا توجد طلبات حالية", Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                error.printStackTrace();
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
