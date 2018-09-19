package com.ncookhom.NavFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ncookhom.Chat.Chat;
import com.ncookhom.Chat.ChatModel;
import com.ncookhom.MainActivity;
import com.ncookhom.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ma7MouD on 8/15/2018.
 */

public class MessagesRecyclerAdapter extends RecyclerView.Adapter<MessagesRecyclerAdapter.ViewHolder> {

    Context context;
    List<ChatMessagesModel> list;
    private String open_chat_url = "http://cookehome.com/CookApp/getChat.php";
    public static ArrayList<ChatModel> chat_list = new ArrayList<>();

    public MessagesRecyclerAdapter(Context context, List<ChatMessagesModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MessagesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessagesRecyclerAdapter.ViewHolder holder, int position) {
        holder.item_name.setText(list.get(position).getRecive_name());
        holder.item_description.setText(list.get(position).getMessage());
        holder.item_price.setText(list.get(position).getDate() + "");
        holder.viewForeground.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void insertItem(ChatMessagesModel item, int position) {
        list.add(position, item);
        notifyItemInserted(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView item_name, item_description;
        public TextView item_price;
        public LinearLayout viewForeground;

        public ViewHolder(View itemView) {
            super(itemView);

            item_name = itemView.findViewById(R.id.item_name_txtv);
            item_description = itemView.findViewById(R.id.item_desc_txtv);
            item_price = itemView.findViewById(R.id.item_price_txtview);
            viewForeground = itemView.findViewById(R.id.view_foreground);

            viewForeground.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = (int) view.getTag();
            String sender_id = list.get(position).getSender_id();
            String receiver_id = list.get(position).getRecive_id();
            String sender_name = list.get(position).getSender_name();
            String receiver_name = list.get(position).getRecive_name();
//            Toast.makeText(context, "Sender_id : " + list.get(position).getSender_id(), Toast.LENGTH_SHORT).show();
//            if (!sender_id.equals(MainActivity.customer_id))
            Family_chat(sender_id, receiver_id, sender_name, receiver_name);
        }
    }

    private void Family_chat(final String sender_id, final String receiver_id, final String sender_name, final String receiver_name) {
        if (!MainActivity.customer_id.equals("")) {
            if (sender_id.equals(MainActivity.customer_id)) {
                Intent intent1 = new Intent(context, Chat.class);
                intent1.putExtra("sender_id", sender_id);
                intent1.putExtra("receiver_id", receiver_id);
                intent1.putExtra("sender_name", sender_name);
                intent1.putExtra("receiver_name", receiver_name);
                context.startActivity(intent1);
            } else {
                Intent intent1 = new Intent(context, Chat.class);
                intent1.putExtra("sender_id", receiver_id);
                intent1.putExtra("receiver_id", sender_id);
                intent1.putExtra("sender_name", receiver_name);
                intent1.putExtra("receiver_name", sender_name);
                context.startActivity(intent1);
            }
        } else {
            Toast.makeText(context, "يجب تسجيل الدخول اولا", Toast.LENGTH_SHORT).show();
        }

//        StringRequest open_chat_request = new StringRequest(Request.Method.POST, open_chat_url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                chat_list.clear();
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    boolean success = jsonObject.getBoolean("success");
//                    JSONArray jsonArray = jsonObject.getJSONArray("chat");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject data_obj = jsonArray.getJSONObject(i);
//                        String chat_id = data_obj.getString("chat_id");
//                        String sender_id = data_obj.getString("sender_id");
//                        String sender_name = data_obj.getString("sender_name");
//                        String reciver_id = data_obj.getString("reciver_id");
//                        String recive_name = data_obj.getString("recive_name");
//                        String message = data_obj.getString("message");
//                        String send_at_time = data_obj.getString("send_at");
//
//                        chat_list.add(new ChatModel(sender_id, sender_name, message, send_at_time));
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context, "لا يوجد اتصال بالانترنت", Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("sender_id", MainActivity.customer_id);
//                params.put("reciver_id", receiver_id);
//                return params;
//            }
//        };
//        Volley.newRequestQueue(context).add(open_chat_request);
    }
}