package com.ncookhom.NavFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.ncookhom.FamilyProducts.FamilyProductsAdapter;
import com.ncookhom.FamilyProducts.ProdImagesModel;
import com.ncookhom.MainActivity;
import com.ncookhom.MyOrders.Orders;
import com.ncookhom.MyOrders.OrdersAdapter;
import com.ncookhom.MyOrders.OrdersModel;
import com.ncookhom.MyOrders.OrdersPagerAdapter;
import com.ncookhom.MyProducts.HomeModel;
import com.ncookhom.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class LastDealsFragment extends Fragment {

    String family_id;

    private String orders_url = "http://cookehome.com/CookApp/User/SearchMyOrders.php";
    private ArrayList<OrdersModel> last_orders_list = new ArrayList<>();
    private DealsAdapter adapter;
    ListView deals_listview;
    TextView check_products_txt ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_last_deals, container, false);
        check_products_txt = view.findViewById(R.id.check_products_txt);
        deals_listview = view.findViewById(R.id.family_deals_list);
        if (getArguments().getString("family_id") != null) {
            family_id = getArguments().getString("family_id");
            getlastOrders(family_id);
            check_products_txt.setVisibility(View.GONE);
        } else {
            deals_listview.setVisibility(View.GONE);
            check_products_txt.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private void getlastOrders(final String family_id) {
        StringRequest orders_request = new StringRequest(Request.Method.POST, orders_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    last_orders_list.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            String Order_id = jsonObject.getString("Order_id");
                            String Product_Name = jsonObject.getString("Product_Name");
                            String Product_img = jsonObject.getString("Product_img");
                            String foodType = jsonObject.getString("FoodType");
                            String Quantity = jsonObject.getString("Quantity");
                            String Price = jsonObject.getString("Price");
                            String Seller_id = jsonObject.getString("Seller_id");
                            String Seller_name = jsonObject.getString("Seller_name");
                            String Seller_mail = jsonObject.getString("Seller_mail");
                            String Customer_id = jsonObject.getString("Customer_id");
                            String Customer_name = jsonObject.getString("Customer_name");
                            String Customer_mail = jsonObject.getString("Customer_mail");
                            String Customer_phone = jsonObject.getString("Customer_phone");
                            String state_type = jsonObject.getString("type");

                            if (family_id.equals(Seller_id)) {
                                last_orders_list.add(new OrdersModel(Product_img, Product_Name, foodType, Price, Quantity, Order_id, state_type, Seller_id,
                                        Seller_name, Seller_mail, Customer_id, Customer_name, Customer_mail, Customer_phone));

                            }
                        }
                        adapter = new DealsAdapter(getActivity(), last_orders_list);
                        deals_listview.setAdapter(adapter);
                    }
                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            },new Response.ErrorListener()

            {
                @Override
                public void onErrorResponse (VolleyError error){
                error.printStackTrace();
            }
            })

            {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Customer_id", MainActivity.customer_id);
                return params;
            }
            }

            ;
        Volley.newRequestQueue(

            getActivity()).

            add(orders_request);
        }
    }