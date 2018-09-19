package com.ncookhom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ncookhom.NavFragments.ChatMessagesModel;
import com.ncookhom.NavFragments.MessagesRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.services.common.SafeToast;

public class Messages extends AppCompatActivity {

    MessagesRecyclerAdapter messagesRecyclerAdapter;
    RecyclerView messages_recycler;
    RecyclerView.LayoutManager layoutManager;
    TextView check_messages_txt, messages_back;
    FrameLayout messages_layout;

    String messages_url = "http://cookehome.com/CookApp/User/getmychat.php";
    List<ChatMessagesModel> list = new ArrayList<>();
    NetworkAvailable networkAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        messages_back = findViewById(R.id.messages_back);
        messages_recycler = findViewById(R.id.chat_messages_recycler);
        check_messages_txt = findViewById(R.id.check_msgs_txt);
        layoutManager = new LinearLayoutManager(Messages.this);

        messages_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final StringRequest messages_request = new StringRequest(Request.Method.POST, messages_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        boolean success = jsonObject.getBoolean("success");
                        String sender_id = jsonObject.getString("sender_id");
                        String sender_name = jsonObject.getString("sender_name");
                        String recive_id = jsonObject.getString("recive_id");
                        String recive_name = jsonObject.getString("recive_name");
                        String message = jsonObject.getString("message");
                        String date = jsonObject.getString("date");

                        list.add(new ChatMessagesModel(success, sender_id, sender_name, recive_id, message, recive_name, date));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (list.size() > 0) {
                    check_messages_txt.setVisibility(View.GONE);
                    messages_recycler.setLayoutManager(layoutManager);
                    messages_recycler.setHasFixedSize(true);
                    messagesRecyclerAdapter = new MessagesRecyclerAdapter(Messages.this, list);
                    messages_recycler.addItemDecoration(new DividerItemDecoration(Messages.this, DividerItemDecoration.VERTICAL));
                    messages_recycler.setAdapter(messagesRecyclerAdapter);
                } else {
                    messages_recycler.setVisibility(View.GONE);
                    check_messages_txt.setVisibility(View.VISIBLE);
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
                params.put("user_id", MainActivity.customer_id);
                return params;
            }
        };
        Volley.newRequestQueue(Messages.this).add(messages_request);
    }
}