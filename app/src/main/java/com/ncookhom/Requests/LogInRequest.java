package com.ncookhom.Requests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;

/**
 * Created by Ma7MouD on 4/8/2018.
 */

public class LogInRequest extends StringRequest {

    private static final String login_url = "http://cookehome.com/CookApp/User/Login.php";
    private HashMap<String, String> params ;

    public LogInRequest(String email, String pass, String token, Response.Listener<String> listener) {
        super(Method.POST, login_url, listener, null);

        params = new HashMap<>();
        params.put("Name", email);
        params.put("Password",pass);
        params.put("token", token);
    }

    @Override
    public HashMap<String, String> getParams() {
        return params;
    }
}
