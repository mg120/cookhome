package com.ncookhom.Card;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ncookhom.MainActivity;
import com.ncookhom.R;
import com.ncookhom.ShippingAddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.services.common.SafeToast;

public class MyCard extends AppCompatActivity {

    ListView card_list;
    private CardAdapter card_adapter;
    private LinearLayout total_layout;
    public static LinearLayout no_products_layout, card_products_layout;
    private TextView discover_products;
    public static TextView total_tv;
    private ArrayList<CardModel> list = new ArrayList<>();

    private ArrayList<CardModel> new_list = new ArrayList<>();
    float total = 0.0f;

    private String card_url = "http://cookehome.com/CookApp/User/SearchBasket.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card);

        final ProgressBar progressBar = findViewById(R.id.card_progress);
        TextView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        card_list = findViewById(R.id.card_list);
        total_tv = findViewById(R.id.total_price_tv);
        final Button sure_add_order = findViewById(R.id.sure_add_order);
        total_layout = findViewById(R.id.total_layout);
        no_products_layout = findViewById(R.id.no_products);
        card_products_layout = findViewById(R.id.card_prods_layout);
        discover_products = findViewById(R.id.discover_products);

        discover_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        StringRequest card_request = new StringRequest(Request.Method.POST, card_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressBar.setVisibility(View.VISIBLE);
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            String order_id = jsonObject.getString("Order_id");
                            String seller_id = jsonObject.getString("Seller_id");
                            String Product_Name = jsonObject.getString("Product_Name");
                            String Quantity = jsonObject.getString("Quantity");
                            String Price = jsonObject.getString("Price");
                            String type = jsonObject.getString("type");
                            String Product_img = jsonObject.getString("Product_img");

                            total += Float.parseFloat(Price) * Integer.parseInt(Quantity);

                            list.add(new CardModel(seller_id, order_id, Product_img, Product_Name, Price, Quantity));
                        }
                    }

                    if (!list.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        no_products_layout.setVisibility(View.INVISIBLE);
                        card_products_layout.setVisibility(View.VISIBLE);

                        card_adapter = new CardAdapter(MyCard.this, list, total);
                        card_list.setAdapter(card_adapter);

                    } else {
                        progressBar.setVisibility(View.GONE);
                        no_products_layout.setVisibility(View.VISIBLE);
                        card_products_layout.setVisibility(View.INVISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
//                Toast.makeText(MyCard.this, "Error connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Customer_id", MainActivity.customer_id);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(card_request);

        sure_add_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCard.this, ShippingAddress.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("card_list", CardAdapter.list);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }
}