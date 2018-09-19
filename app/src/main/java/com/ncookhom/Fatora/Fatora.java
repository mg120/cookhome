package com.ncookhom.Fatora;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ncookhom.Card.CardModel;
import com.ncookhom.MainActivity;
import com.ncookhom.ProductDetails.ProductDetails;
import com.ncookhom.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Fatora extends AppCompatActivity {

    RecyclerView fatora_recycler;
    FatoraAdapter adapter;
    private String finished_orders = "http://cookehome.com/CookApp/User/FinishedOrders.php";
    ArrayList<FatoraModel> fatoraModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fatora);
        Intent intent = getIntent();
        ArrayList<CardModel> list = intent.getExtras().getParcelableArrayList("card_list");
        final int total_quantity = getIntent().getExtras().getInt("total_quantity");
        final float total_price = getIntent().getExtras().getFloat("total");
        final String date = getIntent().getExtras().getString("date");
        final String code = getIntent().getExtras().getString("code");
        final double lat = getIntent().getExtras().getDouble("lat");
        final double lan = getIntent().getExtras().getDouble("lan");
        final String address_val = getIntent().getExtras().getString("address");

        Log.d("Quan:", total_quantity + "");
        Log.d("Price:", total_price + "");
        SharedPreferences prefs = getSharedPreferences(ProductDetails.random_num_file, MODE_PRIVATE);
        final int random_num = prefs.getInt("random_num", 0);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final String currentDateandTime = sdf.format(new Date());

        TextView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        final TextView quantity_txt = findViewById(R.id.all_products_num);
        final TextView price_txt = findViewById(R.id.all_products_salary);
        final TextView state_txt = findViewById(R.id.all_order_status);
        final TextView date_txt = findViewById(R.id.all_order_time);
        final TextView address = findViewById(R.id.fatora_address);

        quantity_txt.setText(total_quantity + "");
        price_txt.setText(total_price + " ريال ");
        date_txt.setText(date);
        state_txt.setText("قيد الانتظار");
        address.setText(address_val);

        fatora_recycler = findViewById(R.id.fatora_recycler);
        fatora_recycler.setLayoutManager(new LinearLayoutManager(Fatora.this));
        fatora_recycler.setHasFixedSize(true);

        //**************************
        StringRequest stringRequest = new StringRequest(Request.Method.POST, finished_orders, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    fatoraModels.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String product_name = jsonObject.getString("Product_Name");
                        String product_img = jsonObject.getString("Product_img");
                        String FoodType = jsonObject.getString("FoodType");
                        String Quantity = jsonObject.getString("Quantity");
                        String Price = jsonObject.getString("Price");
                        fatoraModels.add(new FatoraModel(product_name, product_img, FoodType, Quantity, Price));
                    }
                    adapter = new FatoraAdapter(Fatora.this, fatoraModels);
                    fatora_recycler.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                error.getMessage();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Customer_id", MainActivity.customer_id);
                Log.d("Customer_id", MainActivity.customer_id);
                params.put("Customer_name", MainActivity.Name);
                Log.d("Customer_name", MainActivity.Name);
                params.put("Customer_phone", MainActivity.phone);
                Log.d("Customer_phone", MainActivity.phone);
                params.put("Code", code);
                Log.d("Code", code);
                params.put("Lat", lat + "");
                Log.d("Lat", lat + "");
                params.put("Lan", lan + "");
                Log.d("Lan", lan + "");
                params.put("Address", address_val);
                Log.d("Address", address_val);
                params.put("TotalPrice", total_price + "");
                Log.d("TotalPrice", total_price + "");
                params.put("TotalQuantity", total_quantity + "");
                Log.d("TotalQuantity", total_quantity + "");
                params.put("Date", currentDateandTime);
                Log.d("Date", currentDateandTime);
                return params;
            }
        };
        Volley.newRequestQueue(Fatora.this).add(stringRequest);

    }

}