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

    private Spinner dropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        postData(requestQueue);

        TextView name = (TextView) findViewById(R.id.name);
        name.setText(user);

        dropdown = (Spinner) findViewById(R.id.dropdown);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.clientlist));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);

        final String[] al = getResources().getStringArray(R.array.clientlist);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals(al[0])) {
                    //do nothing
                } else {
                    String s = parent.getItemAtPosition(position).toString();
                    if (s.equals("Back Home")) {
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
        String url = getResources().getString(R.string.nameurl);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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
}