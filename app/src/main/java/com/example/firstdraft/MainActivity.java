package com.example.firstdraft;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_TEXT = "com.example.firstdraft.EXTRA_TEXT";
    public static final String EXTRA_TEXT2 = "com.example.firstdraft.EXTRA_TEXT2";
    public static final String EXTRA_TEXT3 = "com.example.firstdraft.EXTRA_TEXT3";

    String id;
    String user;

    RequestQueue requestQueue;
    /*RequestQueue requestQueue1;
    RequestQueue requestQueue2;
    RequestQueue requestQueue3;*/

    private Spinner dropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        postData(requestQueue);

        /*requestQueue1 = Volley.newRequestQueue(MainActivity.this);
        requestQueue2 = Volley.newRequestQueue(MainActivity.this);

        postData1(requestQueue1);
        postData2(requestQueue2);
*/
        TextView name = (TextView) findViewById(R.id.name);
        name.setText(user);

        dropdown = (Spinner) findViewById(R.id.dropdown);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.clientlist));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);

        /*requestQueue3 = Volley.newRequestQueue(MainActivity.this);

        postData3(requestQueue3);*/

        final String[] al = getResources().getStringArray(R.array.clientlist);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals(al[0])) {
                    //do nothing
                } else {
                    String s = parent.getItemAtPosition(position).toString();
                    if(s.equals("Back Home")) {
                        Intent intent1 = new Intent(MainActivity.this, back_home.class);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else {
                        String[] split = s.split("-", 2);

                        String client = split[0];
                        String orderid = split[1];

                        Intent intent = new Intent(MainActivity.this, item_select.class);
                        intent.putExtra(EXTRA_TEXT, client);
                        intent.putExtra(EXTRA_TEXT2, orderid);
                        intent.putExtra(EXTRA_TEXT3, id);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });
    }
    public void postData(RequestQueue requestQueue) {
        JSONObject object = null;
        try {
            object = jsonCreate();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(object);
        String nameurl = getResources().getString(R.string.nameurl);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, nameurl, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("test1");
                        System.out.println(response);
                        try {
                            JSONArray arr = response.getJSONArray("result");
                            id = arr.getJSONObject(0).getString("id");
                            user = arr.getJSONObject(1).getString("name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("test2");
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
/*

    public void postData1(RequestQueue requestQueue) {
        String url1 = getResources().getString(R.string.loginurl);
        JSONObject object = new JSONObject();
        JSONObject objc = new JSONObject();
        try {
            object.put("db", "bitnami_odoo");
            object.put("login","user@example.com");
            object.put("password", "user@123");
            objc.put("params", object);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url1, objc,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        JSONArray key = response.names();
                        String k = null;
                        try {
                            k = key.getString(2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(k.equals("result")) {
                            System.out.println("test1");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("test2");
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
    public void postData2(RequestQueue requestQueue) {
        JSONObject object = null;
        try {
            object = jsonCreate();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = getResources().getString(R.string.nameurl);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("test3");
                        System.out.println(response);
                        try {
                            JSONArray arr = response.getJSONArray("result");
                            id = arr.getJSONObject(0).getString("id");
                            user = arr.getJSONObject(1).getString("name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("test4");
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

    public void postData3(RequestQueue requestQueue) {
        JSONObject object = null;
        try {
            object = jsonCreate2();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        */
/*System.out.println(object);*//*

        String url = getResources().getString(R.string.projecturl);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("test1");
                        System.out.println(response);
                        */
/*try {
                            JSONArray arr = response.getJSONArray("result");
                            id = arr.getJSONObject(0).getString("id");
                            user = arr.getJSONObject(1).getString("name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*//*

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("test2");
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
*/
    public JSONObject jsonCreate() throws JSONException {
        JSONObject jso = new JSONObject();
        JSONArray joarr = new JSONArray();
        joarr.put("user_id");
        joarr.put("=");
        joarr.put(2);
        JSONArray jarr1 =  new JSONArray();
        jarr1.put(joarr);
        JSONArray jarr2 =  new JSONArray();
        jarr2.put("attendance_state");
        jarr2.put("name");
        JSONArray arr = new JSONArray();
        arr.put(jarr1);
        arr.put(jarr2);
        JSONObject obj = new JSONObject();
        obj.put("args",arr);
        obj.put("model","hr.employee");
        obj.put("method","search_read");
        obj.put("kwargs",jso);
        JSONObject ob = new JSONObject();
        ob.put("jsonrpc","2.0");
        ob.put("method","call");
        ob.put("params",obj);
        ob.put("id",49458607);
        System.out.println(ob);
        return ob;
    }
/*
    public JSONObject jsonCreate2() throws JSONException {
        JSONArray arr = new JSONArray();
        JSONArray arr2 = new JSONArray();
        arr2.put("name");
        arr2.put("partner_id");
        arr2.put("color");
        arr2.put("task_count");
        arr2.put("label_tasks");
        arr2.put("alias_id");
        arr2.put("alias_domain");
        arr2.put("is_favorite");
        arr2.put("percentage_satisfaction_project");
        arr2.put("rating_status");
        JSONObject jsobj2 = new JSONObject();
        jsobj2.put("lang","en_US");
        jsobj2.put("tz",false);
        jsobj2.put("uid",2);
        JSONObject jsobj = new JSONObject();
        jsobj.put("model","project.project");
        jsobj.put("domain",arr);
        jsobj.put("fields",arr2);
        jsobj.put("limit",80);
        jsobj.put("sort","");
        jsobj.put("context",jsobj2);
        JSONObject obj = new JSONObject();
        obj.put("jsonrpc","2.0");
        obj.put("method","call");
        obj.put("params",jsobj);
        System.out.println(obj);
        return obj;
    }*/
}