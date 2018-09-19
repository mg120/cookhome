package com.ncookhom.Contact;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ncookhom.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContactIdara extends AppCompatActivity {

    private String contact_url = "http://cookehome.com/CookApp/ShowData/Users/ShowContact.php";

    String send_msg_url = "http://cookehome.com/CookApp/sendemail.php";
    ArrayList<ContactModel> list = new ArrayList<>();
    private TextView back;
    private ListView listView;
    private ContactAdapter adapter;
    private EditText contact_name_ed, contact_email_ed, contact_msgtit_ed, contact_msgdesc_ed;
    private Button send_msg;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_idara);

        listView = (ListView) findViewById(R.id.contact_list);
        contact_name_ed = (EditText) findViewById(R.id.contact_user_name);
        contact_email_ed = (EditText) findViewById(R.id.contact_user_email);
        contact_msgtit_ed = (EditText) findViewById(R.id.contact_msg_title);
        contact_msgdesc_ed = (EditText) findViewById(R.id.contact_msg_desc);
        send_msg = (Button) findViewById(R.id.send_msg);
        back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        StringRequest stringRequest = new StringRequest(Request.Method.GET, contact_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String id = jsonObject.getString("id");
                        String title = jsonObject.getString("name");
                        String desc = jsonObject.getString("describtion");

                        list.add(new ContactModel(id, title, desc));
                    }
                    adapter = new ContactAdapter(ContactIdara.this, list);
                    listView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ContactIdara.this, "Error Connection", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(ContactIdara.this);
        queue.add(stringRequest);
        //-----------------------------------------------------------------------------------------

        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contact_name = contact_name_ed.getText().toString().trim();
                final String contact_email = contact_email_ed.getText().toString().trim();
                final String msg_title = contact_msgtit_ed.getText().toString().trim();
                final String msg_desc = contact_msgdesc_ed.getText().toString().trim();

                if (TextUtils.isEmpty(contact_name)) {
                    contact_name_ed.setError("مطلوب");
                    contact_name_ed.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(contact_email)) {
                    contact_email_ed.setError("مطلوب");
                    contact_email_ed.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(contact_email).matches()) {
                    contact_email_ed.setError("ادخل ايميل صحيح!");
                    contact_email_ed.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(msg_title)) {
                    contact_msgtit_ed.setError("مطلوب");
                    contact_msgtit_ed.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(msg_desc)) {
                    contact_msgdesc_ed.setError("مطلوب");
                    contact_msgdesc_ed.requestFocus();
                    return;
                }

                if (!list.isEmpty()) {
                    if (list.get(4).getDescription() != null) {
                        email = list.get(4).getDescription();
                    }
                }

                StringRequest contact_request = new StringRequest(Request.Method.POST, send_msg_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
//                            Toast.makeText(ContactIdara.this, "" + success, Toast.LENGTH_SHORT).show();
                            if (success.equals("message send successfully")){
                                Toast.makeText(ContactIdara.this, "تم ارسال الرسالة بنجاح", Toast.LENGTH_SHORT).show();
                                contact_name_ed.setText("");
                                contact_email_ed.setText("");
                                contact_msgtit_ed.setText("");
                                contact_msgdesc_ed.setText("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("to", email);
                        params.put("from", contact_email);
                        params.put("subject", msg_title);
                        params.put("message", msg_desc);
                        return params;
                    }
                };
                Volley.newRequestQueue(ContactIdara.this).add(contact_request);

//                               Intent i = new Intent(Intent.ACTION_SEND);
//                i.setData(Uri.parse("mailto:"));
//                i.setType("message/rfc822");
//                i.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
//                i.putExtra(Intent.EXTRA_SUBJECT, msg_title);
//                i.putExtra(Intent.EXTRA_TEXT, msg_desc);
//                try {
//                    startActivity(Intent.createChooser(i, "Send mail..."));
//                    // Toast.makeText(ContactIdara.this, "تم الارسال بنجاح", Toast.LENGTH_SHORT).show();
//                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(ContactIdara.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
//                }

            }
        });
    }
}
