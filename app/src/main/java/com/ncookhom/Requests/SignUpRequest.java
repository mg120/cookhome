package com.ncookhom.Requests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;

/**
 * Created by Ma7MouD on 4/8/2018.
 */

public class SignUpRequest extends StringRequest {

    private static final String signUp_url = "http://cookehome.com/CookApp/create_account1.php";
    private HashMap<String, String> params ;
    public SignUpRequest(String name, String email, String pass, String phone, String img, String lat, String lan
            , String address, String type, Response.Listener<String> listener) {
        super(Method.POST, signUp_url, listener, null);

        params = new HashMap<>();
        // put data to map here ...
        params.put("Name" , name);
        params.put("Email" , email );
        params.put("Password" , pass);
        params.put("Phone" , phone);
        params.put("img" , img);
        params.put("Lat" , lat);
        params.put("Lan" , lan);
        params.put("Address" , address);
        params.put("type" , type);
    }

    @Override
    public HashMap<String, String> getParams() {
        return params;
    }
}
