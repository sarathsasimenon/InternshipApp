package com.example.firstdraft;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_TEXT = "com.example.firstdraft.EXTRA_TEXT";
    public static final String EXTRA_TEXT2 = "com.example.firstdraft.EXTRA_TEXT2";
    public static final String EXTRA_TEXT3 = "com.example.firstdraft.EXTRA_TEXT3";
    public static final String EXTRA_TEXT5 = "com.example.firstdraft.EXTRA_TEXT5";

    private long backPressedTime;
    private Toast backToast;

    String cookie;
    String uid;

    String userid;
    String user;
    String project;

    TextView name;

    RequestQueue requestQueue;
    RequestQueue requestQueue1;

    ArrayList<String> al = new ArrayList<>();

    SharedPreferences sharedPreferences;

    private Spinner dropdown;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("Preferences",MODE_PRIVATE);
        cookie = sharedPreferences.getString("Cookie","");
        uid = sharedPreferences.getString("uid","");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid", uid);
        editor.commit();

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        postData(requestQueue);

        dropdown = (Spinner) findViewById(R.id.dropdown);

        requestQueue1 = Volley.newRequestQueue(MainActivity.this);

        al.add("Choose a client and respective order id");
        postData1(requestQueue1);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < al.size(); i++) {
                    if (parent.getItemAtPosition(position).equals(al.get(0))) {
                        // do nothing
                    } else {
                        String s = parent.getItemAtPosition(position).toString();
                        if (s.equals("Head Office")) {
                            Intent intent2 = new Intent(MainActivity.this,back_home.class);
                            intent2.putExtra(EXTRA_TEXT3,userid);
                            intent2.putExtra(EXTRA_TEXT5,user);
                            startActivity(intent2);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                        else {
                            Intent intent = new Intent(MainActivity.this, item_select.class);
                            intent.putExtra(EXTRA_TEXT, s);
                            /*intent.putExtra(EXTRA_TEXT2, address);*/
                            intent.putExtra(EXTRA_TEXT3,userid);
                            intent.putExtra(EXTRA_TEXT5,user);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });
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

    public void postData1(RequestQueue requestQueue) {
        String obj = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"call\",\n" +
                "    \"params\": {\n" +
                "        \"model\": \"project.project\",\n" +
                "        \"domain\": [],\n" +
                "        \"fields\": [\n" +
                "            \"name\",\n" +
                "            \"partner_id\",\n" +
                "            \"color\",\n" +
                "            \"task_count\",\n" +
                "            \"label_tasks\",\n" +
                "            \"alias_id\",\n" +
                "            \"alias_name\",\n" +
                "            \"alias_domain\",\n" +
                "            \"is_favorite\",\n" +
                "            \"percentage_satisfaction_project\",\n" +
                "            \"rating_status\"\n" +
                "        ],\n" +
                "        \"limit\": 80,\n" +
                "        \"sort\": \"\",\n" +
                "        \"context\": {\n" +
                "            \"lang\": \"en_US\",\n" +
                "            \"tz\": false,\n" +
                "            \"uid\": 2\n" +
                "        }\n" +
                "    },\n" +
                "    \"id\": 184535202\n" +
                "}";
        JSONObject object = null;
        try {
            object = new JSONObject(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String projecturl = "http://34.87.169.30/web/dataset/search_read";
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, projecturl, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                /*System.out.println(response);*/
                try {
                    JSONObject obj = response.getJSONObject("result");
                    JSONArray arr = obj.getJSONArray("records");
                    for(int i=0;i<arr.length();i++){
                        project = arr.getJSONObject(i).getString("name");
                        al.add(project);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, al);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dropdown.setAdapter(adapter);
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