package com.ncookhom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.ncookhom.Search.SearchProductsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private EditText search_ed;
    private ImageView search_img;
    private Button seach_btn;
    private TextView back;

    private ListView listView;
    private SearchProductsAdapter productsAdapter;
    ArrayList<ProdImagesModel> search_prod_images = new ArrayList<>();
    ArrayList<HomeModel> search_prod_data = new ArrayList<>();
    String search_url = "http://cookehome.com/CookApp/User/SearchProductAll.php/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final NetworkAvailable networkAvailable = new NetworkAvailable(this);

        search_ed = findViewById(R.id.home_search_ed);
        search_img = findViewById(R.id.home_search_img);
        seach_btn = findViewById(R.id.seach_btn);
        back = findViewById(R.id.back);
        listView = findViewById(R.id.search_res_list);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        seach_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String text = search_ed.getText().toString().trim();
                if (text != null && !text.equals("")) {
//                    Intent intent = new Intent(getActivity(), SearchProducts.class);
//                    intent.putExtra("search_text", text);
//                    startActivity(intent);
                    search_prod_data.clear();
                    search_prod_images.clear();
                    listView.setAdapter(null);
                    if (networkAvailable.isNetworkAvailable()) {
                        RequestQueue requestQueue = Volley.newRequestQueue(SearchActivity.this);
                        StringRequest search_request = new StringRequest(Request.Method.POST, search_url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        boolean success = jsonObject.getBoolean("success");
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
                                            search_prod_images.add(new ProdImagesModel(img, img2, img3, img4, img5));
                                            search_prod_data.add(new HomeModel(img, product_id, Name, food_type, rate, price, desc, time, Customer_id, customer_name, customer_mail));
                                        }
                                    }

                                    if (!search_prod_data.isEmpty()) {
                                        productsAdapter = new SearchProductsAdapter(SearchActivity.this, search_prod_data);
                                        listView.setAdapter(productsAdapter);
                                    } else {
                                        Toast.makeText(SearchActivity.this, "لا توجد منتجات بهذا الأسم", Toast.LENGTH_SHORT).show();
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
                                params.put("gsearch", text);
                                return params;
                            }
                        };
                        requestQueue.add(search_request);
                    } else {
                        Toast.makeText(SearchActivity.this, "خطأ فى الاتصال بالشبكة!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HomeModel familyProductsModel = search_prod_data.get(i);
                ProdImagesModel imagesModel = search_prod_images.get(i);
                Intent intent1 = new Intent(SearchActivity.this, ProductDetails.class);
                intent1.putExtra("product_data", familyProductsModel);
                intent1.putExtra("Imageslist", imagesModel);
                startActivity(intent1);
            }
        });
//        search_ed.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                filter(editable.toString());
//            }
//        });

    }

    private void filter(String newText) {
        //new array list that will hold the filtered data
        ArrayList<HomeModel> filterdNames = new ArrayList<>();

        //looping through existing elements
//        for (HomeModel item : home_data) {
//            //if the existing elements contains the search input
//            if (item.getFamily_name().toLowerCase().contains(newText.toLowerCase())) {
//                //adding the element to filtered list
//                filterdNames.add(item);
//            }
//        }
//
//        //calling a method of the adapter class and passing the filtered list
//        adapter.filterList(filterdNames);
    }
}
