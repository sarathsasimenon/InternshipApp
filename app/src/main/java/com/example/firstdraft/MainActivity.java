package com.example.firstdraft;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
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
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_TEXT = "com.example.firstdraft.EXTRA_TEXT";
    public static final String EXTRA_TEXT2 = "com.example.firstdraft.EXTRA_TEXT2";
    public static final String EXTRA_TEXT3 = "com.example.firstdraft.EXTRA_TEXT3";

    String id;
    String user;
    String project;

    RequestQueue requestQueue;
    RequestQueue requestQueue1;

    ArrayList<String> al = new ArrayList<>();

    private Spinner dropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        postData(requestQueue);

        /*System.out.println(user);*/

        /*TextView name = (TextView) findViewById(R.id.name);
        name.setText("Administrator");*/

        dropdown = (Spinner) findViewById(R.id.dropdown);

        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.clientlist));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);*/

        requestQueue1 = Volley.newRequestQueue(MainActivity.this);

        al.add("Choose a client and respective order id");
        postData1(requestQueue1);

        /*final String[] al = getResources().getStringArray(R.array.clientlist);*/

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < al.size(); i++) {
                    if (parent.getItemAtPosition(position).equals(al.get(0))) {
                        // do nothing
                    } else {
                        String s = parent.getItemAtPosition(position).toString();
                        if (s.equals("Head Office")) {
                            Intent intent1 = new Intent(MainActivity.this, back_home.class);
                            startActivity(intent1);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                        else {
                            Intent intent = new Intent(MainActivity.this, item_select.class);
                            intent.putExtra(EXTRA_TEXT, s);
                            /*intent.putExtra(EXTRA_TEXT2, address);*/
                            intent.putExtra(EXTRA_TEXT3, id);
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
                "                    2\n" +
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
        /*System.out.println(object);*/
        String nameurl = "http://34.87.62.211/web/dataset/call_kw/hr.employee/search_read";
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, nameurl, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                try {
                    JSONArray arr = response.getJSONArray("result");
                    id = arr.getJSONObject(0).getString("id");
                    user = arr.getJSONObject(0).getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*System.out.println(id);
                System.out.println(user);*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Cookie","session_id=5532515de4dba12c8836e4842814f9459f8df9dc");
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        List<String> cookies = new ArrayList<>();
        cookies.add("session_id=5532515de4dba12c8836e4842814f9459f8df9dc");
        customRequest.setCookies(cookies);
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
        /*System.out.println(object);*/
        String projecturl = "http://34.87.62.211/web/dataset/search_read";
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, projecturl, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                /*System.out.println("test1");
                System.out.println(response);*/
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
                /*System.out.println(al);*/
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Cookie","session_id=5532515de4dba12c8836e4842814f9459f8df9dc");
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        List<String> cookies = new ArrayList<>();
        cookies.add("session_id=5532515de4dba12c8836e4842814f9459f8df9dc");
        customRequest.setCookies(cookies);
        requestQueue.add(customRequest);
    }
}