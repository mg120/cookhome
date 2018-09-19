package com.ncookhom.NavFragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.ncookhom.FamilyProducts.FamilyProducts;
import com.ncookhom.FamilyProducts.FamilyProductsAdapter;
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

public class FamilyProductsFragment extends Fragment {

    String products_url = "http://cookehome.com/CookApp/User/SearchProducts.php";
    TextView check_txt;
    ListView products_list;
    FamilyProductsAdapter adapter;
    String family_id;
    private ArrayList<HomeModel> prod_data_list = new ArrayList<>();
    private ArrayList<ProdImagesModel> prod_imgs_list = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_family_products, container, false);
        check_txt = view.findViewById(R.id.check_products_txt);
        products_list = view.findViewById(R.id.family_products_list);
        if (getArguments().getString("family_id") != null) {
            family_id = getArguments().getString("family_id");
            loadFamilyproducts(family_id);
            check_txt.setVisibility(View.GONE);
        } else {
            products_list.setVisibility(View.GONE);
            check_txt.setVisibility(View.VISIBLE);
        }
        // Inflate the layout for this fragment
        return view;
    }

    private void loadFamilyproducts(final String family_id) {

        StringRequest products_request = new StringRequest(Request.Method.POST, products_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
//                    Toast.makeText(FamilyProducts.this, "rrrrr", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data_obj = jsonArray.getJSONObject(i);
                        String Product_ID = data_obj.getString("Product_ID");
                        String Product_Name = data_obj.getString("Name");
                        String Product_img = data_obj.getString("img");
                        String Product_img2 = data_obj.getString("img2");
                        String Product_img3 = data_obj.getString("img3");
                        String Product_img4 = data_obj.getString("img4");
                        String Product_img5 = data_obj.getString("img5");
                        String foodType = data_obj.getString("FoodType");
                        String time = data_obj.getString("Timeee");
                        String Product_desc = data_obj.getString("Description");
                        String Product_rate = data_obj.getString("rate");
                        String Product_price = data_obj.getString("Price");
                        String seller_id = data_obj.getString("Customer_id");
                        String seller_name = data_obj.getString("customer_name");
                        String seller_email = data_obj.getString("customer_mail");

//                        Toast.makeText(FamilyProducts.this, "" + Product_Name, Toast.LENGTH_SHORT).show();
                        prod_imgs_list.add(new ProdImagesModel(Product_img, Product_img2, Product_img3, Product_img4, Product_img5));
                        prod_data_list.add(new HomeModel(Product_img, Product_ID, Product_Name, foodType, Product_rate, Product_price, Product_desc, time, seller_id, seller_name, seller_email));
                    }
                    if (prod_data_list.size() > 0) {
                        adapter = new FamilyProductsAdapter(getActivity(), prod_data_list);
                        products_list.setAdapter(adapter);
                    }
                    products_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Intent intent1 = new Intent(getActivity(), ProductDetails.class);
                            intent1.putExtra("Imageslist", prod_imgs_list.get(i));
                            intent1.putExtra("product_data", prod_data_list.get(i));
                            startActivity(intent1);
                        }
                    });


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
                params.put("Customer_id", family_id);
                return params;
            }
        };
        RequestQueue products_queue = Volley.newRequestQueue(getActivity());
        products_queue.add(products_request);
    }
}