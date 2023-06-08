package com.example.winning_calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ScheduleSub extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_sub);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Button btn2 = (Button) findViewById(R.id.button8);
        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void onBackPressed() {

        super.onBackPressed();

    }
}
