package com.example.firstdraft;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

    EditText username,password;
    Button login;
    RequestQueue requestQueue;

    String u, p;

    String cookie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        requestQueue = Volley.newRequestQueue(LoginActivity.this);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById((R.id.login));

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u = username.getText().toString();
                p = password.getText().toString();
                postData(requestQueue);
                /*Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra(EXTRA_TEXT, cookie);*/
            }
        });
    }
    public void openDashboard() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra(EXTRA_TEXT, cookie);
        System.out.print(cookie);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }
    public void postData(RequestQueue requestQueue){
        JSONObject objc = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            objc.put("db", "bitnami_odoo");
            objc.put("login",u);
            objc.put("password", p);

            object.put("params", objc);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        String url = getResources().getString(R.string.loginurl);
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
                            cookie = obj.getString("session_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(k.equals("result")) {
                            /*System.out.println(response);*/
                            Toast.makeText(getApplicationContext(), "Logging In", Toast.LENGTH_SHORT).show();
                            openDashboard();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_SHORT).show();
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
        };
        requestQueue.add(jsonObjectRequest);
    }
}