package com.ncookhom.FamilyProducts;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ma7MouD on 4/12/2018.
 */

public class FamilyProductsModel implements Parcelable{

    private String product_id ;
    private String imageView_url ;
    private String title ;
    private String food_type ;
    private String description ;
    private String points_num ;
    private String price ;
    private String time ;

    public FamilyProductsModel(String product_id, String imageView_url, String title, String food_type, String description, String points_num, String price, String time) {
        this.product_id = product_id;
        this.imageView_url = imageView_url;
        this.title = title;
        this.food_type = food_type;
        this.description = description;
        this.points_num = points_num;
        this.price = price;
        this.time = time;
    }

    protected FamilyProductsModel(Parcel in) {
        product_id = in.readString();
        imageView_url = in.readString();
        title = in.readString();
        food_type = in.readString();
        description = in.readString();
        points_num = in.readString();
        price = in.readString();
        time = in.readString();
    }

    public static final Creator<FamilyProductsModel> CREATOR = new Creator<FamilyProductsModel>() {
        @Override
        public FamilyProductsModel createFromParcel(Parcel in) {
            return new FamilyProductsModel(in);
        }

        @Override
        public FamilyProductsModel[] newArray(int size) {
            return new FamilyProductsModel[size];
        }
    };

    public String getFood_type() {
        return food_type;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImageView_url() {
        return imageView_url;
    }

    public void setImageView_url(String imageView_url) {
        this.imageView_url = imageView_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPoints_num() {
        return points_num;
    }

    public void setPoints_num(String points_num) {
        this.points_num = points_num;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(product_id);
        parcel.writeString(imageView_url);
        parcel.writeString(title);
        parcel.writeString(food_type);
        parcel.writeString(description);
        parcel.writeString(points_num);
        parcel.writeString(price);
        parcel.writeString(time);
    }
}
