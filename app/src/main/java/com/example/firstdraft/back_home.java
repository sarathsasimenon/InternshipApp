package com.example.firstdraft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

public class back_home extends AppCompatActivity {
    public static final String EXTRA_TEXT3 = "com.example.firstdraft.EXTRA_TEXT3";
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_home);

        final String flag = "flag";

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnStart = (Button) this.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent1 = new Intent(back_home.this, back_home_stop.class);
                startActivity(intent1);
                intent1.putExtra(EXTRA_TEXT3, flag);
                overridePendingTransition(0,0);
            }
        });
    }
}