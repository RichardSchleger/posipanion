package sk.richardschleger.posipanion;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import sk.richardschleger.posipanion.databinding.ActivityMainBinding;

public class MainActivity extends Activity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPref = getSharedPreferences("PosiPanionSharedPrefferences", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "");
        if(token != ""){
            binding.loginContainer.setVisibility(View.INVISIBLE);
            binding.mainViewContainer.setVisibility(View.VISIBLE);
        }else{
            binding.loginContainer.setVisibility(View.VISIBLE);
            binding.mainViewContainer.setVisibility(View.INVISIBLE);
        }

    }

    public void onLoginClick(View view){
        String code = binding.editTextNumberPassword4.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://104.248.142.7:8080/verifycode",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String token = jsonObject.get("token").toString();
                            if(token != ""){
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

    public void onLogout(View view){

        SharedPreferences sharedPref = getSharedPreferences("PosiPanionSharedPrefferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("token");
        editor.apply();

        binding.loginContainer.setVisibility(View.VISIBLE);
        binding.mainViewContainer.setVisibility(View.INVISIBLE);

    }

    public void onRideStart(View view){
        binding.startRideButton.setVisibility(View.INVISIBLE);
        binding.endRideButton.setVisibility(View.VISIBLE);
    }

    public void onRideEnd(View view){
        binding.startRideButton.setVisibility(View.VISIBLE);
        binding.endRideButton.setVisibility(View.INVISIBLE);
    }
}