package com.example.firstdraft;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_TEXT = "com.example.firstdraft.EXTRA_TEXT";
    public static final String EXTRA_TEXT2 = "com.example.firstdraft.EXTRA_TEXT2";
    public static final String EXTRA_TEXT3 = "com.example.firstdraft.EXTRA_TEXT3";
    public static final String EXTRA_TEXT4 = "com.example.firstdraft.EXTRA_TEXT4";
    public static final String EXTRA_TEXT5 = "com.example.firstdraft.EXTRA_TEXT5";

    private long backPressedTime;
    private Toast backToast;

    String baseurl;
    String cookie;
    String uid;

    String userid;
    String user;
    String projectid;
    String pid;
    String project;

    TextView name;

    RequestQueue requestQueue;
    RequestQueue requestQueue1;

    ArrayList<String> al = new ArrayList<>();
    ArrayList<String> al2 = new ArrayList<>();

    SharedPreferences sharedPreferences;

    private Spinner dropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("Preferences",MODE_PRIVATE);
        baseurl = sharedPreferences.getString("baseurl","");
        cookie = sharedPreferences.getString("Cookie","");
        uid = sharedPreferences.getString("uid","");
        user = sharedPreferences.getString("user","");

//        requestQueue = Volley.newRequestQueue(MainActivity.this);
//
//        postData(requestQueue);

        final Intent intent = getIntent();
        user = intent.getStringExtra(FirstActivity.EXTRA_TEXT);
        userid = intent.getStringExtra(FirstActivity.EXTRA_TEXT2);

        TextView name = (TextView) findViewById(R.id.name);
        name.setText(user);

        dropdown = (Spinner) findViewById(R.id.dropdown);

        requestQueue1 = Volley.newRequestQueue(MainActivity.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        al.add("Choose your destination");
        al2.add("Project ids");
        postData1(requestQueue1);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < al.size(); i++) {
                    if (parent.getItemAtPosition(position).equals(al.get(0))) {
                        // do nothing
                    } else {
                        String s = parent.getItemAtPosition(position).toString();
                        if (s.equals("Home Office")) {
                            int j = al.indexOf(s);
                            pid = al2.get(j);
                            Intent intent2 = new Intent(MainActivity.this,back_home.class);
                            intent2.putExtra(EXTRA_TEXT3,userid);
                            intent2.putExtra(EXTRA_TEXT4,pid);
                            intent2.putExtra(EXTRA_TEXT5,user);
                            startActivity(intent2);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
//                        if (s.equals("Attendance")) {
////                            int j = al.indexOf(s);
////                            pid = al2.get(j);
////                            Intent intent2 = new Intent(MainActivity.this,AttendanceActivity.class);
////                            intent2.putExtra(EXTRA_TEXT3,userid);
////                            intent2.putExtra(EXTRA_TEXT4,pid);
////                            intent2.putExtra(EXTRA_TEXT5,user);
////                            startActivity(intent2);
////                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                            continue;
//                        }
                        else {
                            int j = al.indexOf(s);
                            pid = al2.get(j);
                            Intent intent = new Intent(MainActivity.this, item_select.class);
                            intent.putExtra(EXTRA_TEXT, s);
                            /*intent.putExtra(EXTRA_TEXT2, address);*/
                            intent.putExtra(EXTRA_TEXT3,userid);
                            intent.putExtra(EXTRA_TEXT4,pid);
                            intent.putExtra(EXTRA_TEXT5,user);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("user",user);
                        editor.apply();
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
        Intent intent = new Intent(MainActivity.this,FirstActivity.class);
        startActivity(intent);
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
        String objc = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"call\",\n" +
                "    \"params\": {\n" +
                "        \"model\": \"project.project\",\n" +
                "        \"domain\": [\n" +
                "            [\n" +
                "                \"favorite_user_ids\",\n" +
                "                \"in\",\n" +
                "                2\n" +
                "            ]\n" +
                "        ],\n" +
                "        \"fields\": [\n" +
                "            \"name\",\n" +
                "            \"partner_id\",\n" +
                "            \"allow_timesheets\",\n" +
                "            \"color\",\n" +
                "            \"task_count\",\n" +
                "            \"label_tasks\",\n" +
                "            \"alias_id\",\n" +
                "            \"alias_name\",\n" +
                "            \"alias_domain\",\n" +
                "            \"is_favorite\",\n" +
                "            \"rating_percentage_satisfaction\",\n" +
                "            \"rating_status\",\n" +
                "            \"analytic_account_id\"\n" +
                "        ],\n" +
                "        \"limit\": 80,\n" +
                "        \"sort\": \"\",\n" +
                "        \"context\": {\n" +
                "            \"lang\": \"en_US\",\n" +
                "            \"tz\": \"Asia/Calcutta\",\n" +
                "            \"uid\": 2,\n" +
                "            \"allowed_company_ids\": [\n" +
                "                1\n" +
                "            ],\n" +
                "            \"bin_size\": true\n" +
                "        }\n" +
                "    },\n" +
                "    \"id\": 424942075\n" +
                "}";
        JSONObject object = null;
        try {
            if(baseurl.equals("https://inspiresupport.odoo.com/")){
                object = new JSONObject(objc);
            }
            else{
                object = new JSONObject(obj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(object);
        String projecturl = baseurl+"web/dataset/search_read";
        System.out.println(projecturl);
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, projecturl, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                try {
                    JSONObject obj = response.getJSONObject("result");
                    JSONArray arr = obj.getJSONArray("records");
                    for(int i=0;i<arr.length();i++){
                        projectid = arr.getJSONObject(i).getString("id");
                        project = arr.getJSONObject(i).getString("name");
                        al.add(project);
                        al2.add(projectid);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i=0;i<al.size();i++){
                    if(al.get(i).equals("Attendance")){
                        al.remove(i);
                        al2.remove(i);
                    }
                }
                System.out.println(al);
                System.out.println(al2);
                /*al.remove("Attendance");
                al2.remove("4");*/
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