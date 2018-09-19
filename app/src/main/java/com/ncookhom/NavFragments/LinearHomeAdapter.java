package com.ncookhom.NavFragments;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ncookhom.Card.MyCard;
import com.ncookhom.FamilyProducts.FamilyProducts;
import com.ncookhom.FamilyProducts.ProdImagesModel;
import com.ncookhom.MyProducts.HomeModel;
import com.ncookhom.ProductDetails.ProductDetails;
import com.ncookhom.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ma7MouD on 9/2/2018.
 */

public class LinearHomeAdapter extends RecyclerView.Adapter<LinearHomeAdapter.ViewHolder>{

    Context context ;
    ArrayList<HomeModel> list;
    ArrayList<ProdImagesModel> images_list;

    public LinearHomeAdapter(Context context, ArrayList<HomeModel> list, ArrayList<ProdImagesModel> images_list) {
        this.context = context;
        this.list = list;
        this.images_list = images_list;
    }

    @Override
    public LinearHomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_product_linear_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LinearHomeAdapter.ViewHolder holder, int position) {

        Picasso.with(context).load("http://cookehome.com/CookApp/images/" + list.get(position).getMeal_image()).into(holder.item_image);
        holder.item_name.setText(list.get(position).getFamily_name());
        holder.item_osra_name.setText(list.get(position).getSeller_name());
        holder.item_price.setText(list.get(position).getPrice());
        holder.item_osra_location.setText(list.get(position).getAddress());
        if (list.get(position).getFamily_rate() != null && list.get(position).getFamily_rate().length() > 0) {
            float d = (float) ((Float.parseFloat(String.valueOf(list.get(position).getFamily_rate())) * 5) / 100);
            holder.item_osra_rate.setRating(d);
        }
        holder.cardView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView ;
        ImageView item_image ;
        TextView item_name, item_osra_name, item_price, item_osra_location;
        RatingBar item_osra_rate ;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.home_linear_item_layout);
            item_image = itemView.findViewById(R.id.item_image);
            item_name = itemView.findViewById(R.id.item_name);
            item_osra_name = itemView.findViewById(R.id.osra_name_id);
            item_price = itemView.findViewById(R.id.item_price_id);
            item_osra_location = itemView.findViewById(R.id.sra_location_id);
            item_osra_rate = itemView.findViewById(R.id.item_osra_rate);

            item_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getLayoutPosition();
                    int position = getAdapterPosition();
                    Intent intent = new Intent(context, ProductDetails.class);
                    final ProdImagesModel imagesModel = images_list.get(pos);
                    final HomeModel homeModel = list.get(pos);

                    intent.putExtra("Imageslist", imagesModel);
                    intent.putExtra("product_data", homeModel);
                    intent.putExtra("rate_val", homeModel.getFamily_rate() + "");
                    context.startActivity(intent);
                }
            });
            item_osra_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getLayoutPosition();
                    Intent intent = new Intent(context, FamilyProducts.class);
                    intent.putExtra("family_id", list.get(pos).getSeller_id());
                    context.startActivity(intent);
                }
            });

//            add_card_img.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    context.startActivity(new Intent(context, MyCard.class));
//                }
//            });
        }
    }
}