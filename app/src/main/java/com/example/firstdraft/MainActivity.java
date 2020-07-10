package com.example.firstdraft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_TEXT = "com.example.firstdraft.EXTRA_TEXT";
    public static final String EXTRA_TEXT2 = "com.example.firstdraft.EXTRA_TEXT2";

    private Spinner dropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}