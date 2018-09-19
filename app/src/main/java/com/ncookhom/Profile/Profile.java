package com.ncookhom.Profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.ncookhom.MainActivity;
import com.ncookhom.NetworkAvailable;
import com.ncookhom.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class Profile extends AppCompatActivity {


    private TextView back;
    private EditText profile_name_ed, profile_email_ed, profile_pass_ed;
    private Button update_prifile;
    private TextView profile_customer_id;
    private ImageView profile_img;
    String customer_id, username, email, address, type, image;

    SharedPreferences.Editor user_data_edito;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    public static final String MY_PREFS_NAME = "all_user_data";
    public static final String MY_PREFS_login = "login_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        back = findViewById(R.id.back);
        profile_customer_id = findViewById(R.id.profile_id_txt);
        profile_name_ed = findViewById(R.id.profile_name_ed);
        profile_email_ed = findViewById(R.id.profile_email_ed);
        profile_pass_ed = findViewById(R.id.profile_password_ed);
        update_prifile = findViewById(R.id.update_profile);
        profile_img = findViewById(R.id.profile_image);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // get Intent to get data from it ...
        final Intent intent = getIntent();
        if (intent.hasExtra("userName")) {
            profile_name_ed.setText(intent.getStringExtra("userName"));
            profile_email_ed.setText(intent.getStringExtra("email"));
            profile_pass_ed.setText(intent.getStringExtra("password"));
            Picasso.with(Profile.this).load("http://cookehome.com/CookApp/images/" + intent.getStringExtra("img")).into(profile_img);
        }

        update_prifile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String N_username = profile_name_ed.getText().toString().trim();
                String N_email = profile_email_ed.getText().toString().trim();
                String N_pass = profile_pass_ed.getText().toString().trim();

                if (TextUtils.isEmpty(N_username) && N_username.equals("")) {
                    profile_customer_id.setError("مطلوب");
                    profile_customer_id.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(N_email) && N_email.equals("")) {
                    profile_email_ed.setError("مطلوب");
                    profile_email_ed.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(N_pass) && N_pass.equals("")) {
                    profile_pass_ed.setError("مطلوب");
                    profile_pass_ed.requestFocus();
                    return;
                }
                // update request ....
                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            final String id = jsonObject.getString("ID");
                            final String name = jsonObject.getString("Name");
                            final String email = jsonObject.getString("Email");
                            final String password = jsonObject.getString("Password");
                            final String Phone = jsonObject.getString("Phone");
                            final String img = jsonObject.getString("img");
                            final String Lat = jsonObject.getString("Lat");
                            final String Lan = jsonObject.getString("Lan");
                            final String Address = jsonObject.getString("Address");
                            final int available = Integer.parseInt(jsonObject.getString("available"));
                            final int type = Integer.parseInt(jsonObject.getString("type"));

                            AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
                            builder.setMessage("تم تحديث البيانات")
                                    .setCancelable(false)
                                    .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent1 = new Intent(Profile.this, MainActivity.class);
                                            intent1.putExtra("customer_id", id);
                                            intent1.putExtra("email", email);
                                            intent1.putExtra("Name", name);
                                            intent1.putExtra("password", password);
                                            intent1.putExtra("img", img);
                                            intent1.putExtra("Address", Address);
                                            intent1.putExtra("available", available);
                                            intent1.putExtra("type", type);
                                            startActivity(intent1);
                                            //   get data from shared preferences ...
                                            sharedPreferences = getSharedPreferences(MY_PREFS_login, MODE_PRIVATE);
                                            editor = sharedPreferences.edit();
                                            editor.putString("email", email);
                                            editor.putString("password", password);
                                            editor.commit();
                                            editor.apply();

                                            user_data_edito = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                            user_data_edito.putString("user_id", id);
                                            user_data_edito.putString("email", email);
                                            user_data_edito.putString("password", password);
                                            user_data_edito.putString("userName", name);
                                            user_data_edito.putString("address", address);
                                            user_data_edito.putInt("available", available);
                                            user_data_edito.putString("phone", Phone);
                                            user_data_edito.putString("img", img);
                                            user_data_edito.putString("type", type + "");
                                            user_data_edito.commit();
                                            user_data_edito.apply();
                                            dialogInterface.dismiss();
                                            finish();
                                        }
                                    }).create().show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest(MainActivity.customer_id, N_username, N_email, N_pass, listener);
                RequestQueue queue = Volley.newRequestQueue(Profile.this);
                queue.add(updateProfileRequest);
            }
        });
    }
}