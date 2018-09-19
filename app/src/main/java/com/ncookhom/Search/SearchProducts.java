package com.ncookhom.Search;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.ncookhom.FamilyProducts.ProdImagesModel;
import com.ncookhom.MyProducts.HomeModel;
import com.ncookhom.ProductDetails.ProductDetails;
import com.ncookhom.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchProducts extends AppCompatActivity {

    private TextView back ;
    private ListView listView ;
    private SearchProductsAdapter productsAdapter ;

    ArrayList<ProdImagesModel> search_prod_images = new ArrayList<>();
    ArrayList<HomeModel> search_prod_data = new ArrayList<>();
    String search_url = "http://cookehome.com/CookApp/User/SearchProductAll.php/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_res);

        Intent intent = getIntent();

        final String search_text = intent.getStringExtra("search_text");

        back =  findViewById(R.id.back);
        listView =  findViewById(R.id.orders_list);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              list.clear();
                finish();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest search_request = new StringRequest(Request.Method.POST, search_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        boolean success = jsonObject.getBoolean("success");
//                        Toast.makeText(SearchProducts.this, "success:" + success, Toast.LENGTH_SHORT).show();
                        if (success) {
                            String product_id = jsonObject.getString("Product_ID");
                            String Name = jsonObject.getString("Name");
                            String img = jsonObject.getString("img");
                            String img2 = jsonObject.getString("img2");
                            String img3 = jsonObject.getString("img3");
                            String img4 = jsonObject.getString("img4");
                            String img5 = jsonObject.getString("img5");
                            String food_type = jsonObject.getString("FoodType");
                            String time = jsonObject.getString("Timeee");
                            String price = jsonObject.getString("Price");
                            String desc = jsonObject.getString("Description");
                            String Customer_id = jsonObject.getString("Customer_id");
                            String customer_name = jsonObject.getString("customer_name");
                            String customer_mail = jsonObject.getString("customer_mail");
                            String rate = jsonObject.getString("rate");
                            search_prod_data.clear();
                            search_prod_images.clear();
                            search_prod_images.add(new ProdImagesModel(img, img2, img3, img4, img5));
                            search_prod_data.add(new HomeModel(img, product_id, Name, food_type, rate, price, desc,  time, Customer_id, customer_name, customer_mail));
                        }
                    }

                    if (!search_prod_data.isEmpty()) {
                        productsAdapter = new SearchProductsAdapter(SearchProducts.this, search_prod_data);
                        listView.setAdapter(productsAdapter);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SearchProducts.this);
                        builder.setMessage("لا يوجد منتجات بهذا الاسم")
                                .setCancelable(false)
                                .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                }).create().show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchProducts.this, "Error Conection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("gsearch", search_text);
                return params;
            }
           };
         requestQueue.add(search_request);

         listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 HomeModel familyProductsModel = search_prod_data.get(i);
                 ProdImagesModel imagesModel = search_prod_images.get(i);
                 Intent intent1 = new Intent(SearchProducts.this, ProductDetails.class);
                 intent1.putExtra("product_data" , familyProductsModel);
                 intent1.putExtra("Imageslist" ,imagesModel);
                 startActivity(intent1);
             }
         });

    }

}

