package com.ncookhom.ProductDetails;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ncookhom.MyProducts.HomeModel;
import com.ncookhom.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Comments extends AppCompatActivity {

    private String comments_url = "http://cookehome.com/CookApp/User/getcomments.php";
    private RecyclerView commente_recycler;
    RecyclerView.LayoutManager layoutManager;
    CommentAdapter commentAdapter;
    ArrayList<CommentsModel> comments_list = new ArrayList<>();
    private String product_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        TextView back = findViewById(R.id.comments_back);
        commente_recycler = findViewById(R.id.comments_recycler_id);
        layoutManager = new LinearLayoutManager(this);
        commente_recycler.setLayoutManager(layoutManager);
        commente_recycler.addItemDecoration(new DividerItemDecoration(Comments.this, DividerItemDecoration.VERTICAL));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (getIntent().getExtras() != null) {
            product_id = getIntent().getExtras().getString("product_id");
            Toast.makeText(this, "" + product_id, Toast.LENGTH_SHORT).show();
        }
        StringRequest comments_request = new StringRequest(Request.Method.POST, comments_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String prod_id = jsonObject.getString("product_id");
                        String name = jsonObject.getString("name");
                        String comment = jsonObject.getString("comment");
                        String rate = jsonObject.getString("rate");

                        comments_list.add(new CommentsModel(name, comment, rate));
                    }
                    commentAdapter = new CommentAdapter(Comments.this, comments_list);
                    commente_recycler.setAdapter(commentAdapter);
                    Toast.makeText(Comments.this, "" + comments_list.size(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(Comments.this, "لا يوجد اتصال بالانترنت", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("product_id", product_id);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(comments_request);
    }
}