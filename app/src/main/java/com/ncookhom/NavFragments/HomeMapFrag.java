package com.ncookhom.NavFragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ncookhom.FamilyProducts.FamilyProducts;
import com.ncookhom.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;


public class HomeMapFrag extends Fragment implements OnMapReadyCallback {

    MapView mMapView;
    private GoogleMap googleMap;
    private String home_map_url = "http://cookehome.com/CookApp/ShowData/Users/showIOS.php";

    LatLng Riyad;
    Double Lat, Lan;
    ArrayList<FamilyModel> familyModels = new ArrayList<>();

    ////////
    private GpsTracker gpsTracker;
    Handler handler;
    Runnable runnable;
    boolean running;

    private GoogleMap mMap;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    Marker marker;


    public HomeMapFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home_map, container, false);

        mMapView = rootView.findViewById(R.id.mapView_frag);
        mMapView.onCreate(savedInstanceState);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately
//        Lat = 24.7112816;
//        Lan = 46.6712844;
//        Riyad = new LatLng(Lat, Lan);
//        googleMap.addMarker(new MarkerOptions().position(Riyad).title("Marker Title").snippet("Marker Description"));

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap mMap) {
                googleMap = mMap;

                runtimer();

                StringRequest map_data_request = new StringRequest(Request.Method.GET, home_map_url, new Response.Listener<String>() {
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
                                googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lan))).title(family_name));
                                familyModels.add(new FamilyModel(family_id, family_name, family_email, family_img, lat, lan, family_address));
                            }

                            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                @Override
                                public void onInfoWindowClick(Marker marker) {
                                    int pos = Integer.parseInt((marker.getId()).replaceAll("\\D+", "")) - 1;
                                    Intent intent = new Intent(getActivity(), FamilyProducts.class);
                                    intent.putExtra("family_id", familyModels.get(pos).getFamily_id());
                                    startActivity(intent);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(map_data_request);

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
    }

    private void runtimer() {
        running = true;
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (running) {
                    getLocation();
                    Log.d("ok", "okkk");
                    Log.d("now:", "now..");
                }
                handler.postDelayed(this, 2800);
            }
        };
        handler.post(runnable);
    }

    private void getLocation() {
        gpsTracker = new GpsTracker(getActivity());
        if (gpsTracker.canGetLocation()) {
            if (marker != null) {
                marker.remove();
            }
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();

            LatLng latLng = new LatLng(latitude, longitude);
            Log.d("lat: ", latitude + "");
            Log.d("lng: ", longitude + "");

            marker = googleMap.addMarker(new MarkerOptions().position(latLng).draggable(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
            googleMap.setMaxZoomPreference(18f);
            CameraUpdate location = CameraUpdateFactory.newLatLngZoom(latLng, 16.6f);
            googleMap.animateCamera(location);

        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        running = false;

        // use this when you donot want to be called anymore..
//        handler.removeCallbacks(runnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        running = false;

        // use this when you donot want to be called anymore..
//        handler.removeCallbacks(runnable);
    }
}