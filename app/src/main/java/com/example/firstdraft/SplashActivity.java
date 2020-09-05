package com.example.firstdraft;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {
    Handler handler;
    SharedPreferences sharedPreferences;
    int flag1;
    int flag;
    boolean loggedin;
    boolean journeyover;
    boolean journeyhomeover;
    boolean checkedout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = new Handler();
        sharedPreferences = getSharedPreferences("Preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        flag1 = sharedPreferences.getInt("flag",100);
        if (flag1==100){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent2 = new Intent(SplashActivity.this, LoginActivity.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent2);
                    finish();
                }
            },2000);
        }
        else{
            loggedin = sharedPreferences.getBoolean("loggedin", false);
            journeyover = sharedPreferences.getBoolean("journeyover",true);
            journeyhomeover = sharedPreferences.getBoolean("journeyhomeover",true);
            checkedout = sharedPreferences.getBoolean("checkedout",true);

            System.out.println("Flag:"+flag1);
            System.out.println("Login:"+loggedin);
            System.out.println("Journey:"+journeyover);
            System.out.println("Journey Home:"+journeyhomeover);
            System.out.println("Checked Out:"+checkedout);

            if (journeyover && loggedin && journeyhomeover && checkedout) {
                flag = 1;
            }
            if (!journeyover && loggedin && journeyhomeover && checkedout) {
                flag = 2;
            }
            if(journeyover && !loggedin && journeyhomeover && checkedout){
                flag = 3;
            }
            if (journeyover && loggedin && !journeyhomeover & checkedout) {
                flag = 4;
            }
            if (journeyover && loggedin && journeyhomeover & !checkedout) {
                flag = 5;
            }
            if (flag == 1){
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent1 = new Intent(SplashActivity.this, FirstActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        finish();
                    }
                },2000);
            }
            if (flag==2){
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, stop_journey.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                },2000);
            }
            if (flag==3){
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent2 = new Intent(SplashActivity.this, LoginActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent2);
                        finish();
                    }
                },2000);
            }
            if (flag==4){
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, back_home_stop.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                },2000);
            }
            if (flag==5){
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, StopAttendance.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                },2000);
            }
        }
    }
}