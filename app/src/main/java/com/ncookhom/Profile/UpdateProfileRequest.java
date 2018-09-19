package com.ncookhom.Profile;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ma7MouD on 4/21/2018.
 */

public class UpdateProfileRequest extends StringRequest {

    private static final String update_profile_url = "http://cookehome.com/CookApp/UpdateInfo.php";
    private Map<String, String> params ;

    public UpdateProfileRequest(String customer_id, String name, String email, String pass, Response.Listener<String> listener) {
        super(Method.POST, update_profile_url, listener, null);

        params = new HashMap<>();
        params.put("ID", customer_id);
        params.put("Name", name);
        params.put("Email", email);
        params.put("Password", pass);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
