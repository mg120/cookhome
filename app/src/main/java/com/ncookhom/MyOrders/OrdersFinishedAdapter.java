package com.ncookhom.MyOrders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ncookhom.OrderedFromMe.FinishedAdapter;
import com.ncookhom.OrderedFromMe.FromMFinishedFrag;
import com.ncookhom.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ma7MouD on 8/14/2018.
 */

public class OrdersFinishedAdapter extends RecyclerView.Adapter<OrdersFinishedAdapter.ViewHolder> {

    Context context ;
    List<OrdersModel> list ;
    private String delete_url = "http://cookehome.com/CookApp/User/deletefinishorders.php";

    public OrdersFinishedAdapter(Context context, List<OrdersModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.from_m_finished_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Picasso.with(context).load("http://cookehome.com/CookApp/images/" + list.get(position).getOrder_img()).into(holder.item_image);
        holder.item_name.setText(list.get(position).getOrder_name());
        holder.item_price.setText(list.get(position).getOrder_price());
        holder.item_Quantity.setText(list.get(position).getOrder_quantity());
        holder.item_total_price.setText("" + Float.parseFloat(list.get(position).getOrder_price()) * Integer.parseInt(list.get(position).getOrder_quantity()) + " ريال ");

        if (list.get(position).getState_type().equals("0")) {
            holder.item_state.setText("قيد الانتظار");
        } else if (list.get(position).getState_type().equals("1")) {
            holder.item_state.setText("تم قبول طلبك");
        } else if (list.get(position).getState_type().equals("2")) {
            holder.item_state.setText("تم تجهيز طلبك");
        } else if (list.get(position).getState_type().equals("3")) {
            holder.item_state.setText("تم انهاء طلبك");
        } else if (list.get(position).getState_type().equals("4")) {
            holder.item_state.setText("تم رفض طلبك");
        }
        holder.linearLayout.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

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
//                    getAdapterPosition()
                    delete_item(getAdapterPosition());
                }
            });
            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }

    // Delete Item From Finished Orders
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
                        list.remove(position);
                        OrdersFinishedAdapter.this.notifyItemRemoved(position);
                        OrdersFinishedAdapter.this.notifyItemRangeChanged(position, list.size());
                        if (list.isEmpty()) {
                            FinishedFragment.check_txt.setVisibility(View.VISIBLE);
                            FinishedFragment.finished_orders_recycler.setVisibility(View.GONE);
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
                params.put("order_id", list.get(position).getOrder_id());
                return params;
            }
        };
        Volley.newRequestQueue(context).add(delete_request);
    }

}
