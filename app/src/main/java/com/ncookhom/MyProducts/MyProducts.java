package com.ncookhom.MyProducts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ncookhom.FamilyProducts.ProdImagesModel;
import com.ncookhom.MainActivity;
import com.ncookhom.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyProducts extends AppCompatActivity {

    private static final String products_url = "http://cookehome.com/CookApp/User/SearchProducts.php";
    ArrayList<ProdImagesModel> osra_images = new ArrayList<>();
    ArrayList<HomeModel> osra_data = new ArrayList<>();
    private AosraAdapter adapter;

    RecyclerView products_recycler;
    ProgressBar progressBar;
    TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_products);

        back = findViewById(R.id.back);
        progressBar = findViewById(R.id.main_progress);
        products_recycler = findViewById(R.id.products_recyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        products_recycler.setHasFixedSize(true);
        products_recycler.setLayoutManager(layoutManager);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        progressBar.setVisibility(View.VISIBLE);
        StringRequest products_request = new StringRequest(Request.Method.POST, products_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String product_id = jsonObject.getString("Product_ID");
                        String name = jsonObject.getString("Name");
                        String Price = jsonObject.getString("Price");
                        String rate = jsonObject.getString("rate");
                        String desc = jsonObject.getString("Description");
                        String time = jsonObject.getString("Timeee");
                        String img1 = jsonObject.getString("img");
                        String img2 = jsonObject.getString("img2");
                        String img3 = jsonObject.getString("img3");
                        String img4 = jsonObject.getString("img4");
                        String img5 = jsonObject.getString("img5");
                        String FoodType = jsonObject.getString("FoodType");
                        String customer_id = jsonObject.getString("Customer_id");
                        String customer_name = jsonObject.getString("customer_name");
                        String customer_mail = jsonObject.getString("customer_mail");

                        osra_images.add(new ProdImagesModel(img1, img2, img3, img4, img5));
                        osra_data.add(new HomeModel(img1, product_id, name, FoodType, rate, Price, desc, time, customer_id, customer_name, customer_mail));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (osra_data.size() > 0) {
                    progressBar.setVisibility(View.GONE);
                    adapter = new AosraAdapter(MyProducts.this, osra_data, osra_images);
                    products_recycler.setAdapter(adapter);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MyProducts.this, "لا توجد منتجات حالية", Toast.LENGTH_SHORT).show();
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
                params.put("Customer_id", MainActivity.customer_id);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(products_request);
    }
}