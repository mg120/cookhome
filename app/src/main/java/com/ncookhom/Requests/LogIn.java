package com.ncookhom.Requests;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ncookhom.FCMRegistrationService;
import com.ncookhom.MainActivity;
import com.ncookhom.NetworkAvailable;
import com.ncookhom.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class LogIn extends AppCompatActivity {

    // google and facebook login buttons ...
    LoginButton fb_login_button;
    GoogleSignInClient mGoogleSignInClient;
    CallbackManager callbackManager;
    // --------------------------------------------
    private EditText login_email_ed, login_pass_ed;
    private Button login_btn;
    private TextView back, forget_pass;
    // put data to shared preferences ...
    SharedPreferences.Editor user_data_edito;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    public static final String MY_PREFS_NAME = "all_user_data";
    public static final String MY_PREFS_login = "login_data";

    String email;
    String password;
    String str_facebookname, str_facebookemail, str_facebookid, str_birthday, str_location;
    boolean boolean_login;
    TextView new_account_txt;
    LinearLayout fc_login_linearLayout;
    private String send_email_url = "http://cookehome.com/CookApp/User/FindEmail.php";

    NetworkAvailable networkAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_log_in);

        networkAvailable = new NetworkAvailable(LogIn.this);
        // start FCM Service
        startService(new Intent(this, FCMRegistrationService.class));

        callbackManager = CallbackManager.Factory.create();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        login_email_ed = findViewById(R.id.user_email);
        login_email_ed.setTypeface(custom_font);
        login_pass_ed = findViewById(R.id.user_pass);
        login_pass_ed.setTypeface(custom_font);
        login_btn = findViewById(R.id.login_btn);
        back = findViewById(R.id.back);
        forget_pass = findViewById(R.id.forget_pass_txt);
        fc_login_linearLayout = findViewById(R.id.fc_login_pg_btn);
        new_account_txt = findViewById(R.id.new_account);
        fb_login_button = findViewById(R.id.fb_login_pg_btn);

        //   get data from shared preferences ...
        sharedPreferences = getSharedPreferences(MY_PREFS_login, MODE_PRIVATE);
        login_email_ed.setText(sharedPreferences.getString("email", ""));//"No name defined" is the default value.
        login_pass_ed.setText(sharedPreferences.getString("password", "")); //0 is the default value.

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        new_account_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogIn.this, LogInTypes.class));
                finish();
            }
        });
        // google ........
        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.google_login_btn);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // finish();
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogIn.this, ForgetPassword.class));
            }
        });

        fc_login_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fb_login();
            }
        });
//        fb_login_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                fb_login();
//            }
//        });
    }

    private void fb_login() {
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        callbackManager = CallbackManager.Factory.create();
//        startService(new Intent(this, FCMRegistrationService.class));

        fb_login_button.setReadPermissions("email");
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"
                , "email"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {

                                    boolean_login = true;
                                    //tv_facebook.setText("Logout from Facebook");

                                    Log.e("object", object.toString());
                                    str_facebookname = object.getString("name");

                                    try {
                                        str_facebookemail = object.getString("email");
                                    } catch (Exception e) {
                                        str_facebookemail = "";
                                        e.printStackTrace();
                                    }

                                    try {
                                        str_facebookid = object.getString("id");
                                    } catch (Exception e) {
                                        str_facebookid = "";
                                        e.printStackTrace();

                                    }


                                    try {
                                        str_birthday = object.getString("birthday");
                                    } catch (Exception e) {
                                        str_birthday = "";
                                        e.printStackTrace();
                                    }

                                    try {
                                        JSONObject jsonobject_location = object.getJSONObject("location");
                                        str_location = jsonobject_location.getString("name");

                                    } catch (Exception e) {
                                        str_location = "";
                                        e.printStackTrace();
                                    }

                                    //fn_profilepic();
                                    send_Email(str_facebookemail, str_facebookname);
                                    //send_Email(str_facebookemail);

                                } catch (Exception e) {

                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, email,gender,birthday,location");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LogIn.this, "Login Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void send_Email(final String login_email, final String display_name) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, send_email_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
//                    Toast.makeText(LogIn.this, "" + success, Toast.LENGTH_SHORT).show();
                    if (success) {

                        String ID = jsonObject.getString("ID");
                        String name = jsonObject.getString("Name");
                        String email = jsonObject.getString("Email");
                        String phone = jsonObject.getString("Phone");
                        String img = jsonObject.getString("img");
                        String Password = jsonObject.getString("Password");
                        String address = jsonObject.getString("Address");
                        int available = Integer.parseInt(jsonObject.getString("available"));
                        int type = Integer.parseInt(jsonObject.getString("type"));

                        user_data_edito = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        user_data_edito.putString("user_id", ID);
                        user_data_edito.putString("email", email);
                        user_data_edito.putString("password", Password);
                        user_data_edito.putString("userName", name);
                        user_data_edito.putString("address", address);
                        user_data_edito.putInt("available", available);
                        user_data_edito.putString("phone", phone);
                        user_data_edito.putString("img", img);
                        user_data_edito.putString("type", type + "");
                        user_data_edito.commit();
                        user_data_edito.apply();

                        Intent intent = new Intent(LogIn.this, MainActivity.class);
                        intent.putExtra("customer_id", ID);
                        intent.putExtra("email", email);
                        intent.putExtra("Name", name);
                        intent.putExtra("password", Password);
                        intent.putExtra("Phone", phone);
                        intent.putExtra("img", img);
                        intent.putExtra("Address", address);
                        intent.putExtra("available", available);
                        intent.putExtra("type", type);
                        startActivity(intent);

                        Toast.makeText(LogIn.this, "تم الدخول بنجاح", Toast.LENGTH_SHORT).show();

                    } else {
                        Intent intent = new Intent(LogIn.this, SignUp.class);
                        intent.putExtra("email", login_email);
                        intent.putExtra("name", display_name);
                        startActivity(intent);
                        finish();
//                        return;
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
                params.put("email", login_email);
                return params;
            }
        };
        Volley.newRequestQueue(LogIn.this).add(stringRequest);
    }

    int RC_SIGN_IN = 100;

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void initialize() {
        email = login_email_ed.getText().toString().trim();
        password = login_pass_ed.getText().toString().trim();

    }

    private boolean validate() {
        boolean valid = true;
        if (TextUtils.isEmpty(email)) {
            login_email_ed.setError("مطلوب");
            login_email_ed.requestFocus();
            valid = false;
        }
//        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
//            login_email_ed.setError("ادخل ايميل صحيح!");
//            login_email_ed.requestFocus();
//            valid = false;
//        }

        if (TextUtils.isEmpty(password)) {
            login_pass_ed.setError("مطلوب");
            login_pass_ed.requestFocus();
            valid = false;
        }

        return valid;
    }


    private void login() {
        String token = FirebaseInstanceId.getInstance().getToken();
        sendTokenToServer(token);
        initialize();
        if (!validate()) {
        } else {
            Response.Listener<String> listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        String ID = "";
                        String email = "";
                        String Name = "";
                        String Password = "";
                        String phone = "";
                        String image = "";
                        String Address = "";
                        int available = 0;
                        int type = -1;

                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");

                        if (success) {
                            ID = jsonObject.getString("ID");
                            email = jsonObject.getString("Email");
                            Name = jsonObject.getString("Name");
                            Password = jsonObject.getString("Password");
                            phone = jsonObject.getString("Phone");
                            image = jsonObject.getString("img");
                            Address = jsonObject.getString("Address");
                            available = jsonObject.getInt("available");
                            type = jsonObject.getInt("type");

                            // Save user data in shared preferences ...
                            user_data_edito = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            user_data_edito.putString("user_id", ID);
                            user_data_edito.putString("email", email);
                            user_data_edito.putString("password", Password);
                            user_data_edito.putString("userName", Name);
                            user_data_edito.putString("address", Address);
                            user_data_edito.putInt("available", available);
                            user_data_edito.putString("phone", phone);
                            user_data_edito.putString("img", image);
                            user_data_edito.putString("type", type + "");
                            user_data_edito.commit();
                            user_data_edito.apply();

                            Intent intent = new Intent(LogIn.this, MainActivity.class);
                            intent.putExtra("customer_id", ID);
                            intent.putExtra("email", email);
                            intent.putExtra("Name", Name);
                            intent.putExtra("password", Password);
                            intent.putExtra("Phone", phone);
                            intent.putExtra("img", image);
                            intent.putExtra("Address", Address);
                            intent.putExtra("available", available);
                            intent.putExtra("type", type);
                            startActivity(intent);

                            Toast.makeText(LogIn.this, "تم الدخول بنجاح", Toast.LENGTH_SHORT).show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LogIn.this);
                            builder.setMessage("خطأ الايميل او الباسورد")
                                    .setCancelable(false)
                                    .setPositiveButton("الغاء", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    }).create().show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            LogInRequest logInRequest = new LogInRequest(email, password, token, listener);
            RequestQueue requestQueue = Volley.newRequestQueue(LogIn.this);
            requestQueue.add(logInRequest);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.commit();
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            String display_name = account.getDisplayName();
            final String email = account.getEmail();
            send_Email(email, display_name);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(this, "he reject the request...", Toast.LENGTH_SHORT).show();
        }
    }

    // method use vollezy to send token to server and stop the service when done or error happened
    private void sendTokenToServer(final String token) {
        String ADD_TOKEN_URL = "https://cookehome.com/CookApp/User/sendToken.php";
        StringRequest request = new StringRequest(Request.Method.POST, ADD_TOKEN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        Log.e("Registration Service", "Response : Send Token Success");
                    } else {
                        Log.e("Registration Service", "Response : Send Token Failed");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // preferences.edit().putBoolean("token_sent", false).apply();
                error.printStackTrace();
                error.getMessage();
                Log.e("Registration Service", "Error :Send Token Failed");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("user_id", MainActivity.customer_id);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
}