package com.ncookhom.NavFragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ncookhom.MainActivity;
import com.ncookhom.NavFragments.Model.PicModel;
import com.ncookhom.NavFragments.networking.RetroWeb;
import com.ncookhom.NavFragments.networking.ServiceApi;
import com.ncookhom.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import gun0912.tedbottompicker.TedBottomPicker;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;


public class AddProduct extends Fragment implements ProgressRequestBody.UploadCallbacks {

    EditText meal_name_ed, meal_price_ed, meal_desc_ed;
    LinearLayout time_picker_layout;
    Button add_meal_images;
    ImageView imageView1, imageView2, imageView3, imageView4, imageView5;
    Button add_meal_btn;
    Spinner meal_type_spinner, order_type_spinner, order_hours_time, order_minutes_time;
    String name, foodtype, order_type_selected_item, time, price, description;
    String minutes_time, hours_time;
    ArrayList<Uri> images = new ArrayList<>();
    ArrayAdapter<String> hours_spinner_adapter;

    ProgressDialog progressDialog;
    MultipartBody.Part mPartImageOne, mPartImageTwo, mPartImageThree, mPartImageFour, mPartImageFive;
    String meal_type = "http://cookehome.com/CookApp/ShowData/Users/showDepartments.php";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("اضافة اكلة");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("اضافة اكلة");
        return inflater.inflate(R.layout.fragment_add_meal, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/font.ttf");
        meal_name_ed = getActivity().findViewById(R.id.add_meal_name);
        meal_name_ed.setTypeface(custom_font);
        meal_type_spinner = getActivity().findViewById(R.id.meal_type);
        order_type_spinner = getActivity().findViewById(R.id.order_type);
        order_hours_time = getActivity().findViewById(R.id.order_hours_time);
        order_minutes_time = getActivity().findViewById(R.id.order_minutes_time);
        meal_price_ed = getActivity().findViewById(R.id.add_meal_price);
        meal_price_ed.setTypeface(custom_font);
        meal_desc_ed = getActivity().findViewById(R.id.meal_description);
        meal_desc_ed.setTypeface(custom_font);
        add_meal_images = getActivity().findViewById(R.id.add_prod_imgs);
        add_meal_btn = getActivity().findViewById(R.id.add_product_btn);
        imageView1 = getActivity().findViewById(R.id.imageView);
        imageView2 = getActivity().findViewById(R.id.imageView2);
        imageView3 = getActivity().findViewById(R.id.imageView3);
        imageView4 = getActivity().findViewById(R.id.imageView4);
        imageView5 = getActivity().findViewById(R.id.imageView5);

        time_picker_layout = getActivity().findViewById(R.id.time_picker_layout);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }

        };
        StringRequest stringRequest = new StringRequest(Request.Method.GET, meal_type, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");

                        adapter.add(name);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error Connection", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(getActivity()).add(stringRequest);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("اختر نوع الاكلة");
        meal_type_spinner.setAdapter(adapter);
        meal_type_spinner.setSelection(adapter.getCount()); //display hint
        meal_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                foodtype = meal_type_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // --------------------------------------------------------------------
        // second spinner .....
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }
        };
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.add("جاهز");
        adapter2.add("عند الطلب");
        adapter2.add("حالة التجهيز");
        order_type_spinner.setAdapter(adapter2);
        order_type_spinner.setSelection(adapter2.getCount()); //display hint
        order_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                order_type_selected_item = order_type_spinner.getSelectedItem().toString();
                if (order_type_selected_item.equals("عند الطلب")) {
                    time_picker_layout.setVisibility(View.VISIBLE);
                } else if (order_type_selected_item.equals("جاهز")) {
                    time_picker_layout.setVisibility(View.GONE);
                    time = "0";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        add_meal_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPickImageWithPermission();
            }
        });


        // Minutes spinner .....
        ArrayAdapter<String> minutes_spinner_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }

        };
        minutes_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minutes_spinner_adapter.add("00");
        minutes_spinner_adapter.add("01");
        minutes_spinner_adapter.add("02");
        minutes_spinner_adapter.add("03");
        minutes_spinner_adapter.add("04");
        minutes_spinner_adapter.add("05");
        minutes_spinner_adapter.add("06");
        minutes_spinner_adapter.add("07");
        minutes_spinner_adapter.add("08");
        minutes_spinner_adapter.add("09");
        minutes_spinner_adapter.add("10");
        minutes_spinner_adapter.add("11");
        minutes_spinner_adapter.add("12");
        minutes_spinner_adapter.add("13");
        minutes_spinner_adapter.add("14");
        minutes_spinner_adapter.add("15");
        minutes_spinner_adapter.add("16");
        minutes_spinner_adapter.add("17");
        minutes_spinner_adapter.add("18");
        minutes_spinner_adapter.add("19");
        minutes_spinner_adapter.add("20");
        minutes_spinner_adapter.add("21");
        minutes_spinner_adapter.add("22");
        minutes_spinner_adapter.add("23");
        minutes_spinner_adapter.add("24");
        minutes_spinner_adapter.add("25");
        minutes_spinner_adapter.add("26");
        minutes_spinner_adapter.add("27");
        minutes_spinner_adapter.add("28");
        minutes_spinner_adapter.add("29");
        minutes_spinner_adapter.add("30");
        minutes_spinner_adapter.add("31");
        minutes_spinner_adapter.add("32");
        minutes_spinner_adapter.add("33");
        minutes_spinner_adapter.add("34");
        minutes_spinner_adapter.add("35");
        minutes_spinner_adapter.add("36");
        minutes_spinner_adapter.add("37");
        minutes_spinner_adapter.add("38");
        minutes_spinner_adapter.add("39");
        minutes_spinner_adapter.add("40");
        minutes_spinner_adapter.add("41");
        minutes_spinner_adapter.add("42");
        minutes_spinner_adapter.add("43");
        minutes_spinner_adapter.add("44");
        minutes_spinner_adapter.add("45");
        minutes_spinner_adapter.add("46");
        minutes_spinner_adapter.add("47");
        minutes_spinner_adapter.add("48");
        minutes_spinner_adapter.add("49");
        minutes_spinner_adapter.add("50");
        minutes_spinner_adapter.add("51");
        minutes_spinner_adapter.add("52");
        minutes_spinner_adapter.add("53");
        minutes_spinner_adapter.add("54");
        minutes_spinner_adapter.add("55");
        minutes_spinner_adapter.add("56");
        minutes_spinner_adapter.add("57");
        minutes_spinner_adapter.add("58");
        minutes_spinner_adapter.add("59");
        minutes_spinner_adapter.add("دقيقة");
        order_minutes_time.setAdapter(minutes_spinner_adapter);
        order_minutes_time.setSelection(minutes_spinner_adapter.getCount()); //display hint
        order_minutes_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                minutes_time = order_minutes_time.getSelectedItem().toString();

                Log.d("minutes: ", minutes_time);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Hours spinner .....
        hours_spinner_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }
        };
        hours_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hours_spinner_adapter.add("00");
        hours_spinner_adapter.add("01");
        hours_spinner_adapter.add("02");
        hours_spinner_adapter.add("03");
        hours_spinner_adapter.add("04");
        hours_spinner_adapter.add("05");
        hours_spinner_adapter.add("06");
        hours_spinner_adapter.add("07");
        hours_spinner_adapter.add("08");
        hours_spinner_adapter.add("09");
        hours_spinner_adapter.add("10");
        hours_spinner_adapter.add("11");
        hours_spinner_adapter.add("12");
        hours_spinner_adapter.add("ساعة");
        order_hours_time.setAdapter(hours_spinner_adapter);
        order_hours_time.setSelection(hours_spinner_adapter.getCount()); //display hint
        order_hours_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hours_time = order_hours_time.getSelectedItem().toString();
                if (hours_time.equals("ساعة")) {
                    hours_time = "00";
                }
                Log.d("hours: ", hours_time);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        add_meal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_meal_info();
            }
        });
    }

    private void add_meal_info() {
        name = meal_name_ed.getText().toString();
        price = meal_price_ed.getText().toString();
        description = meal_desc_ed.getText().toString();

        if (images == null || images.size() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
            meal_name_ed.setError("هذا الحقل مطلوب");
            meal_name_ed.requestFocus();
            return;
        }
        if (foodtype.equals("اختر نوع الاكلة")) {
            Toast.makeText(getActivity(), "اختر نوع الاكلة اولا", Toast.LENGTH_SHORT).show();
            return;
        }

        if (order_type_selected_item.equals("حالة التجهيز")) {
            Toast.makeText(getActivity(), "اختر حالة الطلب", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(price)) {
            meal_price_ed.setError("هذا الحقل مطلوب");
            meal_price_ed.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(description)) {
            meal_desc_ed.setError("هذا الحقل مطلوب");
            meal_desc_ed.requestFocus();
            return;
        }

        if (time_picker_layout.getVisibility() == View.VISIBLE) {
            // Its visible
//            Toast.makeText(getActivity(), "Time layout Visible", Toast.LENGTH_SHORT).show();
            if (minutes_time.equals("دقيقة")) {
                Toast.makeText(getActivity(), "اختر مدة التحضير", Toast.LENGTH_SHORT).show();
                return;
            }
            time = hours_time + ":" + minutes_time;
        } else {
            // Either gone or invisible

        }

        createOrder();
    }

    public void getPickImageWithPermission() {
        if (PermissionUtils.canMakeSmores(Build.VERSION_CODES.LOLLIPOP_MR1)) {
            if (!PermissionUtils.hasPermissions(getActivity(), PermissionUtils.IMAGE_PERMISSIONS)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PermissionUtils.IMAGE_PERMISSIONS, 100);
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
        TedBottomPicker bottomSheetDialogFragment = new TedBottomPicker.Builder(getActivity())
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
        bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager());
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
        showProgressDialog(R.string.please_wait);

        RequestBody NamePart = RequestBody.create(MultipartBody.FORM, name);
        RequestBody foodtype_Part = RequestBody.create(MultipartBody.FORM, foodtype);
        RequestBody time_Part = RequestBody.create(MultipartBody.FORM, time);
        RequestBody price_Part = RequestBody.create(MultipartBody.FORM, price);
        RequestBody description_Part = RequestBody.create(MultipartBody.FORM, description);
        RequestBody cus_id_part = RequestBody.create(MultipartBody.FORM, MainActivity.customer_id);
        RequestBody cus_name_part = RequestBody.create(MultipartBody.FORM, MainActivity.Name);
        RequestBody cus_email_part = RequestBody.create(MultipartBody.FORM, MainActivity.email);

        RetroWeb.getClient().create(ServiceApi.class)
                .uploadImage(NamePart, foodtype_Part, time_Part, price_Part,
                        description_Part, cus_id_part, cus_name_part, cus_email_part, mPartImageOne, mPartImageTwo, mPartImageThree,
                        mPartImageFour, mPartImageFive)
                .enqueue(new Callback<PicModel>() {
                    @Override
                    public void onResponse(Call<PicModel> call, retrofit2.Response<PicModel> response) {

                        PicModel picModel = response.body();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("تم اضافة المنتج بنجاح")
                                .setCancelable(false)
                                .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        HomeFragment fragment = new HomeFragment();
                                        displaySelectedFragment(fragment);

                                    }
                                }).create().show();
                        hideProgressDialog();
                    }

                    @Override
                    public void onFailure(Call<PicModel> call, Throwable t) {
                        t.printStackTrace();
                        hideProgressDialog();
                    }
                });
    }


    protected void showProgressDialog(int message) {
        progressDialog = DialogUtil.showProgressDialog(getActivity(), getString(message), false);
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

    private void displaySelectedFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}