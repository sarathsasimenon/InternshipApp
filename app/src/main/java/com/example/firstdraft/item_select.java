package com.example.firstdraft;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class item_select extends AppCompatActivity {
    public static final String EXTRA_TEXT = "com.example.firstdraft.EXTRA_TEXT";
    public static final String EXTRA_TEXT2 = "com.example.firstdraft.EXTRA_TEXT2";
    public static final String EXTRA_TEXT3 = "com.example.firstdraft.EXTRA_TEXT3";
    public static final String EXTRA_TEXT4 = "com.example.firstdraft.EXTRA_TEXT4";
    public static final String EXTRA_TEXT5 = "com.example.firstdraft.EXTRA_TEXT5";
    public static final String EXTRA_TEXT6 = "com.example.firstdraft.EXTRA_TEXT6";

    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();

    RequestQueue requestQueue;

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;

    private Button btnStart;

    String time1;

    double longitude;
    double latitude;

    String id;
    String lat;
    String longi;
    int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_select);

        requestQueue = Volley.newRequestQueue(item_select.this);

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Date date = new Date();
        Timestamp timestamp1 = new Timestamp(date.getTime());
        final long m = timestamp1.getTime();
        time1 = time(m);
        System.out.println(time1);

        final Intent intent = getIntent();
        final String client = intent.getStringExtra(MainActivity.EXTRA_TEXT);
        final String oid = intent.getStringExtra(MainActivity.EXTRA_TEXT2);
        id = intent.getStringExtra(MainActivity.EXTRA_TEXT3);

        TextView cl = (TextView) findViewById(R.id.cl);
        final TextView order = (TextView) findViewById(R.id.order);

        cl.setText(client);
        order.setText(oid);

        btnStart = (Button) this.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationTrack = new LocationTrack(item_select.this);
                if (locationTrack.canGetLocation()) {
                    longitude = locationTrack.getLongitude();
                    latitude = locationTrack.getLatitude();
                    lat = Double.toString(latitude);
                    longi = Double.toString(longitude);
                    /*System.out.println(longitude);
                    System.out.println(latitude);*/
                }
                else {
                    locationTrack.showSettingsAlert();
                }
                postData(requestQueue);

                final Intent intent1 = new Intent(item_select.this, stop_journey.class);
                intent1.putExtra(EXTRA_TEXT, client);
                intent1.putExtra(EXTRA_TEXT2, oid);
                intent1.putExtra(EXTRA_TEXT3, m);
                intent1.putExtra(EXTRA_TEXT4,latitude);
                intent1.putExtra(EXTRA_TEXT5,longitude);
                intent1.putExtra(EXTRA_TEXT6,result);
                startActivity(intent1);
                overridePendingTransition(0, 0);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            }
        });
    }
    public String time(long milliseconds) {
        Date currentDate = new Date(milliseconds);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(currentDate);
    }
    public void postData(RequestQueue requestQueue) {
        JSONObject object = null;
        try {
            object = jsonCreate();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = getResources().getString(R.string.url3);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        /*locationTrack = new LocationTrack(item_select.this);
                        System.out.println(response);
                        if (locationTrack.canGetLocation()) {
                            longitude = locationTrack.getLongitude();
                            latitude = locationTrack.getLatitude();
                            *//*System.out.println(latitude);
                            System.out.println(longitude);*//*
                        }
                        else{
                            locationTrack.showSettingsAlert();
                        }*/
                        System.out.println(response);
                        try {
                            result = (int) response.get("result");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
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

    public JSONObject jsonCreate() throws JSONException {
        JSONObject objc = new JSONObject();
        objc.put("id",15);
        objc.put("action",179);
        objc.put("model","hr.attendance");
        objc.put("view_type","form");
        objc.put("menu_id",141);
        JSONObject jsobj = new JSONObject();
        jsobj.put("lang","en_US");
        jsobj.put("tz","Asia/Kolkata");
        jsobj.put("uid",2);
        jsobj.put("params",objc);
        jsobj.put("search_default_today",1);
        JSONObject jso = new JSONObject();
        jso.put("context",jsobj);
        JSONObject jo = new JSONObject();
        jo.put("employee_id",id);
        jo.put("check_in",time1);
        jo.put("check_out",false);
        jo.put("x_check_in_lat",lat);
        jo.put("x_check_in_long",longi);
        jo.put("x_check_out_lat",false);
        jo.put("x_check_out_long",false);
        jo.put("x_distance",0);
        JSONArray arr = new JSONArray();
        arr.put(jo);
        JSONObject obj = new JSONObject();
        obj.put("args",arr);
        obj.put("model","hr.attendance");
        obj.put("method","create");
        obj.put("kwargs",jso);
        JSONObject ob = new JSONObject();
        ob.put("jsonrpc","2.0");
        ob.put("method","call");
        ob.put("params",obj);
        ob.put("id",753438469);
        System.out.println(ob);
        return ob;
    }


    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();
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
    protected void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
    }
}
/*
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng latlng = new LatLng(d1,d2);
        map.addMarker(new MarkerOptions().position(latlng).title("destination"));
        map.moveCamera(CameraUpdateFactory.newLatLng(latlng));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }*/


