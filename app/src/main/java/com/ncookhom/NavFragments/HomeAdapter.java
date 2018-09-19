package com.ncookhom.NavFragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ncookhom.Card.MyCard;
import com.ncookhom.FamilyProducts.FamilyProducts;
import com.ncookhom.FamilyProducts.ProdImagesModel;
import com.ncookhom.MainActivity;
import com.ncookhom.MyProducts.HomeModel;
import com.ncookhom.ProductDetails.ProductDetails;
import com.ncookhom.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ma7MouD on 4/9/2018.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    Context context;
    ArrayList<HomeModel> list;
    ArrayList<ProdImagesModel> images_list;
    private int type;
    int rate_val = 0;
    private String product_rate_url = "http://cookehome.com/CookApp/Other/SearchRate.php";

    public HomeAdapter(Context context, ArrayList<HomeModel> list, ArrayList<ProdImagesModel> images_list) {
        this.context = context;
        this.list = list;
        this.images_list = images_list;
    }

    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_item, parent, false);
        type = MainActivity.type;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HomeAdapter.ViewHolder holder, final int position) {

//        if (type == 0) {
        // user , so  disappear edit and remove ...
        holder.layout_1.setVisibility(View.VISIBLE);
        holder.layout_2.setVisibility(View.VISIBLE);
        holder.edit_remov_layout.setVisibility(View.GONE);
//        } else if (type == 1) {
//            holder.layout_1.setVisibility(View.GONE);
//            holder.layout_2.setVisibility(View.GONE);
//            holder.edit_remov_layout.setVisibility(View.VISIBLE);
//        }
        StringRequest rate_request = new StringRequest(Request.Method.POST, product_rate_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        rate_val = jsonObject.getInt("rate");
                        // Toast.makeText(context, "Rate: " + rate_val, Toast.LENGTH_LONG).show();
                        Log.e("Rate: ", rate_val + "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error Connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("product_id", list.get(position).getFamily_id());
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(rate_request);
        Log.e("Rate::", rate_val + "");
        Picasso.with(context).load("http://cookehome.com/CookApp/images/" + list.get(position).getMeal_image()).into(holder.family_image);
        holder.family_name.setText(list.get(position).getFamily_name());
        holder.home_fam_prod_price.setText(String.valueOf(list.get(position).getPrice()));
        holder.family_prod_name.setText(list.get(position).getFamily_name());
        holder.family_name.setText(list.get(position).getSeller_name());
        holder.family_city.setText(list.get(position).getAddress());
        if (list.get(position).getFamily_rate() != null && list.get(position).getFamily_rate().length() > 0) {
            float d = (float) ((Float.parseFloat(String.valueOf(list.get(position).getFamily_rate())) * 5) / 100);
//            Toast.makeText(context, "" + d, Toast.LENGTH_SHORT).show();
            holder.family_rate.setRating(d);
        }
        holder.home_cardview.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(ArrayList<HomeModel> filterdNames) {
        this.list = filterdNames;
        notifyDataSetChanged();
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView family_image;
        private TextView family_name;
        private TextView family_prod_name;
        private RatingBar family_rate;
        private TextView family_city;
        private TextView home_fam_prod_price;
        private CardView home_cardview;
        private ImageView add_card_img;
        private FrameLayout layout_1, layout_2, edit_remov_layout;
        private Button home_edit_pro_btn, home_remove_pro_btn;


        public ViewHolder(View itemView) {
            super(itemView);
            Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/font.ttf");
            home_cardview = itemView.findViewById(R.id.home_cardview);
            family_image = itemView.findViewById(R.id.home_family_img);
            add_card_img = itemView.findViewById(R.id.home_add_card);
            family_name = itemView.findViewById(R.id.home_family_name);
            family_prod_name = itemView.findViewById(R.id.home_fam_prod_name);
            family_name.setTypeface(custom_font);
            family_rate = itemView.findViewById(R.id.home_family_rate);
            family_city = itemView.findViewById(R.id.home_family_city);
            home_fam_prod_price = itemView.findViewById(R.id.home_fam_prod_price);

            layout_1 = itemView.findViewById(R.id.home_layout_1);
            layout_2 = itemView.findViewById(R.id.home_layout_2);
            edit_remov_layout = itemView.findViewById(R.id.edit_remov_layout);
            home_edit_pro_btn = itemView.findViewById(R.id.home_edit_product);
            home_remove_pro_btn = itemView.findViewById(R.id.home_remove_prod);

            family_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getLayoutPosition();
                    int position = getAdapterPosition();
                    Intent intent = new Intent(context, ProductDetails.class);
                    final ProdImagesModel imagesModel = images_list.get(pos);
                    final HomeModel homeModel = list.get(pos);

                    intent.putExtra("Imageslist", imagesModel);
                    intent.putExtra("product_data", homeModel);
                    intent.putExtra("rate_val", rate_val + "");
                    context.startActivity(intent);
                }
            });
            family_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getLayoutPosition();
                    Intent intent = new Intent(context, FamilyProducts.class);
                    intent.putExtra("family_id", list.get(pos).getSeller_id());
                    context.startActivity(intent);
                }
            });

            add_card_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, MyCard.class));
                }
            });
        }

//        @Override
//        public void onClick(View view) {
//            int position = (int) view.getTag();
//            //Toast.makeText(context, "pos: "+ position, Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(context, FamilyProducts.class);
//            intent.putExtra("family_Id", list.get(position).getFamily_id());
//            context.startActivity(intent);
//        }
    }
}
