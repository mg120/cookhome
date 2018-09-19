package com.ncookhom.MyOrders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import com.ncookhom.MainActivity;
import com.ncookhom.ProductDetails.OrderRequest;
import com.ncookhom.ProductDetails.ProductDetails;
import com.ncookhom.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ma7MouD on 4/25/2018.
 */

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    Context context;
    List<OrdersModel> list;
    private static final String order_url ="http://cookehome.com/CookApp/User/MakeOrder.php";
    private String update_order_type = "http://cookehome.com/CookApp/Other/UpdateType.php";

    public static int random_num = 254122;

    public OrdersAdapter(Context context, List<OrdersModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.orders_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrdersAdapter.ViewHolder holder, final int position) {

        Picasso.with(context).load("http://cookehome.com/CookApp/images/" + list.get(position).getOrder_img()).into(holder.imageView);
        holder.order_item_name.setText(list.get(position).getOrder_name());
//        try {
//            float one_item_price = Float.parseFloat(list.get(position).getOrder_price()) * Integer.parseInt(list.get(position).getOrder_quantity());
//            holder.order_item_price.setText(one_item_price + " ريال " );
//        }catch (NumberFormatException ex){
//            ex.printStackTrace();
//        }
        holder.order_item_price.setText(list.get(position).getOrder_price() + " ريال ");
        holder.order_item_quantity.setText(list.get(position).getOrder_quantity());

        if (list.get(position).getState_type().equals("0")) {
            holder.order_state.setText("قيد الانتظار");
        } else if (list.get(position).getState_type().equals("1")) {
            holder.order_state.setText("تم قبول طلبك");
        } else if (list.get(position).getState_type().equals("2")) {
            holder.order_state.setText("تم تجهيز طلبك");
        } else if (list.get(position).getState_type().equals("3")) {
            holder.order_state.setText("تم انهاء طلبك");
        } else if (list.get(position).getState_type().equals("4")) {
            holder.order_state.setText("تم رفض طلبك");
        }
        holder.order_item_totalprice.setText("" + Float.parseFloat(list.get(position).getOrder_price()) * Integer.parseInt(list.get(position).getOrder_quantity()) + " ريال ");
//        holder.order_item_totalprice.setText(list.get(position).getOrder_price()+ " ريال ");

        holder.order_item_another_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest again_order_request = new StringRequest(Request.Method.POST, order_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                Toast.makeText(context, "تم اضافة المنتج الى السلة", Toast.LENGTH_SHORT).show();
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
                        params.put("Product_Name", list.get(position).getOrder_name());
                        params.put("Product_img", list.get(position).getOrder_img());
                        params.put("FoodType", list.get(position).getOrder_foodtype());
                        params.put("Quantity", list.get(position).getOrder_quantity());
                        params.put("code", MainActivity.customer_id + random_num);
                        params.put("Price", list.get(position).getOrder_price());
                        params.put("Seller_id", list.get(position).getSeller_id());
                        params.put("Seller_name", list.get(position).getSeller_name());
                        params.put("Seller_mail", list.get(position).getSeller_mail());
                        params.put("Customer_id", list.get(position).getCustomer_id());
                        params.put("Customer_name", list.get(position).getCustomer_name());
                        params.put("Customer_mail", list.get(position).getCustomer_mail());
                        params.put("Customer_phone", list.get(position).getCustomer_phone());
                        return params;
                    }
                };
                Volley.newRequestQueue(context).add(again_order_request);
            }
        });
        holder.linearLayout.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView order_item_name, order_item_price, order_item_quantity, order_item_totalprice, order_state;
        TextView order_item_another_time;
        LinearLayout linearLayout;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.orders_item_layout);
            imageView = (ImageView) itemView.findViewById(R.id.order_item_img);
            order_item_name = (TextView) itemView.findViewById(R.id.order_item_name);
            order_item_price = (TextView) itemView.findViewById(R.id.order_item_price);
            order_item_quantity = (TextView) itemView.findViewById(R.id.order_item_quantity);
            order_item_totalprice = (TextView) itemView.findViewById(R.id.order_item_totalprice);
            order_state = (TextView) itemView.findViewById(R.id.order_state);
            order_item_another_time = (TextView) itemView.findViewById(R.id.order_item_another_time);

            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = (int) view.getTag();

        }
    }

//    private Context context;
//    private ArrayList<OrdersModel> list;
//
//    private static final String order_url = "http://cookehome.com/CookApp/User/MakeOrder.php";
//    private Map<String, String> params;
//    public static int random_num = 254122;
//
//    public OrdersAdapter(Context context, ArrayList<OrdersModel> list) {
//        this.context = context;
//        this.list = list;
//    }
//
//    @Override
//    public int getCount() {
//        return list.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return list.get(i);
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        view = LayoutInflater.from(context).inflate(R.layout.orders_item, viewGroup, false);
//
//        final ImageView order_img = view.findViewById(R.id.order_item_img);
//        final TextView order_name = view.findViewById(R.id.order_item_name);
//        final TextView order_price = view.findViewById(R.id.order_item_price);
//        final TextView order_quantity = view.findViewById(R.id.order_item_quantity);
//        final TextView order_total = view.findViewById(R.id.order_item_totalprice);
//        final TextView order_state = view.findViewById(R.id.order_state);
//
//        final TextView order_item_another_time = view.findViewById(R.id.order_item_another_time);
//
//        final OrdersModel ordersModel = list.get(i);
//
//        Picasso.with(context).load("http://cookehome.com/CookApp/images/" + ordersModel.getOrder_img()).into(order_img);
//        order_name.setText(ordersModel.getOrder_name());
//        order_price.setText(ordersModel.getOrder_price() + " ريال ");
//        order_quantity.setText(ordersModel.getOrder_quantity());
//
//        if (ordersModel.getState_type().equals("0")) {
//            order_state.setText("قيد الانتظار");
//        } else if (ordersModel.getState_type().equals("1")) {
//            order_state.setText("تم قبول طلبك");
//        } else if (ordersModel.getState_type().equals("2")) {
//            order_state.setText("تم تجهيز طلبك");
//        } else if (ordersModel.getState_type().equals("3")) {
//            order_state.setText("تم انهاء طلبك");
//        } else if (ordersModel.getState_type().equals("4")) {
//            order_state.setText("تم رفض طلبك");
//        }
//        order_total.setText("" + Float.parseFloat(ordersModel.getOrder_price()) * Integer.parseInt(ordersModel.getOrder_quantity()) + " ريال ");
//
//        order_item_another_time.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                StringRequest again_order_request = new StringRequest(Request.Method.POST, order_url, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            boolean success = jsonObject.getBoolean("success");
//                            if (success) {
//                                Toast.makeText(context, "تم اضافة المنتج الى السلة", Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                    }
//                }) {
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//
//                        params = new HashMap<>();
//                        params.put("Product_Name", ordersModel.getOrder_name());
//                        params.put("Product_img", ordersModel.getOrder_img());
//                        params.put("FoodType", ordersModel.getOrder_foodtype());
//                        params.put("Quantity", ordersModel.getOrder_quantity());
//                        params.put("code", MainActivity.customer_id + random_num);
//                        params.put("Price", ordersModel.getOrder_price());
//                        params.put("Seller_id", ordersModel.getSeller_id());
//                        params.put("Seller_name", ordersModel.getSeller_name());
//                        params.put("Seller_mail", ordersModel.getSeller_mail());
//                        params.put("Customer_id", ordersModel.getCustomer_id());
//                        params.put("Customer_name", ordersModel.getCustomer_name());
//                        params.put("Customer_mail", ordersModel.getCustomer_mail());
//                        params.put("Customer_phone", ordersModel.getCustomer_phone());
//                        return params;
//                    }
//                };
//                Volley.newRequestQueue(context).add(again_order_request);
//            }
//        });
//        return view;
//    }
}