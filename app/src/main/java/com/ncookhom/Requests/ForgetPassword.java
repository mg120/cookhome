package com.ncookhom.Requests;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ncookhom.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetPassword extends AppCompatActivity {

    private String reset_pass_url = "http://cookehome.com/CookApp/User/ForgetPassword.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        final EditText pass_ed = (EditText) findViewById(R.id.reset_pass_email);
        Button reset_pass_btn = (Button) findViewById(R.id.reset_pass_btn);

        reset_pass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String user_email = pass_ed.getText().toString().trim();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, reset_pass_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success  = jsonObject.getBoolean("success");
                            if (success){
                                AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPassword.this);
                                builder.setMessage("تم ارسال كلمة المرور الى بريدك الالكترونى")
                                        .setCancelable(false)
                                        .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                                finish();
                                            }
                                        }).create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ForgetPassword.this, "Error Connection", Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> params = new HashMap<>();
                        params.put("Email", user_email);
                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(ForgetPassword.this);
                queue.add(stringRequest);
            }
        });

    }
}
