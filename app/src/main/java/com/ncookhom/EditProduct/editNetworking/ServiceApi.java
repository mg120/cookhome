package com.ncookhom.EditProduct.editNetworking;

import com.ncookhom.NavFragments.Model.PicModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ServiceApi {

    @Multipart
    @POST(Urls.uploadPic)
    Call<PicModel> uploadImage(

//            @Query("Name") String Name,
//            @Query("FoodType") String FoodType,
//            @Query("Timeee") String Timeee,
//            @Query("Price") String Price,
//            @Query("Description") String Description,
//            @Query("Customer_id") String Customer_id,
//            @Query("customer_name") String customer_name,
//            @Query("customer_mail") String customer_mail,

            @Part("Name") RequestBody name,
            @Part("FoodType") RequestBody foodType,
            @Part("Timeee") RequestBody time,
            @Part("Price") RequestBody price,
            @Part("Description") RequestBody desc,
            @Part("Customer_id") RequestBody customer_id,
            @Part("customer_name") RequestBody customer_name,
            @Part("customer_mail") RequestBody customer_email,

            @Part MultipartBody.Part image1,
            @Part MultipartBody.Part image2,
            @Part MultipartBody.Part image3,
            @Part MultipartBody.Part image4,
            @Part MultipartBody.Part image5);
}
