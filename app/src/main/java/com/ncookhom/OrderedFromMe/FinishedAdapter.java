package com.ncookhom.OrderedFromMe;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.ncookhom.Card.CardAdapter;
import com.ncookhom.Chat.Chat;
import com.ncookhom.Chat.ChatModel;
import com.ncookhom.FamilyProducts.FamilyProducts;
import com.ncookhom.MainActivity;
import com.ncookhom.MyOrders.OrdersModel;
import com.ncookhom.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ma7MouD on 8/8/2018.
 */

public class FinishedAdapter extends RecyclerView.Adapter<FinishedAdapter.ViewHolder> {

    Context context;
    List<OrdersModel> finished_list;

    private int order_type = 0;

    private String open_chat_url = "http://cookehome.com/CookApp/getChat.php";
    private String Lat, Lan, email, Name, sender_name, recive_name;
    String fromm_orders_url = "http://cookehome.com/CookApp/User/SearchSellerOrders.php";
    private String update_order_type = "http://cookehome.com/CookApp/Other/UpdateType.php";
    private String delete_url = "http://cookehome.com/CookApp/User/deletefinishorders.php";

    public FinishedAdapter(Context context, List<OrdersModel> finished_list) {
        this.context = context;
        this.finished_list = finished_list;
    }

    @Override
    public FinishedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.from_m_finished_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FinishedAdapter.ViewHolder holder, final int position) {

        Picasso.with(context).load("http://cookehome.com/CookApp/images/" + finished_list.get(position).getOrder_img()).into(holder.item_image);
        holder.item_name.setText(finished_list.get(position).getOrder_name());
        holder.item_price.setText(finished_list.get(position).getOrder_price());
        holder.item_Quantity.setText(finished_list.get(position).getOrder_quantity());
        holder.item_total_price.setText("" + Float.parseFloat(finished_list.get(position).getOrder_price()) * Integer.parseInt(finished_list.get(position).getOrder_quantity()) + " ريال ");

        if (finished_list.get(position).getState_type().equals("0")) {
            holder.item_state.setText("قيد الانتظار");
        } else if (finished_list.get(position).getState_type().equals("1")) {
            holder.item_state.setText("تم قبول طلبك");
        } else if (finished_list.get(position).getState_type().equals("2")) {
            holder.item_state.setText("تم تجهيز طلبك");
        } else if (finished_list.get(position).getState_type().equals("3")) {
            holder.item_state.setText("تم انهاء طلبك");
        } else if (finished_list.get(position).getState_type().equals("4")) {
            holder.item_state.setText("تم رفض طلبك");
        }
        holder.linearLayout.setTag(position);
    }

    private void delete_item(final int position) {
        StringRequest delete_request = new StringRequest(Request.Method.POST, delete_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    Log.e("success:", success + "");
                    Log.d("success::", success + "");
                    if (success) {
                        finished_list.remove(position);
                        FinishedAdapter.this.notifyItemRemoved(position);
                        FinishedAdapter.this.notifyItemRangeChanged(position, finished_list.size());
                        if (finished_list.isEmpty()) {
                            FromMFinishedFrag.check_txt.setVisibility(View.VISIBLE);
                            FromMFinishedFrag.finished_recycler.setVisibility(View.GONE);
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
                HashMap<String, String> params = new HashMap<>();
                params.put("order_id", finished_list.get(position).getOrder_id());
                return params;
            }
        };
        Volley.newRequestQueue(context).add(delete_request);
    }

    @Override
    public int getItemCount() {
        return finished_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout linearLayout;
        TextView item_name, item_price, item_Quantity, item_total_price, item_state, delete_item;
        ImageView item_image;

        public ViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.finished_item_layout);
            item_image = (ImageView) itemView.findViewById(R.id.finished_order_item_img);
            item_name = (TextView) itemView.findViewById(R.id.finished_order_item_name);
            item_price = (TextView) itemView.findViewById(R.id.finished_order_item_price);
            item_Quantity = (TextView) itemView.findViewById(R.id.finished_order_item_quantity);
            item_total_price = (TextView) itemView.findViewById(R.id.finished_order_item_totalprice);
            item_state = (TextView) itemView.findViewById(R.id.finished_order_state);
            delete_item = (TextView) itemView.findViewById(R.id.delete_finished_order_item);

            delete_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delete_item(getAdapterPosition());
                }
            });
            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = (int) view.getTag();
            final OrdersModel ordersModel = finished_list.get(position);
            final Dialog dialog = new Dialog(context);
            TextView txt_close, order_user_name, order_user_email, order_user_phone;
            TextView order_item_name, order_itm_price, user_order_state;
            ImageView order_item_img;
            Button sure_add_order, reject_order, order_completed, cancel_order, family_chat;
            LinearLayout user_state_layout, osra_state_layout, user_info_layout;

            dialog.setContentView(R.layout.order_state_popup);
            txt_close = dialog.findViewById(R.id.txt_close);
            order_user_name = dialog.findViewById(R.id.order_user_name);
            order_user_email = dialog.findViewById(R.id.order_user_email);
            order_user_phone = dialog.findViewById(R.id.order_user_phone);
            order_item_name = dialog.findViewById(R.id.order_item_name);
            user_order_state = dialog.findViewById(R.id.user_order_state);
            order_itm_price = dialog.findViewById(R.id.order_item_price);
            order_item_img = dialog.findViewById(R.id.order_item_imgage);
            user_state_layout = dialog.findViewById(R.id.user_state_layout);
            osra_state_layout = dialog.findViewById(R.id.osra_state_layout);
            user_info_layout = dialog.findViewById(R.id.user_info);
            family_chat = dialog.findViewById(R.id.family_chat);
            sure_add_order = dialog.findViewById(R.id.accept_order);
            reject_order = dialog.findViewById(R.id.reject_order);
            order_completed = dialog.findViewById(R.id.complete_order);
            cancel_order = dialog.findViewById(R.id.cancel_order);

            txt_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });


            user_state_layout.setVisibility(View.GONE);
            osra_state_layout.setVisibility(View.VISIBLE);
            user_info_layout.setVisibility(View.VISIBLE);
            family_chat.setVisibility(View.VISIBLE);

            Picasso.with(context).load("http://cookehome.com/CookApp/images/" + ordersModel.getOrder_img()).into(order_item_img);
            order_item_name.setText(ordersModel.getOrder_name());
            order_user_name.setText(ordersModel.getCustomer_name());
            order_user_email.setText(ordersModel.getCustomer_mail());
            order_user_phone.setText(ordersModel.getCustomer_phone());
            order_itm_price.setText(ordersModel.getOrder_price() + " ريال ");
            if (ordersModel.getState_type().equals("0")) {
                user_order_state.setText("قيد الانتظار");
            } else if (ordersModel.getState_type().equals("1")) {
                user_order_state.setText("تم قبول طلبك");
            } else if (ordersModel.getState_type().equals("2")) {
                user_order_state.setText("تم تجهيز طلبك");
            } else if (ordersModel.getState_type().equals("3")) {
                user_order_state.setText("تم انهاء طلبك");
            } else if (ordersModel.getState_type().equals("4")) {
                user_order_state.setText("تم رفض طلبك");
            }
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.show();

            /////////////////////////////////////////////////////////////////
            family_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    StringRequest open_chat_request = new StringRequest(Request.Method.POST, open_chat_url, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//
//                            FamilyProducts.chat_msgs_list.clear();
//                            try {
//                                JSONObject jsonObject = new JSONObject(response);
//                                boolean success = jsonObject.getBoolean("success");
//                                JSONArray jsonArray = jsonObject.getJSONArray("chat");
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject data_obj = jsonArray.getJSONObject(i);
//                                    String chat_id = data_obj.getString("chat_id");
//                                    String sender_id = data_obj.getString("sender_id");
//                                    sender_name = data_obj.getString("sender_name");
//                                    String reciver_id = data_obj.getString("reciver_id");
//                                    recive_name = data_obj.getString("recive_name");
//                                    String message = data_obj.getString("message");
//                                    String send_at_time = data_obj.getString("send_at");
//
//                                    FamilyProducts.chat_msgs_list.add(new ChatModel(sender_id, sender_name, message, send_at_time));
//                                }
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(context, "لا يوجد اتصال بالانترنت", Toast.LENGTH_SHORT).show();
//                        }
//                    }) {
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//
//                            Map<String, String> params = new HashMap<>();
//                            params.put("sender_id", MainActivity.customer_id);
//                            params.put("reciver_id", ordersModel.getCustomer_id());
//                            return params;
//                        }
//                    };
//                    Volley.newRequestQueue(context).add(open_chat_request);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!MainActivity.customer_id.equals("")) {
                                Intent intent1 = new Intent(context, Chat.class);
                                intent1.putExtra("sender_id", MainActivity.customer_id);
                                intent1.putExtra("receiver_id", ordersModel.getCustomer_id());
                                intent1.putExtra("sender_name", MainActivity.Name);
                                intent1.putExtra("receiver_name", ordersModel.getCustomer_name());
                                context.startActivity(intent1);
                            } else {
                                Toast.makeText(context, "يجب تسجيل الدخول اولا", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 10);
                }
            });
            sure_add_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    order_type = 1;
                    updateOrder_type(order_type, ordersModel.getOrder_id());
                    dialog.dismiss();
                }
            });
            reject_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    order_type = 4;
                    updateOrder_type(order_type, ordersModel.getOrder_id());
                    dialog.dismiss();
                }
            });
            order_completed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    order_type = 2;
                    updateOrder_type(order_type, ordersModel.getOrder_id());
                    dialog.dismiss();
                }
            });
            cancel_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    order_type = 3;
                    updateOrder_type(order_type, ordersModel.getOrder_id());
                    dialog.dismiss();
                }
            });
        }
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

                Toast.makeText(context, "Error Connection", Toast.LENGTH_SHORT).show();
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
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(update_order_request);
    }
}
