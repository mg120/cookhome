package com.ncookhom.MyProducts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ncookhom.EditProduct.EditProduct;
import com.ncookhom.FamilyProducts.FamilyProducts;
import com.ncookhom.FamilyProducts.ProdImagesModel;
import com.ncookhom.NetworkAvailable;
import com.ncookhom.ProductDetails.ProductDetails;
import com.ncookhom.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Ma7MouD on 4/23/2018.
 */

public class AosraAdapter extends RecyclerView.Adapter<AosraAdapter.ViewHolder> {

    NetworkAvailable networkAvailable ;
    Context context;
    ArrayList<HomeModel> list;
    ArrayList<ProdImagesModel> images_list;
    private String delete_product_url = "http://cookehome.com/CookApp/Other/DeleteProduct.php";


    public AosraAdapter(Context context, ArrayList<HomeModel> list, ArrayList<ProdImagesModel> images_list) {
        this.context = context;
        this.list = list;
        this.images_list = images_list;
    }

    @Override
    public AosraAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.osra_home_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AosraAdapter.ViewHolder holder, final int position) {

        Picasso.with(context).load("http://cookehome.com/CookApp/images/" + list.get(position).getMeal_image()).into(holder.family_image);
        holder.family_name.setText(list.get(position).getFamily_name());
        holder.family_prod_name.setText(list.get(position).getFamily_name());
        holder.family_name.setText(list.get(position).getSeller_name());

        holder.osra_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditProduct.class);
                intent.putExtra("product_images", images_list.get(position));
                intent.putExtra("product_data", list.get(position));
                context.startActivity(intent);
            }
        });

        holder.osra_remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setIcon(R.drawable.iconlogo)
                            .setMessage("تأكيد حذف المنتج ؟")
                            .setCancelable(false)
                            .setPositiveButton("الغاء", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).setNegativeButton("حذف", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialogInterface, int i) {
                            StringRequest request = new StringRequest(Request.Method.POST, delete_product_url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        int success = jsonObject.getInt("success");
                                        if (success == 1) {
                                            removeAt(position);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                    Toast.makeText(context, "خطأ فى الاتصال الشبكة!", Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("Product_ID", list.get(position).getFamily_id());
                                    return params;
                                }
                            };
                            RequestQueue queue = Volley.newRequestQueue(context);
                            queue.add(request);
                        }
                    }).create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void removeAt(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView family_image;
        private TextView family_name;
        private TextView family_prod_name;
        private Button osra_edit_btn;
        private Button osra_remove_btn;

        public ViewHolder(View itemView) {
            super(itemView);
            family_image = (ImageView) itemView.findViewById(R.id.home_osra_prod_img);
            family_name = (TextView) itemView.findViewById(R.id.home_osra_name);
            family_prod_name = (TextView) itemView.findViewById(R.id.home_osra_prod_name);
            osra_edit_btn = (Button) itemView.findViewById(R.id.osra_edit_product);
            osra_remove_btn = (Button) itemView.findViewById(R.id.osra_remove_prod);

            family_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getLayoutPosition();
                    Intent intent = new Intent(context, FamilyProducts.class);
                    intent.putExtra("family_id", list.get(pos).getSeller_id());
                    context.startActivity(intent);
                }
            });

            family_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getLayoutPosition();
                    Intent intent = new Intent(context, ProductDetails.class);
                    final ProdImagesModel imagesModel = images_list.get(pos);
                    final HomeModel homeModel = list.get(pos);
                    intent.putExtra("Imageslist", imagesModel);
                    intent.putExtra("product_data", homeModel);
                    context.startActivity(intent);
                }
            });

        }
    }
}
