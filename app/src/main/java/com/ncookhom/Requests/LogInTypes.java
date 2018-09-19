package com.ncookhom.Requests;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
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
import com.ncookhom.FCMRegistrationService;
import com.ncookhom.MainActivity;
import com.ncookhom.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LogInTypes extends AppCompatActivity {

    private Button login_btn, create_account_btn;
    GoogleSignInClient mGoogleSignInClient ;
    CallbackManager callbackManager;
    LoginButton fb_login_button ;
    private TextView back ;
    String str_facebookname, str_facebookemail, str_facebookid, str_birthday, str_location;
    boolean boolean_login;
    LinearLayout fb_login_layout ;

    // for login ......
    SharedPreferences.Editor user_data_edito;
    SharedPreferences.Editor editor;
   // public static final String MY_PREFS_NAME = "all_user_data";
    private String send_email_url = "http://cookehome.com/CookApp/User/FindEmail.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        // start FCM Service
        startService(new Intent(this, FCMRegistrationService.class));

        setContentView(R.layout.activity_log_in_types);
        callbackManager = CallbackManager.Factory.create();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        login_btn = (Button) findViewById(R.id.login_type_btn);
        create_account_btn = (Button) findViewById(R.id.create_type_btn);
        fb_login_button = (LoginButton) findViewById(R.id.fb_login_btn);
        back = (TextView) findViewById(R.id.back);
        fb_login_layout = (LinearLayout) findViewById(R.id.fb_login_layout);
//        fb_login_button.setReadPermissions("email");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LogInTypes.this);
                builder.setMessage("هل تريد الخروج من التطبيق ؟")
                        .setCancelable(false)
                        .setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //super.onBackPressed();
//                        pref = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
//                        editor = pref.edit();
//                        editor.clear();
//                        editor.apply();
//                        // -------------------------------------------------------
//                        login_prefs = getSharedPreferences(login_pref_file, Context.MODE_PRIVATE);
//                        login_editor = login_prefs.edit();
//                        login_editor.putBoolean("login", false);
//                        login_editor.apply();
                        finish();

                    }
                }).create().show();
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogInTypes.this, LogIn.class));
            }
        });

        create_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogInTypes.this, SignUp.class));
            }
        });

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               signIn();
            }
        });

        fb_login_layout.setOnClickListener(new View.OnClickListener() {
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
        fb_login_button.setReadPermissions("email");
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
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
//                                    Toast.makeText(LogInTypes.this, str_facebookname + "\n" + str_facebookemail, Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(LogInTypes.this, SignUp.class);
//                                    intent.putExtra("email", str_facebookemail);
//                                    intent.putExtra("name", str_facebookname);
//                                    startActivity(intent);

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
                Toast.makeText(LogInTypes.this, "Login Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    int RC_SIGN_IN = 10 ;
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            String display_name = account.getDisplayName();
            String email = account.getEmail();
            send_Email(email, display_name);
            //Toast.makeText(this, display_name +"\n" + email, Toast.LENGTH_SHORT).show();


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, "he reject the request ...", Toast.LENGTH_SHORT).show();
        }
    }

    private void send_Email(final String login_email, final String login_name) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, send_email_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success){

                        String ID = jsonObject.getString("ID");
                        String name = jsonObject.getString("Name");
                        String email = jsonObject.getString("Email");
                        String phone = jsonObject.getString("Phone");
                        String img = jsonObject.getString("img");
                        String Password = jsonObject.getString("Password");
                        String address = jsonObject.getString("Address");
                        int available = Integer.parseInt(jsonObject.getString("available"));
                        int type = Integer.parseInt(jsonObject.getString("type"));
//                        Toast.makeText(LogInTypes.this, name+"\n"+ email + "\n"+ available , Toast.LENGTH_SHORT).show();

                        user_data_edito = getSharedPreferences(LogIn.MY_PREFS_NAME, MODE_PRIVATE).edit();
                        user_data_edito.putString("user_id", ID);
                        user_data_edito.putString("email", email);
                        user_data_edito.putString("password", Password);
                        user_data_edito.putString("userName", name);
                        user_data_edito.putString("address", address);
                        user_data_edito.putInt("available", available);
                        user_data_edito.putString("phone", phone);
                        user_data_edito.putString("img", img);
                        user_data_edito.putString("type", type +"" );
                        user_data_edito.commit();
                        user_data_edito.apply();

                        //Toast.makeText(LogIn.this, "success: "+ success, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LogInTypes.this, MainActivity.class);
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

                        Toast.makeText(LogInTypes.this, "تم الدخول بنجاح", Toast.LENGTH_SHORT).show();

                    } else {
                        Intent intent = new Intent(LogInTypes.this, SignUp.class);
                        intent.putExtra("email", login_email);
                        intent.putExtra("name", login_name);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LogInTypes.this, "Error Connection", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("email", login_email);
                return params;
            }
        };
        Volley.newRequestQueue(LogInTypes.this).add(stringRequest);
    }
}
