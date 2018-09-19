package com.ncookhom.EditProduct;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ncookhom.FamilyProducts.ProdImagesModel;
import com.ncookhom.MainActivity;
import com.ncookhom.NavFragments.DialogUtil;
import com.ncookhom.MyProducts.HomeModel;
import com.ncookhom.NavFragments.Model.PicModel;
import com.ncookhom.NavFragments.PermissionUtils;
import com.ncookhom.NavFragments.ProgressRequestBody;
import com.ncookhom.NavFragments.networking.RetroWeb;
import com.ncookhom.NavFragments.networking.ServiceApi;
import com.ncookhom.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import gun0912.tedbottompicker.TedBottomPicker;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class EditProduct extends AppCompatActivity implements ProgressRequestBody.UploadCallbacks{

    ImageView imageView1, imageView2, imageView3, imageView4, imageView5;


    ArrayList<Uri> images = new ArrayList<>();

    private ProgressDialog progressDialog;
    private MultipartBody.Part mPartImageOne, mPartImageTwo, mPartImageThree, mPartImageFour, mPartImageFive;

    private String name, food_type, time, desc, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        Intent intent = getIntent();
        final ProdImagesModel list = intent.getParcelableExtra("product_images");
        final HomeModel product_data = intent.getParcelableExtra("product_data");

        TextView back = findViewById(R.id.back);
        imageView1 = findViewById(R.id.prod_img1);
        imageView2 = findViewById(R.id.prod_img2);
        imageView3 = findViewById(R.id.prod_img3);
        imageView4 = findViewById(R.id.prod_img4);
        imageView5 = findViewById(R.id.prod_img5);
        final EditText prod_name_ed = findViewById(R.id.edit_prod_name_ed);
        final EditText prod_type_ed = findViewById(R.id.edit_prod_type_ed);
        final EditText prod_time_ed = findViewById(R.id.edit_prod_time_ed);
        final EditText prod_desc_ed = findViewById(R.id.edit_prod_desc_ed);
        final EditText prod_price_ed = findViewById(R.id.edit_prod_price_ed);
        Button change_imgs_btn = findViewById(R.id.change_imgs);
        final Button edit_prod_btn = findViewById(R.id.edit_prod_btn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // --- product images ....
        Picasso.with(EditProduct.this).load("http://cookehome.com/CookApp/images/" + list.getProd_img1()).into(imageView1);
        Picasso.with(EditProduct.this).load("http://cookehome.com/CookApp/images/" + list.getProd_img2()).into(imageView2);
        Picasso.with(EditProduct.this).load("http://cookehome.com/CookApp/images/" + list.getProd_img3()).into(imageView3);
        Picasso.with(EditProduct.this).load("http://cookehome.com/CookApp/images/" + list.getProd_img4()).into(imageView4);
        Picasso.with(EditProduct.this).load("http://cookehome.com/CookApp/images/" + list.getProd_img5()).into(imageView5);

        /// --------------------------------------
//        Toast.makeText(this, "id: "+ product_data.getFamily_id(), Toast.LENGTH_SHORT).show();
        prod_name_ed.setText(product_data.getFamily_name());
        prod_type_ed.setText(product_data.getFood_type());
        prod_desc_ed.setText(product_data.getDesc());
        prod_time_ed.setText(product_data.getTime());
        prod_price_ed.setText(product_data.getPrice());
        /// ---------------------------------------
        change_imgs_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPickImageWithPermission();
            }
        });

        edit_prod_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 name = prod_name_ed.getText().toString().trim();
                 food_type = prod_type_ed.getText().toString().trim();
                 time = prod_time_ed.getText().toString().trim();
                 desc = prod_desc_ed.getText().toString().trim();
                 price = prod_price_ed.getText().toString().trim();

                if (images == null || images.size() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditProduct.this);
                    builder.setMessage("اضف صور المنتج اولا")
                            .setCancelable(false)
                            .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();
                    return;
                }

                if (TextUtils.isEmpty(name)) {
                    prod_name_ed.setError("هذا الحقل مطلوب");
                    prod_name_ed.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(food_type)) {
                    prod_type_ed.setError("هذا الحقل مطلوب");
                    prod_type_ed.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(time)) {
                    prod_time_ed.setError("هذا الحقل مطلوب");
                    prod_time_ed.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(desc)) {
                    prod_desc_ed.setError("هذا الحقل مطلوب");
                    prod_desc_ed.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(price)) {
                    prod_price_ed.setError("هذا الحقل مطلوب");
                    prod_price_ed.requestFocus();
                    return;
                }

                // Create Order Now ...
                createOrder();

            }
        });
    }

    public void getPickImageWithPermission() {
        if (PermissionUtils.canMakeSmores(Build.VERSION_CODES.LOLLIPOP_MR1)) {
            if (!PermissionUtils.hasPermissions(EditProduct.this, PermissionUtils.IMAGE_PERMISSIONS)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PermissionUtils.IMAGE_PERMISSIONS,
                            100);
                }
            } else {
                pickMultiImages();
            }
        } else {
            pickMultiImages();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickMultiImages();
            }
        }
    }
    void pickMultiImages() {
        TedBottomPicker bottomSheetDialogFragment = new TedBottomPicker.Builder(EditProduct.this)
                .setOnMultiImageSelectedListener(new TedBottomPicker.OnMultiImageSelectedListener() {
                    @Override
                    public void onImagesSelected(ArrayList<Uri> uriList) {

                        images.clear();
                        images.addAll(uriList);
                        if (images.size() > 0) {
                            createMultiPartFile();
                        }
                    }
                })
                .setTitle("اخترالصور")
                .setSelectMaxCount(5)
                .setSelectMinCount(1)
                .setPeekHeight(2600)
                .showTitle(false)
                .setCompleteButtonText(R.string.done)
                .create();
        bottomSheetDialogFragment.show(getSupportFragmentManager());
    }
    private void createMultiPartFile() {

        imageView1.setImageURI(null);
        imageView2.setImageURI(null);
        imageView3.setImageURI(null);
        imageView4.setImageURI(null);
        imageView5.setImageURI(null);

        if (images.size() == 1) {
            imageView1.setImageURI(images.get(0));
            File ImageFile = new File(images.get(0).getPath());
            final ProgressRequestBody fileBody = new ProgressRequestBody(ImageFile, this);
            mPartImageOne = MultipartBody.Part.createFormData("img", ImageFile.getName(), fileBody);
        }

        if (images.size() == 2) {
            imageView1.setImageURI(images.get(0));
            File ImageFile = new File(images.get(0).getPath());
            final ProgressRequestBody fileBody = new ProgressRequestBody(ImageFile, this);
            mPartImageOne = MultipartBody.Part.createFormData("img", ImageFile.getName(), fileBody);

            imageView2.setImageURI(images.get(1));
            File ImageFileTwo = new File(images.get(1).getPath());
            final ProgressRequestBody fileBodyTwo = new ProgressRequestBody(ImageFileTwo, this);
            mPartImageTwo = MultipartBody.Part.createFormData("img2", ImageFileTwo.getName(), fileBodyTwo);
        }

        if (images.size() == 3) {
            imageView1.setImageURI(images.get(0));
            File ImageFile = new File(images.get(0).getPath());
            final ProgressRequestBody fileBody = new ProgressRequestBody(ImageFile, this);
            mPartImageOne = MultipartBody.Part.createFormData("img", ImageFile.getName(), fileBody);

            imageView2.setImageURI(images.get(1));
            File ImageFileTwo = new File(images.get(1).getPath());
            final ProgressRequestBody fileBodyTwo = new ProgressRequestBody(ImageFileTwo, this);
            mPartImageTwo = MultipartBody.Part.createFormData("img2", ImageFileTwo.getName(), fileBodyTwo);

            imageView3.setImageURI(images.get(2));
            File ImageFileThree = new File(images.get(2).getPath());
            final ProgressRequestBody fileBodyThree = new ProgressRequestBody(ImageFileThree, this);
            mPartImageThree = MultipartBody.Part.createFormData("img3", ImageFileThree.getName(), fileBodyThree);
        }

        if (images.size() == 4) {


            imageView1.setImageURI(images.get(0));
            File ImageFile = new File(images.get(0).getPath());
            final ProgressRequestBody fileBody = new ProgressRequestBody(ImageFile, this);
            mPartImageOne = MultipartBody.Part.createFormData("img", ImageFile.getName(), fileBody);

            imageView2.setImageURI(images.get(1));
            File ImageFileTwo = new File(images.get(1).getPath());
            final ProgressRequestBody fileBodyTwo = new ProgressRequestBody(ImageFileTwo, this);
            mPartImageTwo = MultipartBody.Part.createFormData("img2", ImageFileTwo.getName(), fileBodyTwo);

            imageView3.setImageURI(images.get(2));
            File ImageFileThree = new File(images.get(2).getPath());
            final ProgressRequestBody fileBodyThree = new ProgressRequestBody(ImageFileThree, this);
            mPartImageThree = MultipartBody.Part.createFormData("img3", ImageFileThree.getName(), fileBodyThree);

            imageView4.setImageURI(images.get(3));
            File ImageFileFour = new File(images.get(3).getPath());
            final ProgressRequestBody fileBodyFour = new ProgressRequestBody(ImageFileFour, this);
            mPartImageFour = MultipartBody.Part.createFormData("img4", ImageFileFour.getName(), fileBodyFour);
        }

        if (images.size() == 5) {

            imageView1.setImageURI(images.get(0));
            File ImageFile = new File(images.get(0).getPath());
            final ProgressRequestBody fileBody = new ProgressRequestBody(ImageFile, this);
            mPartImageOne = MultipartBody.Part.createFormData("img", ImageFile.getName(), fileBody);

            imageView2.setImageURI(images.get(1));
            File ImageFileTwo = new File(images.get(1).getPath());
            final ProgressRequestBody fileBodyTwo = new ProgressRequestBody(ImageFileTwo, this);
            mPartImageTwo = MultipartBody.Part.createFormData("img2", ImageFileTwo.getName(), fileBodyTwo);

            imageView3.setImageURI(images.get(2));
            File ImageFileThree = new File(images.get(2).getPath());
            final ProgressRequestBody fileBodyThree = new ProgressRequestBody(ImageFileThree, this);
            mPartImageThree = MultipartBody.Part.createFormData("img3", ImageFileThree.getName(), fileBodyThree);

            imageView4.setImageURI(images.get(3));
            File ImageFileFour = new File(images.get(3).getPath());
            final ProgressRequestBody fileBodyFour = new ProgressRequestBody(ImageFileFour, this);
            mPartImageFour = MultipartBody.Part.createFormData("img4", ImageFileFour.getName(), fileBodyFour);

            imageView5.setImageURI(images.get(4));
            File ImageFileFive = new File(images.get(4).getPath());
            final ProgressRequestBody fileBodyFive = new ProgressRequestBody(ImageFileFive, this);
            mPartImageFive = MultipartBody.Part.createFormData("img5", ImageFileFive.getName(), fileBodyFive);
        }

    }


    private void createOrder() {
//        createMultiPartFile();
        showProgressDialog(R.string.please_wait);

        RequestBody NamePart = RequestBody.create(MultipartBody.FORM, name);
        RequestBody foodtype_Part = RequestBody.create(MultipartBody.FORM, food_type);
        RequestBody time_Part = RequestBody.create(MultipartBody.FORM, time);
        RequestBody price_Part = RequestBody.create(MultipartBody.FORM, price);
        RequestBody description_Part = RequestBody.create(MultipartBody.FORM, desc);
        RequestBody cus_id_part = RequestBody.create(MultipartBody.FORM, MainActivity.customer_id);
        RequestBody cus_name_part = RequestBody.create(MultipartBody.FORM, MainActivity.Name);
        RequestBody cus_email_part = RequestBody.create(MultipartBody.FORM, MainActivity.email);

        Log.d("mPartImageOne:", mPartImageOne + "");
        Log.d("mPartImageTwo:", mPartImageTwo + "");
        Log.d("mPartImageThree:", mPartImageThree + "");
        Log.d("mPartImageFour:", mPartImageFour + "");
        Log.d("mPartImageFive:", mPartImageFive + "");

        RetroWeb.getClient().create(ServiceApi.class)
                .uploadImage(NamePart, foodtype_Part, time_Part, price_Part,
                        description_Part, cus_id_part, cus_name_part, cus_email_part, mPartImageOne, mPartImageTwo, mPartImageThree,
                        mPartImageFour, mPartImageFive)
                .enqueue(new Callback<PicModel>() {
                    @Override
                    public void onResponse(Call<PicModel> call, retrofit2.Response<PicModel> response) {

                        PicModel picModel = response.body();
//                        Toast.makeText(EditProduct.this, "Status: " + picModel.getStatus(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(EditProduct.this, "تم تعديل المنتج", Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<PicModel> call, Throwable t) {
                        t.printStackTrace();
                        hideProgressDialog();
                    }
                });
    }


    protected void showProgressDialog(int message) {
        progressDialog = DialogUtil.showProgressDialog(EditProduct.this, getString(message), false);
    }

    protected void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onProgressUpdate(int percentage) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {

    }
}
