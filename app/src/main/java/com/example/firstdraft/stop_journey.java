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

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class stop_journey extends AppCompatActivity {
    private ArrayList<Object> permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList<String> permissions = new ArrayList<>();

    RequestQueue requestQueue;
    /*RequestQueue requestQueue2;*/

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;

    String cookie;

    String userid;
    String user;
    String client;
    String add;
    /*String oid;*/
    String time1;
    String time2;

    double longi2;
    double lat2;

    double dist;
    String checkoutlat;
    String checkoutlong;
    String id;

    TextView name;
    TextView cl;
    TextView address;
    /*TextView order;*/

    long m;
    long milli;
    double lat1;
    double longi1;

    String result;

    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_journey);

        sharedPreferences = getSharedPreferences(String.valueOf(R.string.pref_file_key),MODE_PRIVATE);
        client = sharedPreferences.getString("Client","");
        add = sharedPreferences.getString("address","");
        milli = sharedPreferences.getLong("milli", Long.parseLong("0"));
        lat1 = Double.longBitsToDouble(sharedPreferences.getLong("latitude", Double.doubleToLongBits(0)));
        longi1 = Double.longBitsToDouble(sharedPreferences.getLong("longitude", Double.doubleToLongBits(0)));
        userid = sharedPreferences.getString("userid","");
        user = sharedPreferences.getString("user","");
        result = sharedPreferences.getString("result","");

        sharedPreferences1 = getSharedPreferences("Preferences",MODE_PRIVATE);
        cookie = sharedPreferences1.getString("Cookie","");
        id = sharedPreferences1.getString("uid","");

        name = (TextView) findViewById(R.id.name);
        cl = (TextView) findViewById(R.id.cl);
        address = (TextView) findViewById(R.id.address);

        name.setText(user);
        cl.setText(client);
        address.setText(add);

        requestQueue = Volley.newRequestQueue(stop_journey.this);
       /* requestQueue2 = Volley.newRequestQueue(stop_journey.this);*/

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
        m = System.currentTimeMillis();
        time2 = time(m);
        time1 = time(milli);

        final Button btnEnd = (Button) this.findViewById(R.id.btnEnd);

        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationTrack = new LocationTrack(stop_journey.this);
                if (locationTrack.canGetLocation()) {
                    longi2 = locationTrack.getLongitude();
                    lat2 = locationTrack.getLatitude();
                    checkoutlat = Double.toString(lat2);
                    checkoutlong = Double.toString(longi2);
                }
                else {
                    locationTrack.showSettingsAlert();
                }
                distance(lat1,longi1,lat2,longi2);

                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putBoolean("journeyover",true);
                editor.apply();
                Toast.makeText(getApplicationContext(), "Journey recorded.", Toast.LENGTH_SHORT).show();
                final Intent intent1 = new Intent(stop_journey.this, FirstActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            }
        });
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(stop_journey.this)
                .setTitle("Journey still in progress")
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
    public static String timer(long milli){
        final Date date = new Date();
        Timestamp timestamp2 = new Timestamp(date.getTime());
        long m = timestamp2.getTime() - milli;
        int s = (int) (m / 1000) % 60 ;
        int min = (int) ((m / (1000*60)) % 60);
        int h   = (int) ((m / (1000*60*60)) % 24);
        return String.format("%02d:%02d:%02d", h,min,s);
    }
    public void distance(double lat1,double long1,double lat2,double long2){
        // Google Maps Distance Matrix API

        /*
        String apiKey = "AIzaSyBVFCmDp3hmNldDpCXtPc6LtS2c0U3rs2o";
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="+lat1+","+long1+"&destinations="+lat2+","+long2+"&key="+apiKey;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        dist = (int) response.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").get("value");
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        requestQueue2.add(jsonObjectRequest);*/

        // Open Route Service API
        final String obj = "{\"locations\":[[" + long1 + "," + lat1 +"],[" + long2+ "," + lat2 + "]],\"metrics\":[\"distance\"],\"units\":\"km\"}";
        JSONObject object = null;
        try {
            object = new JSONObject(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "https://api.openrouteservice.org/v2/matrix/driving-car";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                if (response != null) {
                    try {
                        JSONArray arr = response.getJSONArray("distances");
                        JSONArray arr2 = arr.getJSONArray(0);
                        dist = arr2.getDouble(1);
                        postData(requestQueue);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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
                headers.put("Authorization", "5b3ce3597851110001cf6248f06f715ad4394da8bd2d16448e3e53cb");
                headers.put("Accept", "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8");
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    public void postData(RequestQueue requestQueue) {
        String obj = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"call\",\n" +
                "    \"params\": {\n" +
                "        \"args\": [\n" +
                "            [" + result + "],\n" +
                "            {\n" +
                "                \"employee_id\":" + userid + ",\n" +
                "                \"check_out\": \""+ time2 +"\",\n" +
                "                \"gps_lat_check_out\": \""+checkoutlat+"\",\n" +
                "                \"gps_lang_check_out\": \""+checkoutlong+"\",\"dist_check_in\":" + dist + " }\n" +
                "        ],\n" +
                "        \"model\": \"hr.attendance\",\n" +
                "        \"method\": \"write\",\n" +
                "        \"kwargs\": {\n" +
                "            \"context\": {\n" +
                "                \"lang\": \"en_US\",\n" +
                "                \"tz\": \"Asia/Kolkata\",\n" +
                "                \"uid\": "+ id +",\n" +
                "                \"search_default_today\": 1\n" +
                "            }\n" +
                "        }\n" +
                "    },\n" +
                "    \"id\": 578731422\n" +
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
            public Map<String, String> getHeaders(){
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
        new AlertDialog.Builder(stop_journey.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    @Override
    protected void onPause(){
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

