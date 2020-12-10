package com.example.firstdraft;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class AttendanceActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    String cookie;
    String baseurl;
    private Button checkin;
    String time1;
    long m;
    String userid;
    String user;
    String project;
    String projectid;
    String pid;
    String uid;
    String result;

    ArrayList<String> al = new ArrayList<>();
    ArrayList<String> al2 = new ArrayList<>();

    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        sharedPreferences = getSharedPreferences("Preferences",MODE_PRIVATE);
        sharedPreferences1 = getSharedPreferences(String.valueOf(R.string.pref_file_key),MODE_PRIVATE);
        baseurl = sharedPreferences.getString("baseurl","");
        cookie = sharedPreferences.getString("Cookie","");
        uid = sharedPreferences.getString("uid","");
//        user = sharedPreferences.getString("user","");

        requestQueue = Volley.newRequestQueue(AttendanceActivity.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        m = System.currentTimeMillis();
        time1 = time(m);

        final Intent intent = getIntent();
//        projectid = intent.getStringExtra(MainActivity.EXTRA_TEXT4);
        user = intent.getStringExtra(FirstActivity.EXTRA_TEXT);
        userid = intent.getStringExtra(FirstActivity.EXTRA_TEXT2);

        TextView name = (TextView) findViewById(R.id.name);
        name.setText(user);

        checkin = (Button) this.findViewById(R.id.checkin);
        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("journeyover",true);
                editor.putBoolean("journeyhomeover",true);
                editor.putBoolean("checkedout",false);
                editor.apply();
                postData1(requestQueue);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AttendanceActivity.this,FirstActivity.class);
        startActivity(intent);
    }
    public String time(long milliseconds) {
        Date currentDate = new Date(milliseconds);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.format(currentDate);
    }
    public void postData1(final RequestQueue requestQueue) {
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
        String projecturl = baseurl+"web/dataset/search_read";
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
                    if(!al.get(i).equals("Attendance")){
                        al.remove(i);
                        al2.remove(i);
                    }
                }
                System.out.println(al);
                System.out.println(al2);
                pid = al2.get(0);
                postData(requestQueue);
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

    public void postData(RequestQueue requestQueue) {
        String obj = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"call\",\n" +
                "    \"params\": {\n" +
                "        \"args\": [\n" +
                "            {\n" +
                "                \"employee_id\": " + userid +",\n" +
                "                \"check_in\": \""+ time1 +"\",\n" +
                "                \"check_out\": false,\n" +
                "                \"hr_project_id\": \""+pid+"\",\n" +
                "                \"gps_lat_check_in\": false,\n" +
                "                \"gps_lat_check_out\": false,\n" +
                "                \"gps_lang_check_in\": false,\n" +
                "                \"gps_lang_check_out\": false,\n" +
                "                \"duration_check_in\": false,\n" +
                "                \"duration_check_out\": false,\n" +
                "                \"dist_check_in\": false,\n" +
                "                \"dist_check_out\": false\n" +
                "            }\n" +
                "        ],\n" +
                "        \"model\": \"hr.attendance\",\n" +
                "        \"method\": \"create\",\n" +
                "        \"kwargs\": {\n" +
                "            \"context\": {\n" +
                "                \"lang\": \"en_US\",\n" +
                "                \"tz\": \"Asia/Kolkata\",\n" +
                "                \"uid\": " + uid + ",\n" +
                "                \"search_default_today\": 1\n" +
                "            }\n" +
                "        }\n" +
                "    },\n" +
                "    \"id\": 454093796\n" +
                "}";

        String objc = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"call\",\n" +
                "    \"params\": {\n" +
                "        \"args\": [\n" +
                "            {\n" +
                "                \"employee_id\": " + userid +",\n" +
                "                \"check_in\": \""+ time1 +"\",\n" +
                "                \"check_out\": false\n" +
                "            }\n" +
                "        ],\n" +
                "        \"model\": \"hr.attendance\",\n" +
                "        \"method\": \"create\",\n" +
                "        \"kwargs\": {\n" +
                "            \"context\": {\n" +
                "                \"lang\": \"en_US\",\n" +
                "                \"tz\": \"Asia/Calcutta\",\n" +
                "                \"uid\": " + uid + ",\n" +
                "                \"allowed_company_ids\": [\n" +
                "                    1\n" +
                "                ]\n" +
                "            }\n" +
                "        }\n" +
                "    },\n" +
                "    \"id\": 681963960\n" +
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
        String starturl = baseurl+"web/dataset/call_kw/hr.attendance/create";
        System.out.println(starturl);
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, starturl, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                try {
                    result = response.getString("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putLong("milli",m);
                editor.putString("userid",userid);
                editor.putString("user",user);
                editor.putString("result",result);
                editor.apply();
                final Intent intent1 = new Intent(AttendanceActivity.this, StopAttendance.class);
                startActivity(intent1);
                overridePendingTransition(0, 0);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
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
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}