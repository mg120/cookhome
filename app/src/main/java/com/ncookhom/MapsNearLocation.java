package com.ncookhom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ncookhom.FamilyProducts.FamilyProducts;
import com.ncookhom.NavFragments.FamilyModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsNearLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String map_users_data_url = "http://cookehome.com/CookApp/ShowData/Users/showIOS.php";
    private TextView back;

    ArrayList<FamilyModel> familyModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_near_location);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        StringRequest map_data_request = new StringRequest(Request.Method.GET, map_users_data_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data_obj = jsonArray.getJSONObject(i);
                                final String family_id = data_obj.getString("ID");
                                final String family_name = data_obj.getString("Name");
                                final String family_email = data_obj.getString("Email");
                                final String family_img = data_obj.getString("img");
                                final String lat = data_obj.getString("Lat");
                                final String lan = data_obj.getString("Lan");
                                final String family_address = data_obj.getString("Address");

                                LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lan));
                                mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lan))).title(family_name));

                                familyModels.add(new FamilyModel(family_id, family_name, family_email, family_img, lat, lan, family_address));

                            }
                            LatLng coordinate = new LatLng(Double.parseDouble("24.774265"), Double.parseDouble("46.738586")); //Store these lat lng values somewhere. These should be constant.
                            CameraUpdate location = CameraUpdateFactory.newLatLngZoom(coordinate, 5);
                            mMap.animateCamera(location);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsNearLocation.this);
                builder.setMessage("تعذر تحديد الاماكن,لايوجد اتصال بالانترنت")
                        .setCancelable(false)
                        .setPositiveButton("الغاء", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).create().show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(MapsNearLocation.this);
        requestQueue.add(map_data_request);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                int pos = Integer.parseInt(marker.getId().replaceAll("\\D+", ""));
                Intent intent = new Intent(MapsNearLocation.this, FamilyProducts.class);
                intent.putExtra("family_id", familyModels.get(pos).getFamily_id());
                startActivity(intent);
            }
        });
        mMap.setMyLocationEnabled(true);
    }
}
