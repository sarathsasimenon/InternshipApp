package com.example.firstdraft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class back_home_stop extends AppCompatActivity {
    private Button btnEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_home_stop);

        final Date date = new Date();
        final Timestamp timestamp1 = new Timestamp(date.getTime());

        btnEnd = (Button) this.findViewById(R.id.btnEnd);
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timestamp timestamp2 = new Timestamp(date.getTime());
                long m = timestamp2.getTime() - timestamp1.getTime();

                DateFormat simple = new SimpleDateFormat("HH:mm:ss:SSS Z");
                Date result = new Date(m);
                String duration = simple.format(result);

                final Intent intent1 = new Intent(back_home_stop.this, MainActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            }
        });
    }
}