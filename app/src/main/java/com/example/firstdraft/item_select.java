package com.example.firstdraft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Timestamp;
import java.util.Date;

public class item_select extends AppCompatActivity {
    public static final String EXTRA_TEXT = "com.example.firstdraft.EXTRA_TEXT";
    public static final String EXTRA_TEXT2 = "com.example.firstdraft.EXTRA_TEXT2";
    public static final String EXTRA_TEXT3 = "com.example.firstdraft.EXTRA_TEXT3";

    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_select);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final long m = t();

        final Intent intent = getIntent();
        final String client = intent.getStringExtra(MainActivity.EXTRA_TEXT);
        final String oid = intent.getStringExtra(MainActivity.EXTRA_TEXT2);
        final TextView cl = (TextView) findViewById(R.id.cl);
        TextView order = (TextView) findViewById(R.id.order);

        cl.setText(client);
        order.setText(oid);

        btnStart = (Button) this.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent1 = new Intent(item_select.this, stop_journey.class);
                intent1.putExtra(EXTRA_TEXT, client);
                intent1.putExtra(EXTRA_TEXT2, oid);
                intent1.putExtra(EXTRA_TEXT3, m);
                startActivity(intent1);
                overridePendingTransition(0,0);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            }
        });
    }
    public long t(){
        Date date = new Date();
        Timestamp timestamp1 = new Timestamp(date.getTime());
        long milli = timestamp1.getTime();
        return milli;
    }
}