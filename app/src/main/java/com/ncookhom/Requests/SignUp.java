package com.ncookhom.Requests;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.ncookhom.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SignUp extends AppCompatActivity {

    public static final int PICK_IMAGE = 100;
    private EditText name_ed, email_ed, pass_ed, country_ed, location_ed, phone_ed;
    private ImageView open_map;
    private Button create_account;
    private TextView have_account, open_location_map, select_location_layout;
    private TextView back;
    private ImageView profile_img;

    int PLACE_PICKER_REQUEST = 1;
    private GoogleApiClient mClient;
    double latitude, longtitude;
    String address, cityName, countryName, state;
    int spinner_selected_pos = 0;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        name_ed = findViewById(R.id.name_ed);
        name_ed.setTypeface(custom_font);
        email_ed = findViewById(R.id.signup_email);
        email_ed.setTypeface(custom_font);
        pass_ed = findViewById(R.id.signup_password);
        country_ed = findViewById(R.id.signup_country);
        phone_ed = findViewById(R.id.signup_phone);
        country_ed.setTypeface(custom_font);
        open_location_map = findViewById(R.id.select_location);
        open_map = findViewById(R.id.open_map_picker);
        create_account = findViewById(R.id.create_account);
        have_account = findViewById(R.id.have_account);
        have_account.setTypeface(custom_font);
        select_location_layout = findViewById(R.id.select_location_layout_txt);
        back = findViewById(R.id.back);
        profile_img = findViewById(R.id.signup_profile_image);

        Intent intent = getIntent();
        if (intent.getExtras().getString("email") != null) {
            email_ed.setText(intent.getStringExtra("email"));
            name_ed.setText(intent.getStringExtra("name"));
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        have_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, LogIn.class));
                finish();
            }
        });

        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        open_location_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(SignUp.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void register() {
        String userName = name_ed.getText().toString().trim();
        String email = email_ed.getText().toString().trim();
        String password = pass_ed.getText().toString().trim();
        String country = country_ed.getText().toString().trim();
        String phone = phone_ed.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            name_ed.setError("مطلوب");
            name_ed.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            email_ed.setError("مطلوب");
            email_ed.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_ed.setError("مطلوب");
            email_ed.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            pass_ed.setError("مطلوب");
            pass_ed.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            phone_ed.setError("مطلوب");
            phone_ed.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(country)) {
            country_ed.setError("مطلوب");
            country_ed.requestFocus();
            return;
        }

        //converting image to base64 string
        String imageString = "0";
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }
        final ProgressDialog progDailog = new ProgressDialog(SignUp.this);
        progDailog.setMessage("جارى التسجيل ...");
        progDailog.setProgress(0);
        progDailog.setMax(30);
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int progress = 0;
                while (progress <= 30) {
                    try {
                        progDailog.setProgress(progress);
                        progress++;
                        Thread.sleep(800);
                    } catch (Exception e) {

                    }
                }
                progDailog.dismiss();
                progDailog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                        builder.setMessage("تم التسجيل بنجاح")
                                .setCancelable(false)
                                .setNegativeButton("موافق", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startActivity(new Intent(SignUp.this, LogIn.class));
                                        finish();
                                    }
                                }).create().show();
                    }
                });
            }
        });
        thread.start();
        progDailog.show();

        // Now we have valid data ...
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int flag = jsonObject.getInt("flag");
                        Log.e("flag: ", flag + "");
                        Log.d("flag: ", flag + "");
                        if (flag == 1) {
//                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
//                            builder.setMessage("تم التسجيل بنجاح")
//                                    .setCancelable(false)
//                                    .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                                            dialogInterface.cancel();
//                                            startActivity(new Intent(SignUp.this, LogIn.class));
//                                            finish();
//                                        }
//                                    }).create().show();
                        } else if (flag == 0) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                            builder.setMessage("هذا الاسم او الايميل موجود بالفعل!")
                                    .setCancelable(false)
                                    .setPositiveButton("الغاء", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                            return;
                                        }
                                    }).create().show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        SignUpRequest signUpRequest = new SignUpRequest(userName, email, password, phone, imageString,
                latitude + "", longtitude + "", country, spinner_selected_pos + "", listener);
        RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
        requestQueue.add(signUpRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mClient.disconnect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            //TODO: action
            Uri file_path = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), file_path);
                profile_img.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 120, 120, false));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                address = String.format("%s", place.getAddress());
                latitude = place.getLatLng().latitude;
                longtitude = place.getLatLng().longitude;

                Geocoder gcd = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(latitude, longtitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses.size() > 0) {
                    cityName = addresses.get(0).getAddressLine(0);
                    countryName = addresses.get(0).getCountryName();
                    state = addresses.get(0).getAdminArea();
                    select_location_layout.setVisibility(View.VISIBLE);
                    select_location_layout.setText(addresses.get(0).getAddressLine(0));
                    String admin_adrea = addresses.get(0).getAdminArea();
//                     Toast.makeText(this, "locality: "+addresses.get(0).getLocality(), Toast.LENGTH_SHORT).show();
//                    country_ed.setText(countryName);
//                    Toast.makeText(this, "stat:"+stat + "\n"+ "cityName: " + cityName +"\n" + "countryName:" + countryName, Toast.LENGTH_LONG).show();
//                    Log.e("data:" , "stat:"+stat + "\n"+ "cityName: " + cityName +"\n" + "countryName:" + countryName);
//                    Log.e("local:" , addresses.get(0).getLocality() +"\n"+ "cityName1:" + cityName1);
                    // Toast.makeText(this, "country: " + countryName, Toast.LENGTH_SHORT).show();
                } else {
                    // do your stuff
                }
            }
        }
    }
}