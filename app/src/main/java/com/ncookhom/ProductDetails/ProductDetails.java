package com.ncookhom.ProductDetails;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.ncookhom.Card.MyCard;
import com.ncookhom.FamilyProducts.ProdImagesModel;
import com.android.volley.toolbox.Volley;
import com.ncookhom.MainActivity;
import com.ncookhom.MyProducts.HomeModel;
import com.ncookhom.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductDetails extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    TextView back, card, order_num, prod_name, prod_name2, prod_category, prod_desc, prod_price, prod_time;

    ArrayList<String> layouts = new ArrayList<>();
    ImageView increase_num, decrease_num;
    RatingBar ratingBar;
    Button order_prod_btn;
    TextView publish_rate, comments;
    EditText comment_ed;

    public static int random_num;
    public static String random_num_file = "random_num";

    SliderLayout sliderLayout;

    private String rate_url = "http://cookehome.com/CookApp/User/Rate.php";

    private String comment;
    private float rate_num;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        closeKeyboard();
        sliderLayout = findViewById(R.id.banner_slider);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        Intent intent = getIntent();
        final ProdImagesModel list = intent.getParcelableExtra("Imageslist");
        final HomeModel prod_data = intent.getParcelableExtra("product_data");
        final String rate_val = intent.getStringExtra("rate_val");

//        Toast.makeText(this, "" + prod_data.getFamily_id(), Toast.LENGTH_SHORT).show();
        if (!list.getProd_img1().equals("0")) {
            layouts.add("http://cookehome.com/CookApp/images/" + list.getProd_img1());
        }
        if (!list.getProd_img2().equals("0")) {
            layouts.add("http://cookehome.com/CookApp/images/" + list.getProd_img2());
        }
        if (!list.getProd_img3().equals("0")) {
            layouts.add("http://cookehome.com/CookApp/images/" + list.getProd_img3());
        }
        if (!list.getProd_img4().equals("0")) {
            layouts.add("http://cookehome.com/CookApp/images/" + list.getProd_img4());
        }
        if (!list.getProd_img5().equals("0")) {
            layouts.add("http://cookehome.com/CookApp/images/" + list.getProd_img5());
        }

        back = findViewById(R.id.back);
        order_num = findViewById(R.id.prod_num_order);
        order_num.setTypeface(custom_font);
        increase_num = findViewById(R.id.increase_num);
        decrease_num = findViewById(R.id.decrease_num);
        prod_name = findViewById(R.id.prod_title);
        prod_name.setTypeface(custom_font);
        prod_name2 = findViewById(R.id.prod_title2);
        prod_name2.setTypeface(custom_font);
        prod_category = findViewById(R.id.prod_category);
        prod_category.setTypeface(custom_font);
        prod_desc = findViewById(R.id.prod_desc);
        prod_desc.setTypeface(custom_font);
        prod_price = findViewById(R.id.prod_price);
        prod_price.setTypeface(custom_font);
        prod_time = findViewById(R.id.prod_time);
        ratingBar = findViewById(R.id.rate_stars_num_id);
        order_prod_btn = findViewById(R.id.add_card_btn_id);
        publish_rate = findViewById(R.id.publish_your_comment_btn);
        comments = findViewById(R.id.all_comments_btn);
        comment_ed = findViewById(R.id.comment_ed_id);
        card = findViewById(R.id.card);

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductDetails.this, MyCard.class));
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        for (String name : layouts) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView.image(name)
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(4000);
        sliderLayout.addOnPageChangeListener(this);

        // set data to views ...
        prod_name.setText(prod_data.getFamily_name());
        prod_name2.setText(prod_data.getFamily_name());
        prod_category.setText(prod_data.getFood_type());
        prod_desc.setText(prod_data.getDesc());
        prod_price.setText(prod_data.getPrice());
        if (String.valueOf(prod_data.getFamily_rate()) != null && String.valueOf(prod_data.getFamily_rate()).length() > 0) {
            float d = ((Float.parseFloat(String.valueOf(prod_data.getFamily_rate())) * 5) / 100);
            ratingBar.setRating(d);
        }

        if (prod_data.getTime().equals("0")) {
            prod_time.setText("جاهزة");
        } else {
            prod_time.setText(prod_data.getTime());
        }

        increase_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = Integer.parseInt(order_num.getText().toString());
                num = num + 1;
                order_num.setText(String.valueOf(num));
                float quan_price = Float.parseFloat(prod_data.getPrice());
                prod_price.setText(String.valueOf(num * quan_price));
            }
        });
        decrease_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = Integer.parseInt(order_num.getText().toString());
                if (num > 1) {
                    num = num - 1;
                    order_num.setText(String.valueOf(num));
                    float quan_price = Float.parseFloat(prod_data.getPrice());
                    prod_price.setText(String.valueOf(num * quan_price));
                }
            }
        });


        publish_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rate_num = ratingBar.getRating();
                comment = comment_ed.getText().toString().trim();
                if (rate_num == 0.0) {
                    Toast.makeText(ProductDetails.this, "برجاء تقييم المنتج!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(comment)) {
                    comment_ed.setError("برجاء كتابة التعليق اولا!");
                    comment_ed.requestFocus();
                    return;
                } else {
                    StringRequest rate_request = new StringRequest(Request.Method.POST, rate_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int flag = jsonObject.getInt("flag");
                                    if (flag == 1) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetails.this);
                                        builder.setMessage("تم تقيم المنتج بنجاح")
                                                .setCancelable(false)
                                                .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.cancel();
                                                        Intent intent1 = new Intent(ProductDetails.this, Comments.class);
                                                        intent1.putExtra("product_id", prod_data.getFamily_id());
                                                        startActivity(intent1);
                                                    }
                                                }).create().show();
                                    } else if (flag == 0) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetails.this);
                                        builder.setMessage("تم تقيم هذا المنتج من قبل")
                                                .setCancelable(false)
                                                .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.cancel();
                                                    }
                                                }).create().show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            error.getMessage();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("product_id", prod_data.getFamily_id());
                            params.put("rate", rate_num + "");
                            params.put("user_id", MainActivity.customer_id);
                            params.put("user_name", MainActivity.Name);
                            params.put("user_mail", MainActivity.email);
                            params.put("comment", comment);
                            return params;
                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(ProductDetails.this);
                    queue.add(rate_request);
                    closeKeyboard();
                }
            }
        });

        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ProductDetails.this, Comments.class);
                intent1.putExtra("product_id", prod_data.getFamily_id());
                startActivity(intent1);
            }
        });
        order_prod_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.Name.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetails.this);
                    builder.setMessage("قم بتسجيل الدخول اولا")
                            .setCancelable(false)
                            .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }).create().show();
                } else if (MainActivity.customer_id.equals(prod_data.getSeller_id())) {
                    Toast.makeText(ProductDetails.this, "لا يمكن شراء المنتج!", Toast.LENGTH_SHORT).show();
                } else {
                    Response.Listener<String> listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    Toast.makeText(ProductDetails.this, "تم اضافة المنتج الى السلة", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    OrderRequest orderRequest = new OrderRequest(prod_data.getFamily_name(), prod_data.getMeal_image(), prod_data.getFood_type(), order_num.getText().toString(), random_num + "", prod_data.getPrice(), prod_data.getSeller_id(), prod_data.getSeller_name(), prod_data.getSeller_email(), MainActivity.customer_id, MainActivity.Name, MainActivity.email, MainActivity.phone, listener);
                    RequestQueue queue = Volley.newRequestQueue(ProductDetails.this);
                    queue.add(orderRequest);
                }
            }
        });
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}