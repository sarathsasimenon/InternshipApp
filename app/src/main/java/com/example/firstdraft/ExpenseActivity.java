package com.example.firstdraft;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExpenseActivity extends AppCompatActivity {
    public static final String EXTRA_TEXT = "com.example.firstdraft.EXTRA_TEXT";
    public static final String EXTRA_TEXT2 = "com.example.firstdraft.EXTRA_TEXT2";
    public static final String EXTRA_TEXT3 = "com.example.firstdraft.EXTRA_TEXT3";
    public static final String EXTRA_TEXT4 = "com.example.firstdraft.EXTRA_TEXT4";
    public static final String EXTRA_TEXT5 = "com.example.firstdraft.EXTRA_TEXT5";

    String baseurl;
    String cookie;
    String uid;

    String userid;
    String user;
    String projectid;
    String pid;
    String project;
    String client;
    String date;

    TextView name;

    String mealText;
    String hotelText;
    String fareText;
    String carText;
    String miscText;
    double total;
    String miscdetText;

    RequestQueue requestQueue;
    RequestQueue requestQueue1;

    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences1;

    private Spinner dropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        sharedPreferences1 = getSharedPreferences(String.valueOf(R.string.pref_file_key),MODE_PRIVATE);
        client = sharedPreferences1.getString("Client","");
        userid = sharedPreferences1.getString("userid","");
        user = sharedPreferences1.getString("user","");

        sharedPreferences = getSharedPreferences("Preferences",MODE_PRIVATE);
        baseurl = sharedPreferences.getString("baseurl","");
        cookie = sharedPreferences.getString("Cookie","");
        uid = sharedPreferences.getString("uid","");
        user = sharedPreferences.getString("user","");

        /*final Intent intent = getIntent();
        user = intent.getStringExtra(FirstActivity.EXTRA_TEXT);
        userid = intent.getStringExtra(FirstActivity.EXTRA_TEXT2);*/

        TextView name = (TextView) findViewById(R.id.name);
        TextView cl = (TextView) findViewById(R.id.cl);
        name.setText(user);
        cl.setText(client);

        final EditText meals = (EditText) findViewById(R.id.meals);
        final EditText hotels = (EditText) findViewById(R.id.hotel);
        final EditText fares = (EditText) findViewById(R.id.fares);
        final EditText cars = (EditText) findViewById(R.id.cars);
        final EditText misc = (EditText) findViewById(R.id.misc);
        final EditText miscdetails = (EditText) findViewById(R.id.miscdetails);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date(System.currentTimeMillis());
        date = formatter.format(d);

        requestQueue1 = Volley.newRequestQueue(ExpenseActivity.this);

        Button btnExp = (Button) this.findViewById(R.id.btnExp);

        btnExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mealText = meals.getText().toString();
                hotelText = hotels.getText().toString();
                fareText = fares.getText().toString();
                carText = cars.getText().toString();
                miscText = misc.getText().toString();
                miscdetText = miscdetails.getText().toString();
                if(mealText.isEmpty() || hotelText.isEmpty() || fareText.isEmpty() || carText.isEmpty() || miscText.isEmpty() || miscdetText.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Add values in each of the fields.", Toast.LENGTH_SHORT).show();
                }
                else{
                    postData1(requestQueue1);
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(ExpenseActivity.this)
                .setTitle("Expenses haven't been recorded.")
                .setMessage("This will end the app. Use the home button instead.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }
    public void postData1(RequestQueue requestQueue) {
        total = Double.parseDouble(mealText) + Double.parseDouble(hotelText) + Double.parseDouble(fareText) + Double.parseDouble(carText) + Double.parseDouble(miscText);
        String obj = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"call\",\n" +
                "    \"params\": {\n" +
                "        \"args\": [\n" +
                "            {\n" +
                "                \"quantity\": 1,\n" +
                "                \"product_uom_id\": 21,\n" +
                "                \"date\":\"" + date +"\",\n"+
                "                \"account_id\": 47,\n" +
                "                \"employee_id\": "+ userid +",\n" +
                "                \"currency_id\": 20,\n" +
                "                \"company_id\": 1,\n" +
                "                \"payment_mode\": \"own_account\",\n" +
                "                \"name\": \"Admin expences\",\n" +
                "                \"product_id\": 3262,\n" +
                "                \"unit_amount\": 100,\n" +
                "                \"tax_ids\": [\n" +
                "                    [\n" +
                "                        6,\n" +
                "                        false,\n" +
                "                        []\n" +
                "                    ]\n" +
                "                ],\n" +
                "                \"reference\": false,\n" +
                "                \"sale_order_id\": false,\n" +
                "                \"analytic_account_id\": false,\n" +
                "                \"analytic_tag_ids\": [\n" +
                "                    [\n" +
                "                        6,\n" +
                "                        false,\n" +
                "                        []\n" +
                "                    ]\n" +
                "                ],\n" +
                "                \"expense_meals\": "+ mealText +",\n" +
                "                \"expense_hotel\": "+ hotelText +",\n" +
                "                \"expense_fares\": "+ fareText +",\n" +
                "                \"expense_car\": "+ carText +",\n" +
                "                \"expense_misc\": "+ miscText +",\n" +
                "                \"expense_total\": "+ total +",\n" +
                "                \"expense_misc_details\": \""+ miscdetText +"\",\n" +
                "                \"description\": false,\n" +
                "                \"message_attachment_count\": 0\n" +
                "            }\n" +
                "        ],\n" +
                "        \"model\": \"hr.expense\",\n" +
                "        \"method\": \"create\",\n" +
                "        \"kwargs\": {\n" +
                "            \"context\": {\n" +
                "                \"lang\": \"en_US\",\n" +
                "                \"tz\": \"Asia/Kolkata\",\n" +
                "                \"uid\": "+ uid +",\n" +
                "                \"search_default_my_expenses\": 1,\n" +
                "                \"search_default_no_report\": 1\n" +
                "            }\n" +
                "        }\n" +
                "    },\n" +
                "    \"id\": 755991171\n" +
                "}  ";
        JSONObject object = null;
        try {
            object = new JSONObject(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(object);
        String projecturl = baseurl+"web/dataset/call_kw/hr.expense/create";
        System.out.println(projecturl);
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, projecturl, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("exptracker",true);
                editor.apply();
                Toast.makeText(getApplicationContext(), "Expenses recorded.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ExpenseActivity.this,FirstActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
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