package com.example.firstdraft;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class item_select extends AppCompatActivity {
    public static final String EXTRA_TEXT3 = "com.example.firstdraft.EXTRA_TEXT3";
    public static final String EXTRA_TEXT4 = "com.example.firstdraft.EXTRA_TEXT4";
    public static final String EXTRA_TEXT5 = "com.example.firstdraft.EXTRA_TEXT5";
    public static final String EXTRA_TEXT6 = "com.example.firstdraft.EXTRA_TEXT6";
    public static final String EXTRA_TEXT7 = "com.example.firstdraft.EXTRA_TEXT7";
    public static final String EXTRA_TEXT8 = "com.example.firstdraft.EXTRA_TEXT8";

    private ArrayList<Object> permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList<String> permissions = new ArrayList<>();

    RequestQueue requestQueue;

    String uid;
    String baseurl;
    String cookie;

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;

    private Button btnStart;

    String time1;

    double longitude;
    double latitude;

    String lat;
    String longi;
    String result;

    String userid;
    String client;
    String projectid;
    String add;
    long m;
    String user;

    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_select);

        sharedPreferences = getSharedPreferences("Preferences",MODE_PRIVATE);
        sharedPreferences1 = getSharedPreferences(String.valueOf(R.string.pref_file_key),MODE_PRIVATE);
        baseurl = sharedPreferences.getString("baseurl","");
        cookie = sharedPreferences.getString("Cookie","");
        uid = sharedPreferences.getString("uid","");

        requestQueue = Volley.newRequestQueue(item_select.this);

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*Date date = new Date();
        Timestamp timestamp1 = new Timestamp(date.getTime());
        m = timestamp1.getTime();*/
        m = System.currentTimeMillis();
        time1 = time(m);

        final Intent intent = getIntent();
        client = intent.getStringExtra(MainActivity.EXTRA_TEXT);
        projectid = intent.getStringExtra(MainActivity.EXTRA_TEXT4);
        add = "Velachery, Chennai";
        userid = intent.getStringExtra(MainActivity.EXTRA_TEXT3);
        user = intent.getStringExtra(MainActivity.EXTRA_TEXT5);

        TextView name = (TextView) findViewById(R.id.name);
        TextView cl = (TextView) findViewById(R.id.cl);
        final TextView address = (TextView) findViewById(R.id.address);

        name.setText(user);
        cl.setText(client);
        address.setText(add);

        btnStart = (Button) this.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("journeyover",false);
                editor.putBoolean("journeyhomeover",true);
                editor.commit();
                locationTrack = new LocationTrack(item_select.this);
                if (!locationTrack.canGetLocation()) {
                    locationTrack.showSettingsAlert();
                }
                if (locationTrack.canGetLocation()) {
                    getLocation();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(item_select.this,MainActivity.class);
        startActivity(intent);
    }
    public void getLocation(){
        longitude = locationTrack.getLongitude();
        latitude = locationTrack.getLatitude();
        lat = Double.toString(latitude);
        longi = Double.toString(longitude);
        postData(requestQueue);
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
                "            {\n" +
                "                \"employee_id\":" + userid + ",\n" +
                "                \"check_in\": \""+ time1 +"\",\n" +
                "                \"check_out\": false,\n" +
                "                \"hr_project_id\": \""+ projectid +"\",\n" +
                "                \"gps_lat_check_in\": \""+lat+"\",\n" +
                "                \"gps_lat_check_out\": false,\n" +
                "                \"gps_lang_check_in\": \""+longi+"\",\n" +
                "                \"gps_lang_check_out\": false,\n" +
                "                \n" +
                "                \"dist_check_in\": false\n" +
                "                \n" +
                "            }\n" +
                "        ],\n" +
                "        \"model\": \"hr.attendance\",\n" +
                "        \"method\": \"create\",\n" +
                "        \"kwargs\": {\n" +
                "            \"context\": {\n" +
                "                \"lang\": \"en_US\",\n" +
                "                \"tz\": \"Asia/Kolkata\",\n" +
                "                \"uid\": "+ uid +",\n" +
                "                \"search_default_today\": 1\n" +
                "            }\n" +
                "        }\n" +
                "    },\n" +
                "    \"id\": 34669893\n" +
                "}";

        JSONObject object = null;
        try {
            object = new JSONObject(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        System.out.println(object);
        String starturl = baseurl+"/web/dataset/call_kw/hr.attendance/create";
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, starturl, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                /*System.out.println(response);*/
                try {
                    result = response.getString("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putString("Client", client);
                editor.putLong("milli",m);
                editor.putString("address",add);
                editor.putLong("latitude",Double.doubleToRawLongBits(latitude));
                editor.putLong("longitude",Double.doubleToRawLongBits(longitude));
                editor.putString("userid",userid);
                editor.putString("user",user);
                editor.putString("result",result);
                editor.apply();
                final Intent intent1 = new Intent(item_select.this, stop_journey.class);
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
                HashMap<String, String> headers = new HashMap<>();
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

    private ArrayList<Object> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<Object> result = new ArrayList<>();
        for (Object perm : wanted) {
            if (!hasPermission((String) perm)) {
                result.add(perm);
            }
        }
        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (Object perms : permissionsToRequest) {
                    if (!hasPermission((String) perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(item_select.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
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

