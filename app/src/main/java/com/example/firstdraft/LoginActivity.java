package com.example.firstdraft;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    public static final String EXTRA_TEXT = "com.example.firstdraft.EXTRA_TEXT";
    public static final String EXTRA_TEXT2 = "com.example.firstdraft.EXTRA_TEXT2";

    private long backPressedTime;
    private Toast backToast;

    EditText username,password;
    Button login;
    RequestQueue requestQueue;

    String u, p;

    String uid;

    String rawCookies;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        requestQueue = Volley.newRequestQueue(LoginActivity.this);

        sharedPreferences = getSharedPreferences("Preferences", MODE_PRIVATE);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById((R.id.login));

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u = username.getText().toString();
                p = password.getText().toString();
                postData(requestQueue);
            }
        });
    }
    public void openDashboard() {
        Intent intent = new Intent(this,FirstActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }
    @Override
    public void onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            finish();
            return;
        }
        else{
            backToast = Toast.makeText(getBaseContext(),"Press back again to exit",Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
    public void postData(RequestQueue requestQueue){
        JSONObject objc = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            objc.put("db", "Deceler_test_15-july-2020");
            objc.put("login",u);
            objc.put("password",p);

            object.put("params", objc);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "http://34.87.169.30/web/session/authenticate/";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray key = response.names();
                        String k = null;
                        try {
                            k = key.getString(2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONObject obj = response.getJSONObject("result");
                            JSONObject obj2 = obj.getJSONObject("user_context");
                            uid = obj2.getString("uid");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(k.equals("result")) {
                            Toast.makeText(getApplicationContext(), "Logging In", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            int flag1 = 10;
                            editor.putBoolean("loggedin",true);
                            editor.putBoolean("journeyover",true);
                            editor.putBoolean("journeyhomeover",true);
                            editor.putBoolean("checkedout",true);
                            editor.putInt("flag",flag1);
                            editor.apply();
                            openDashboard();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_SHORT).show();
                            int flag1 = 100;
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("flag",flag1);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                        Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_SHORT).show();
                    }
            }) {
            @Override
            public Map<String,String> getHeaders() {
                HashMap<String,String> headers = new HashMap<String,String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> responseHeaders = response.headers;
                rawCookies = responseHeaders.get("Set-Cookie");
                return super.parseNetworkResponse(response);
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
    @Override
    protected void onPause(){
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Cookie", rawCookies);
        editor.putString("uid",uid);
        editor.commit();
    }
}