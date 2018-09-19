package com.ncookhom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ncookhom.FamilyProducts.FamilyProducts;
import com.squareup.picasso.Picasso;
import com.suke.widget.SwitchButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Settings extends AppCompatActivity {

    private String family_Data_url = "http://cookehome.com/CookApp/User/SearchUser.php";
    private String update_available_url = "http://cookehome.com/CookApp/Other/UpdateAvilable.php";
    NetworkAvailable networkAvailable;

    com.suke.widget.SwitchButton switchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        networkAvailable = new NetworkAvailable(this);
        TextView back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        switchButton = (com.suke.widget.SwitchButton) findViewById(R.id.switch_button);

        StringRequest family_data_request = new StringRequest(Request.Method.POST, family_Data_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        int available = jsonObject.getInt("available");

                        if (available == 1) {
                            switchButton.setChecked(true);
                        } else if (available == 0) {
                            switchButton.setChecked(false);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Settings.this, "لا يوجد اتصال بالانترنت", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ID", MainActivity.customer_id);
                return params;
            }
        };
        Volley.newRequestQueue(Settings.this).add(family_data_request);
//        Toast.makeText(this, "" + check, Toast.LENGTH_SHORT).show();
        Intent intent = getIntent();
        final String id = intent.getStringExtra("id");
//        switchButton.setChecked(true);
//        switchButton.isChecked();
//        switchButton.toggle();     //switch state
//        switchButton.toggle(false);//switch without animation
//        switchButton.setShadowEffect(true);//disable shadow effect
//        switchButton.setEnabled(false);//disable button
//        switchButton.setEnableEffect(false);//disable the switch animation
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
                if (isChecked) {
                    update_available(1 + "", id);
                    // Toast.makeText(Settings.this, "True", Toast.LENGTH_SHORT).show();
                } else {
                    update_available(0 + "", id);
                    //Toast.makeText(Settings.this, "False", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void update_available(final String availabe, final String id) {
        if (networkAvailable.isNetworkAvailable()) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, update_available_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int success = jsonObject.getInt("success");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("ID", id);
                    params.put("available", availabe);
                    return params;
                }
            };
            Volley.newRequestQueue(Settings.this).add(stringRequest);
        } else {
            Toast.makeText(this, "خطأ فى الاتصال بالشبكة!", Toast.LENGTH_SHORT).show();
        }
    }

}
