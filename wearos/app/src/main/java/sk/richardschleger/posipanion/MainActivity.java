package sk.richardschleger.posipanion;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import sk.richardschleger.posipanion.databinding.ActivityMainBinding;

public class MainActivity extends Activity {

    private ActivityMainBinding binding;

    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private RequestQueue queue;
    private FusedLocationProviderClient locationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.queue = Volley.newRequestQueue(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPref = getSharedPreferences("PosiPanionSharedPrefferences", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "");
        if (token != "") {
            binding.loginContainer.setVisibility(View.INVISIBLE);
            binding.mainViewContainer.setVisibility(View.VISIBLE);
        } else {
            binding.loginContainer.setVisibility(View.VISIBLE);
            binding.mainViewContainer.setVisibility(View.INVISIBLE);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1000);
        }else {
            this.locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0 ||
                        grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    System.exit(0);
                }else{
                    locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                }
                break;
            }
        }
    }

    public void onLoginClick(View view) {
        String code = binding.editTextNumberPassword4.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://104.248.142.7:8080/verifycode",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String token = jsonObject.get("token").toString();
                            if (token != "") {
                                System.out.println(token);
                                SharedPreferences sharedPref = getSharedPreferences("PosiPanionSharedPrefferences", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("token", token);
                                editor.apply();

                                binding.loginContainer.setVisibility(View.INVISIBLE);
                                binding.mainViewContainer.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            System.out.println(e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("code", code);
                return params;
            }
        };

        queue.add(stringRequest);

    }

    public void onLogout(View view) {

        SharedPreferences sharedPref = getSharedPreferences("PosiPanionSharedPrefferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("token");
        editor.apply();

        binding.loginContainer.setVisibility(View.VISIBLE);
        binding.mainViewContainer.setVisibility(View.INVISIBLE);

    }

    public void onRideStart(View view) {
        binding.startRideButton.setVisibility(View.INVISIBLE);
        binding.endRideButton.setVisibility(View.VISIBLE);

        sendStartRideRequest();

        System.out.println("Ride started");

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5 * 1000).setFastestInterval(5 * 1000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    System.out.println("Location Result is null");
                    return;
                }
                Location lastLocation = locationResult.getLastLocation();
                System.out.println(lastLocation.toString());
                SharedPreferences sharedPref = getSharedPreferences("PosiPanionSharedPrefferences", Context.MODE_PRIVATE);
                String token = sharedPref.getString("token", "");
                if (token != "") {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                            "http://104.248.142.7:8080/user/wearables/location",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    System.out.println(response);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.networkResponse.statusCode == 606) {
                                refreshToken(token);
                            }
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("latitude", lastLocation.getLatitude() + "");
                            params.put("longitude", lastLocation.getLongitude() + "");
                            params.put("elevation", lastLocation.getAltitude() + "");
                            params.put("timestamp", System.currentTimeMillis() + "");
                            return params;
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Authorization", "Bearer " + token);

                            return params;
                        }
                    };

                    queue.add(stringRequest);
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }


    public void onRideEnd(View view){
        binding.startRideButton.setVisibility(View.VISIBLE);
        binding.endRideButton.setVisibility(View.INVISIBLE);

        locationProviderClient.removeLocationUpdates(locationCallback);

        sendEndRideRequest();
    }

    private void refreshToken(String token){

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://104.248.142.7:8080/refreshtoken",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String newToken = jsonObject.get("token").toString();
                            if(newToken != ""){
                                SharedPreferences sharedPref = getSharedPreferences("PosiPanionSharedPrefferences", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("token", newToken);
                                editor.apply();
                            }
                        } catch (JSONException e) {
                            System.out.println(e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + token);
                params.put("isRefreshToken", "true");
                return params;
            }
        };

        queue.add(stringRequest);
    }


    private void sendStartRideRequest(){

        SharedPreferences sharedPref = getSharedPreferences("PosiPanionSharedPrefferences", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "");
        if (token != "") {
            StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                    "http://104.248.142.7:8080/user/wearables/start",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse.statusCode == 606) {
                        refreshToken(token);
                        sendStartRideRequest();
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Bearer " + token);
                    return params;
                }
            };

            queue.add(stringRequest);
        }
    }


    private void sendEndRideRequest(){

        SharedPreferences sharedPref = getSharedPreferences("PosiPanionSharedPrefferences", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "");
        if (token != "") {
            StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                    "http://104.248.142.7:8080/user/end",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse.statusCode == 606) {
                        refreshToken(token);
                        sendEndRideRequest();
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Bearer " + token);
                    return params;
                }
            };

            queue.add(stringRequest);
        }
    }
}
