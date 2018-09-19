package com.ncookhom.ProductDetails;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ncookhom.MainActivity;
import com.ncookhom.MyProducts.HomeModel;
import com.ncookhom.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RateProduct extends AppCompatActivity {

    private Button submit_rate;
    private RatingBar add_family_rate;
    private EditText add_comment;
    private TextView back;
    HomeModel prod_data;

    Float rate_num;
    String comment;
    private String rate_url = "http://cookehome.com/CookApp/User/Rate.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_product);

        final Intent intent = getIntent();
        if (intent.getExtras() != null) {
            prod_data = intent.getParcelableExtra("prod_data");
        }

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        back = findViewById(R.id.back);
        submit_rate = findViewById(R.id.submit_rate);
        add_comment = findViewById(R.id.rate_comment_ed);
        add_family_rate = findViewById(R.id.make_rate);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submit_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rate_num = add_family_rate.getRating();
                comment = add_comment.getText().toString().trim();
                if (rate_num == 0.0) {
                    Toast.makeText(RateProduct.this, "برجاء تقييم المنتج!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(comment)) {
                    add_comment.setError("برجاء كتابة التعليق اولا!");
                    add_comment.requestFocus();
                    return;
                } else {
                    StringRequest rate_request = new StringRequest(Request.Method.POST, rate_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int flag = jsonObject.getInt("flag");
                                    if (flag == 1) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(RateProduct.this);
                                        builder.setMessage("تم تقيم المنتج بنجاح")
                                                .setCancelable(false)
                                                .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.cancel();
                                                    }
                                                }).create().show();
                                    } else if (flag == 0) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(RateProduct.this);
                                        builder.setMessage("تم تقيم هذا المنتج من قبل")
                                                .setCancelable(false)
                                                .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.cancel();
                                                    }
                                                }).create().show();
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
                            error.getMessage();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("product_id", prod_data.getFamily_id());
                            params.put("rate", rate_num + "");
                            params.put("user_id", MainActivity.customer_id);
                            params.put("user_name", MainActivity.Name);
                            params.put("user_mail", MainActivity.email);
                            params.put("comment", comment);
                            return params;
                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(RateProduct.this);
                    queue.add(rate_request);
                }
            }
        });


    }
}