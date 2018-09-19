package com.ncookhom.MyOrders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ncookhom.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ma7MouD on 8/28/2018.
 */

public class CompleteAdapter extends RecyclerView.Adapter<CompleteAdapter.ViewHolder> {

    GpsTracker gpsTraker;
    Context context;
    List<OrdersModel> list;
    double src_latitude, src_longitude, lat, lng;
    private String family_Data_url = "http://cookehome.com/CookApp/User/SearchUser.php";

    public CompleteAdapter(Context context, List<OrdersModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public CompleteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.complete_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CompleteAdapter.ViewHolder holder, final int position) {
        Picasso.with(context).load("http://cookehome.com/CookApp/images/" + list.get(position).getOrder_img()).into(holder.item_image);
        holder.item_name.setText(list.get(position).getOrder_name());
        holder.item_price.setText(list.get(position).getOrder_price());
//        try {
//            float one_item_price = Float.parseFloat(list.get(position).getOrder_price()) / Integer.parseInt(list.get(position).getOrder_quantity());
//            holder.item_price.setText(one_item_price + " ريال " );
//        }catch (NumberFormatException ex){
//            ex.printStackTrace();
//        }
        holder.item_Quantity.setText(list.get(position).getOrder_quantity());
        holder.item_state.setText("جاهز للاستلام الأن");
//        holder.item_total_price.setText(list.get(position).getOrder_price() + " ريال ");
        holder.item_total_price.setText("" + Float.parseFloat(list.get(position).getOrder_price()) * Integer.parseInt(list.get(position).getOrder_quantity()) + " ريال ");

        holder.location_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest family_data_request = new StringRequest(Request.Method.POST, family_Data_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                lat = Double.parseDouble(jsonObject.getString("Lat"));
                                lng = Double.parseDouble(jsonObject.getString("Lan"));
//                                Toast.makeText(context, lat + "\n" + lng, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "لا يوجد اتصال بالانترنت", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("ID", list.get(position).getCustomer_id());
                        return params;
                    }
                };
                Volley.newRequestQueue(context).add(family_data_request);

                getLocation();
                String uri = "http://maps.google.com/maps?saddr=" + src_latitude + "," + src_longitude + "&daddr=" + lat + "," + lat;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                context.startActivity(intent);
            }
        });

        holder.linearLayout.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout linearLayout;
        TextView item_name, item_price, item_Quantity, item_total_price, item_state;
        ImageView item_image;
        Button location_image;

        public ViewHolder(View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.complete_layout);
            item_name = itemView.findViewById(R.id.complete_item_name);
            item_price = itemView.findViewById(R.id.complete_item_price);
            item_Quantity = itemView.findViewById(R.id.complete_item_quantity);
            item_total_price = itemView.findViewById(R.id.complete_item_totalprice);
            item_state = itemView.findViewById(R.id.complete_state);
            item_image = itemView.findViewById(R.id.complete_item_img);
            location_image = itemView.findViewById(R.id.location_item_btn);
            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }

    private void getLocation() {
        gpsTraker = new GpsTracker(context);
        if (gpsTraker.canGetLocation()) {

            src_latitude = gpsTraker.getLatitude();
            src_longitude = gpsTraker.getLongitude();

        } else {
            gpsTraker.showSettingsAlert();
        }
    }
}
