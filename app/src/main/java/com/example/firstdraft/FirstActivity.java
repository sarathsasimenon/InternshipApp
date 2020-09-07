package com.example.firstdraft;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FirstActivity extends AppCompatActivity {
    public static final String EXTRA_TEXT = "com.example.firstdraft.EXTRA_TEXT";
    public static final String EXTRA_TEXT2 = "com.example.firstdraft.EXTRA_TEXT2";
    public static final String EXTRA_TEXT3 = "com.example.firstdraft.EXTRA_TEXT3";
    public static final String EXTRA_TEXT4 = "com.example.firstdraft.EXTRA_TEXT4";
    public static final String EXTRA_TEXT5 = "com.example.firstdraft.EXTRA_TEXT5";

    private long backPressedTime;
    private Toast backToast;

    String cookie;
    String uid;

    String userid;
    String user;

    TextView name;

    Button attendance;
    Button destination;

    RequestQueue requestQueue;
//    RequestQueue requestQueue1;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        sharedPreferences = getSharedPreferences("Preferences",MODE_PRIVATE);
        cookie = sharedPreferences.getString("Cookie","");
        uid = sharedPreferences.getString("uid","");

        requestQueue = Volley.newRequestQueue(FirstActivity.this);
        postData(requestQueue);

        attendance = (Button) this.findViewById(R.id.attend);
        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this,AttendanceActivity.class);
                intent.putExtra(EXTRA_TEXT,user);
                intent.putExtra(EXTRA_TEXT2,userid);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        destination = (Button) this.findViewById(R.id.destination);
        destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(FirstActivity.this, MainActivity.class);
                intent2.putExtra(EXTRA_TEXT,user);
                intent2.putExtra(EXTRA_TEXT2,userid);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user",user);
        editor.apply();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.signout) {
            Intent intent = new Intent(FirstActivity.this,LoginActivity.class);
            startActivity(intent);
            logout();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void logout(){
        sharedPreferences = getSharedPreferences("Preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("loggedin",false);
        System.out.println("Logged Out");
        editor.apply();
    }
    @Override
    public void onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            onPause();
            return;
        }
        else{
            backToast = Toast.makeText(getBaseContext(),"Press back again to exit",Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
    public void postData(RequestQueue requestQueue) {
        String obj = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"call\",\n" +
                "    \"params\": {\n" +
                "        \"args\": [\n" +
                "            [\n" +
                "                [\n" +
                "                    \"user_id\",\n" +
                "                    \"=\",\n" +
                uid +
                "                ]\n" +
                "            ],\n" +
                "            [\n" +
                "                \"attendance_state\",\n" +
                "                \"name\"\n" +
                "            ]\n" +
                "        ],\n" +
                "        \"model\": \"hr.employee\",\n" +
                "        \"method\": \"search_read\",\n" +
                "        \"kwargs\": {}\n" +
                "    },\n" +
                "    \"id\": 49458607\n" +
                "}";
        JSONObject object = null;
        try {
            object = new JSONObject(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String nameurl = "http://34.87.169.30/web/dataset/call_kw/hr.employee/search_read";
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, nameurl, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                /*System.out.println(response);*/
                try {
                    JSONArray arr = response.getJSONArray("result");
                    JSONObject obj = arr.getJSONObject(0);
                    userid = obj.getString("id");
                    user = obj.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                name = (TextView) findViewById(R.id.name);
                name.setText(user);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Cookie",cookie);
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        requestQueue.add(customRequest);
    }
}