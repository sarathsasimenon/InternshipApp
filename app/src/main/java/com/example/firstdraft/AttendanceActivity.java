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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    String projectid;
    String uid;
    String result;

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
                try {
                    postData(requestQueue);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
    public void postData(RequestQueue requestQueue) throws JSONException {
        String obj = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"call\",\n" +
                "    \"params\": {\n" +
                "        \"args\": [\n" +
                "            {\n" +
                "                \"employee_id\":" + userid + ",\"check_in\":\""+ time1 +"\",\"check_out\": false,\n" +
                "                \"hr_project_id\": \""+ 4 +"\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"model\": \"hr.attendance\",\n" +
                "        \"method\": \"create\",\n" +
                "        \"kwargs\": {\n" +
                "            \"context\": {\n" +
                "                \"lang\": \"en_US\",\n" +
                "                \"tz\": \"Asia/Kolkata\",\n" +
                "                \"uid\": "+uid+",\n" +
                "                \"params\": {\n" +
                "                    \"action\": 433,\n" +
                "                    \"model\": \"hr.attendance\",\n" +
                "                    \"view_type\": \"form\",\n" +
                "                    \"menu_id\": 292\n" +
                "                },\n" +
                "                \"search_default_today\": 1\n" +
                "            }\n" +
                "        }\n" +
                "    },\n" +
                "    \"id\": 313482682\n" +
                "}";
        JSONObject object = new JSONObject(obj);
        String starturl = baseurl+"/web/dataset/call_kw/hr.attendance/create";
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