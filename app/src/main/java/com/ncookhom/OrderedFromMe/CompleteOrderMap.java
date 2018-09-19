package com.ncookhom.OrderedFromMe;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.ncookhom.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class CompleteOrderMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String family_id, family_name;
    private static final float DEFAULT_ZOOM = 15f;

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    String markerText = "أنت";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    double lat, lng;
    private String family_Data_url = "http://cookehome.com/CookApp/User/SearchUser.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_order_map);

        if (getIntent().getStringExtra("family_id") != null) {
            family_id = getIntent().getStringExtra("family_id");
            family_name = getIntent().getStringExtra("family_name");
        }
        StringRequest family_data_request = new StringRequest(Request.Method.POST, family_Data_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        lat = Double.parseDouble(jsonObject.getString("Lat"));
                        lng = Double.parseDouble(jsonObject.getString("Lan"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CompleteOrderMap.this, "لا يوجد اتصال بالانترنت", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ID", family_id);
                return params;
            }
        };
        Volley.newRequestQueue(CompleteOrderMap.this).add(family_data_request);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(CompleteOrderMap.this, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(CompleteOrderMap.this, COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(CompleteOrderMap.this);
                try {
                    if (mLocationPermissionsGranted) {
                        final Task location = mFusedLocationProviderClient.getLastLocation();
                        location.addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
//                                            Log.d(TAG, "onComplete: found location!");
                                    Location currentLocation = (Location) task.getResult();

                                    //add marker to my location
                                    LatLng latLng1 = new LatLng(lat, lng);
                                    Marker market_marker = googleMap.addMarker(new MarkerOptions().position(latLng1).title(family_name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

                                    //add marker to my location
                                    LatLng latLng2 = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                                    Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng2).title(markerText));

                                    //zoom to position with level 16
                                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng2, DEFAULT_ZOOM);
                                    googleMap.animateCamera(cameraUpdate);

                                } else {
                                    Toast.makeText(CompleteOrderMap.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } catch (SecurityException e) {
                    Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
                }
            }
        } else {
            ActivityCompat.requestPermissions(CompleteOrderMap.this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

}