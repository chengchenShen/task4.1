package com.example.task41;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Chronometer chronometer;
    TextView text_result;
    boolean running, changed,started,paused,
            going = false;
    ImageView stop,start,pause;
    long stop_point;
    String min,input ;
    EditText work_out;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chronometer = findViewById(R.id.chronometer);
        text_result = findViewById(R.id.text_result);
        work_out = findViewById(R.id.work_out);

        upData();

        if(savedInstanceState!=null){
            String time_result = savedInstanceState.getString("result");
            boolean st = savedInstanceState.getBoolean("std");
            boolean chg = savedInstanceState.getBoolean("changed");
            boolean pa = savedInstanceState.getBoolean("pad");
            long t = savedInstanceState.getLong("times");
            long keep_running = savedInstanceState.getLong("time");
            text_result.setText(time_result);

            if(st){
                chronometer.setBase(keep_running);
                chronometer.start();
                running =true;
                going = true;
                started = true;
                changed =false;
            }else if(chg){
                chronometer.stop();
                running = false;
                changed = false;
                started = false;
                paused = true;
            }
            else if(pa){
                chronometer.stop();
                chronometer.setBase(SystemClock.elapsedRealtime() - t);
                running = false;
                changed =false;
                started =false;

            }
        }
    }

    public void upData() {
        SharedPreferences sp = getSharedPreferences("MyDate", Context.MODE_PRIVATE);
        String spent = sp.getString("min","");
        String wk = sp.getString("input","");
        text_result.setText("You spent " + spent + " on " + wk + " last time.");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("result",text_result.getText().toString());
        outState.putLong("time", chronometer.getBase());
        outState.putBoolean("std",started);
        outState.putBoolean("changed",changed);
        outState.putBoolean("pad",paused);
        outState.putLong("times",stop_point);



    }

    public void startChronometer(View v){
        if(work_out.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter your workout type first!", Toast.LENGTH_SHORT).show();

        }else{
            if(!running){
                if(!going){
                    chronometer.setBase(SystemClock.elapsedRealtime()- stop_point);
                }
                chronometer.setBase(SystemClock.elapsedRealtime() - stop_point);
                chronometer.start();
                running =true;
                going = true;
                started = true;
                changed =false;
            }
        }
    }



    public void pauseChronometer(View v){
        if(running){
            chronometer.stop();
            stop_point = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
            changed = false;
            started = false;
            paused = true;
        }
    }

    public void stopChronometer(View v){
        min = chronometer.getText().toString();
        input = work_out.getText().toString();
        chronometer.setBase(SystemClock.elapsedRealtime());
        text_result.setText("You spent " + min + " on " + input + " last time.");

        sp = getSharedPreferences("MyDate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("min" , min);
        editor.putString("input" , input);
        editor.apply();

        stop_point = 0;
        chronometer.stop();
        going = false;
        running = false;
        changed =false;
        started =false;
    }




}






