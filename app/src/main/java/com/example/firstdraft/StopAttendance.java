package com.example.firstdraft;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class StopAttendance extends AppCompatActivity {
    RequestQueue requestQueue;
    String cookie;
    private Button checkout;
    String time1;
    String time2;
    long m;
    long milli;
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
        setContentView(R.layout.activity_stop_attendance);

        sharedPreferences = getSharedPreferences(String.valueOf(R.string.pref_file_key),MODE_PRIVATE);
        userid = sharedPreferences.getString("userid","");
        user = sharedPreferences.getString("user","");
        result = sharedPreferences.getString("result","");
        milli = sharedPreferences.getLong("milli", Long.parseLong("0"));

        sharedPreferences1 = getSharedPreferences("Preferences",MODE_PRIVATE);
        cookie = sharedPreferences1.getString("Cookie","");
        uid = sharedPreferences1.getString("uid","");

        requestQueue = Volley.newRequestQueue(StopAttendance.this);

        TextView name = (TextView) findViewById(R.id.name);
        name.setText(user);

        m = System.currentTimeMillis();
        time2 = time(m);
        time1 = time(milli);

        checkout = (Button) this.findViewById(R.id.checkout);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postData(requestQueue);
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putBoolean("checkedout",true);
                editor.apply();
                Toast.makeText(getApplicationContext(), "Attendance logged.", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(StopAttendance.this,MainActivity.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            }
        });
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(StopAttendance.this)
                .setTitle("Checked in")
                .setMessage("This will end the app. Use the home button instead.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }
    public String time(long milliseconds) {
        Date currentDate = new Date(milliseconds);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.format(currentDate);
    }
    public void postData(RequestQueue requestQueue) {
        String obj = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"call\",\n" +
                "    \"params\": {\n" +
                "        \"args\": [\n" +
                "            [" + result + "],\n" +
                "            {\n" +
                "                \"check_out\": \""+ time2 +"\"}\n" +
                "        ],\n" +
                "        \"model\": \"hr.attendance\",\n" +
                "        \"method\": \"write\",\n" +
                "        \"kwargs\": {\n" +
                "            \"context\": {\n" +
                "                \"lang\": \"en_US\",\n" +
                "                \"tz\": \"Asia/Kolkata\",\n" +
                "                \"uid\": "+ uid +",\n" +
                "                \"params\": {\n" +
                "                        \"action\": 433,\n" +
                "                                \"model\": \"hr.attendance\",\n" +
                "                                \"view_type\": \"list\",\n" +
                "                                \"menu_id\": 292\n" +
                "                    }," +
                "                \"search_default_today\": 1\n" +
                "            }\n" +
                "        }\n" +
                "    },\n" +
                "    \"id\": 415817139\n" +
                "}";

        JSONObject object = null;
        try {
            object = new JSONObject(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String stopurl = "http://34.87.169.30/web/dataset/call_kw/hr.attendance/write";
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, stopurl, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
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