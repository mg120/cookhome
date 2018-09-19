package com.ncookhom.NavFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.ncookhom.MainActivity;
import com.ncookhom.MyProducts.HomeModel;
import com.ncookhom.NetworkAvailable;
import com.ncookhom.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    boolean linear_layout;
    boolean grid_layout;
    private RecyclerView home_recycler;
    private HomeAdapter adapter;
    private LinearHomeAdapter linear_adapter;
    private ImageView linear_recycler, grid_recycler;
    final String[] address = new String[1];

    String current_url = "http://cookehome.com/CookApp/ShowData/Products/showProductsByName1.php";
    ArrayList<HomeModel> home_data = new ArrayList<>();
    ArrayList<ProdImagesModel> images_list = new ArrayList<>();
    private String meal_type = "http://cookehome.com/CookApp/ShowData/Users/showDepartments.php";
    private String horizontal_search_url = "http://cookehome.com/CookApp/User/SearchProductAll.php/";

    private String food_type_url = "http://cookehome.com/CookApp/ShowData/Products/showProductsByFoodType.php";
    private String name1_url = "http://cookehome.com/CookApp/ShowData/Products/showProductsByName2.php ";
    private String name2_url = "http://cookehome.com/CookApp/ShowData/Products/showProductsByName1.php";
    private String osra_name_url = "http://cookehome.com/CookApp/ShowData/Products/showProductsByOsraName.php";
    private String nearby_url = "http://cookehome.com/CookApp/ShowData/Products/show.php";
    private String price1_url = "http://cookehome.com/CookApp/ShowData/Products/showProductsByPrice1.php";
    private String price2_url = "http://cookehome.com/CookApp/ShowData/Products/showProductsByPrice2.php";
    private String selected_item;

    private String family_Data_url = "http://cookehome.com/CookApp/User/SearchUser.php";

    // horizontal scroll view ....
    ArrayList<String> mm = new ArrayList<>();
    LinearLayout myLinearLayout;

    ProgressBar progressBar;
    NetworkAvailable networkAvailable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("الرئيسية");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (MainFragment.selected_item != null) {
//            selected_item = getArguments().getString("selected_item");
            if (MainFragment.selected_item.equals("اسم الطبخة")) {
                current_url = food_type_url;
            } else if (MainFragment.selected_item.equals("أ - ى")) {
                current_url = name1_url;
            } else if (MainFragment.selected_item.equals("ى - أ")) {
                current_url = name2_url;
            } else if (MainFragment.selected_item.equals("اسم الاسرة")) {
                current_url = osra_name_url;
            } else if (MainFragment.selected_item.equals("اعلى سعر")) {
                current_url = price1_url;
            } else if (MainFragment.selected_item.equals("اقل سعر")) {
                current_url = price2_url;
            } else if (MainFragment.selected_item.equals("الاقرب اليك")) {
                current_url = nearby_url;
            }
        }
        // Linear in HorizontalView
        myLinearLayout = getActivity().findViewById(R.id.myLinearLayout);

        //RecyclerView
        progressBar = getActivity().findViewById(R.id.main_progress);

        home_recycler = getActivity().findViewById(R.id.home_recyclerView);
        home_recycler.setLayoutManager(null);
        home_recycler.setAdapter(null);
        home_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        home_recycler.setHasFixedSize(true);
        adapter = new HomeAdapter(getActivity(), home_data, images_list);
        home_recycler.setAdapter(adapter);
        linear_layout = false;
        grid_layout = true;
        HorizontalView_Data();

        loadhomeData();

        // Reference to Linear and Grid Layout of Recycler
        linear_recycler = getActivity().findViewById(R.id.linear_recycler_img);
        grid_recycler = getActivity().findViewById(R.id.grid_recycler_img);

        linear_recycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (linear_layout == false) {
//                    home_recycler.setLayoutManager(null);
//                    home_recycler.setAdapter(null);
                    home_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
//                    home_recycler.setHasFixedSize(true);
//                    linear_adapter = new LinearHomeAdapter(getActivity(), home_data, images_list);
//                    home_recycler.setAdapter(linear_adapter);
                    linear_layout = true;
                    grid_layout = false;
                }
            }
        });

        grid_recycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (grid_layout == false) {
//                    home_recycler.setLayoutManager(null);
//                    home_recycler.setAdapter(null);
                    home_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//                    home_recycler.setHasFixedSize(true);
//                    adapter = new HomeAdapter(getActivity(), home_data, images_list);
//                    home_recycler.setAdapter(adapter);
                    grid_layout = true;
                    linear_layout = false;
                }
            }
        });
    }

    private void loadhomeData() {
        home_data.clear();
        images_list.clear();
        progressBar.setVisibility(View.VISIBLE);
        StringRequest home_request = new StringRequest(Request.Method.GET, current_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        final String product_id = jsonObject.getString("Product_ID");
                        final String name = jsonObject.getString("Name");
                        final String Price = jsonObject.getString("Price");
                        final String rate = jsonObject.getString("star");
                        final String desc = jsonObject.getString("Description");
                        final String time = jsonObject.getString("Timeee");
                        final String FoodType = jsonObject.getString("FoodType");
                        final String img1 = jsonObject.getString("img");
                        final String img2 = jsonObject.getString("img2");
                        final String img3 = jsonObject.getString("img3");
                        final String img4 = jsonObject.getString("img4");
                        final String img5 = jsonObject.getString("img5");
                        final String seller_id = jsonObject.getString("Customer_id");
                        final String seller_name = jsonObject.getString("customer_name");
                        final String seller_mail = jsonObject.getString("customer_mail");
                        StringRequest family_data_request = new StringRequest(Request.Method.POST, family_Data_url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if (success) {
                                        address[0] = jsonObject.getString("Address");
                                    }
                                    if (!MainActivity.customer_id.equals(seller_id)) {
                                        images_list.add(new ProdImagesModel(img1, img2, img3, img4, img5));
                                        home_data.add(new HomeModel(img1, product_id, name, FoodType, rate, Price, desc, time, seller_id, seller_name, seller_mail, address[0]));
                                    }
                                    if (home_data.size() > 0) {
//                                            home_recycler.setAdapter(null);
//                                            home_recycler.setLayoutManager(null);
                                        progressBar.setVisibility(View.GONE);
//                                            home_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//                                            home_recycler.setHasFixedSize(true);
//                                            adapter = new HomeAdapter(getActivity(), home_data, images_list);
//                                            home_recycler.setAdapter(adapter);
                                        adapter.notifyItemRangeInserted(adapter.getItemCount(), home_data.size());
//                                            adapter.notifyDataSetChanged();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                Toast.makeText(getActivity(), "خطأ فى الاتصال بالشبكة!", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("ID", seller_id);
                                return params;
                            }
                        };
                        Volley.newRequestQueue(getActivity()).add(family_data_request);
                    }
                } catch (
                        JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        Volley.newRequestQueue(getActivity()).add(home_request);
    }

    public void HorizontalView_Data() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, meal_type, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    mm.add("الكل");
                    JSONArray jsonArray = new JSONArray(response);
                    for (int x = 0; x < jsonArray.length(); x++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(x);
                        String mea_name = jsonObject.getString("name");

                        mm.add(mea_name);

                    }
                    final int size = mm.size();
                    TextView[] tv = new TextView[size];
                    TextView temp;
                    for (int i = 0; i < size; i++) {
                        temp = new TextView(getActivity());
                        temp.setText("   " + mm.get(i).toString() + "   "); //arbitrary task
                        temp.setTextSize(16.2f);
                        final int finalI1 = i;
                        temp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (mm.get(finalI1).equals("الكل")) {
                                    loadhomeData();
                                } else {
                                    home_recycler.setAdapter(null);
                                    home_data.clear();
                                    images_list.clear();
                                    horizontal_search_filter(mm.get(finalI1));
                                }
                            }
                        });
                        myLinearLayout.addView(temp);
                        tv[i] = temp;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(), "خطأ فى الاتصال الشبكة!", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    public void horizontal_search_filter(final String meal_name_filter) {

        home_data.clear();
        images_list.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest home_request = new StringRequest(Request.Method.POST, horizontal_search_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        final boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            final String product_id = jsonObject.getString("Product_ID");
                            final String name = jsonObject.getString("Name");
                            final String Price = jsonObject.getString("Price");
                            final String rate = jsonObject.getString("rate");
                            final String desc = jsonObject.getString("Description");
                            final String time = jsonObject.getString("Timeee");
                            final String img1 = jsonObject.getString("img");
                            final String img2 = jsonObject.getString("img2");
                            final String img3 = jsonObject.getString("img3");
                            final String img4 = jsonObject.getString("img4");
                            final String img5 = jsonObject.getString("img5");
                            final String FoodType = jsonObject.getString("FoodType");
                            String ready = jsonObject.getString("ready");
                            final String seller_id = jsonObject.getString("Customer_id");
                            final String seller_name = jsonObject.getString("customer_name");
                            final String seller_mail = jsonObject.getString("customer_mail");

                            StringRequest family_data_request = new StringRequest(Request.Method.POST, family_Data_url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        boolean success = jsonObject.getBoolean("success");
                                        if (success) {
                                            address[0] = jsonObject.getString("Address");
                                        }
                                        if (!MainActivity.customer_id.equals(seller_id)) {
                                            home_recycler.setAdapter(null);
                                            images_list.add(new ProdImagesModel(img1, img2, img3, img4, img5));
                                            home_data.add(new HomeModel(img1, product_id, name, FoodType, rate, Price, desc, time, seller_id, seller_name, seller_mail, address[0]));
                                            progressBar.setVisibility(View.GONE);
                                            adapter = new HomeAdapter(getActivity(), home_data, images_list);
                                            home_recycler.setAdapter(adapter);

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
                                    params.put("ID", seller_id);
                                    return params;
                                }
                            };
                            Volley.newRequestQueue(getActivity()).add(family_data_request);

                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "لا توجد منتجات حالية", Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = new HashMap<>();
                params.put("gsearch", meal_name_filter);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(home_request);
    }
}