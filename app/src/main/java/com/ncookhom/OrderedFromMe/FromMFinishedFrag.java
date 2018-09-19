package com.ncookhom.OrderedFromMe;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.ncookhom.Chat.Chat;
import com.ncookhom.Chat.ChatModel;
import com.ncookhom.FamilyProducts.FamilyProducts;
import com.ncookhom.MainActivity;
import com.ncookhom.MyOrders.OrdersAdapter;
import com.ncookhom.MyOrders.OrdersModel;
import com.ncookhom.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FromMFinishedFrag extends Fragment {

    private String open_chat_url = "http://cookehome.com/CookApp/getChat.php";
    private String Lat, Lan, email, Name, sender_name, recive_name;
    String fromm_orders_url = "http://cookehome.com/CookApp/User/SearchSellerOrders.php";
    private String update_order_type = "http://cookehome.com/CookApp/Other/UpdateType.php";

    public static TextView check_txt;
    public static RecyclerView finished_recycler;
    RecyclerView.LayoutManager layoutManager;
    FinishedAdapter adapter;

    ArrayList<OrdersModel> finished_list;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            finished_list = getArguments().getParcelableArrayList("finished_list");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_from_mfinished, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        check_txt = getActivity().findViewById(R.id.from_finished_check_txt);
        finished_recycler = getActivity().findViewById(R.id.frag_finished_recycler);
        layoutManager = new LinearLayoutManager(getActivity());

        if (finished_list.size() > 0) {
            check_txt.setVisibility(View.GONE);
            finished_recycler.setVisibility(View.VISIBLE);
            finished_recycler.setLayoutManager(layoutManager);
            finished_recycler.setHasFixedSize(true);
            finished_recycler.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

            adapter = new FinishedAdapter(getActivity(), finished_list);
            finished_recycler.setAdapter(adapter);
        } else {
            finished_recycler.setVisibility(View.GONE);
            check_txt.setVisibility(View.VISIBLE);
        }
    }
}