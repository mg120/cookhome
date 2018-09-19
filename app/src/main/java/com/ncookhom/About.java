package com.ncookhom;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class About extends AppCompatActivity {

    private TextView back, title_txt, desc_txt, alsalil_txt;
    private String about_url = "http://cookehome.com/CookApp/ShowData/Users/ShowAbout.php";

    NetworkAvailable networkAvailable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        networkAvailable = new NetworkAvailable(this);
        title_txt = findViewById(R.id.name_txt);
        desc_txt = findViewById(R.id.desc_txt);
        alsalil_txt = findViewById(R.id.alsalil_site);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        alsalil_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://www.alsalil.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        if (networkAvailable.isNetworkAvailable()) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, about_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String title = jsonObject.getString("name");
                            String desc = jsonObject.getString("describtion");

                            title_txt.setText(title);
                            desc_txt.setText(desc);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            RequestQueue queue = Volley.newRequestQueue(About.this);
            queue.add(stringRequest);
        } else {
            Toast.makeText(this, "خطأ فى الاتصال بالشبكة!", Toast.LENGTH_SHORT).show();
        }
    }
}