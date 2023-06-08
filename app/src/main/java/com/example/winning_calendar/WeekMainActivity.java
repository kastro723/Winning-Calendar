package com.example.winning_calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.Date;

public class WeekMainActivity extends AppCompatActivity {

    private Button button;
    private ImageButton saveBtn;
    private ImageButton cslBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        button = findViewById(R.id.weekgobutton);

        saveBtn = findViewById(R.id.saveButton);

        cslBtn = findViewById(R.id.cansleButton);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼 클릭 시 event_cell 화면으로 이동
                Intent intent = new Intent(getApplicationContext(), WeekActivity.class);
                startActivity(intent);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼 클릭 시 event_cell 화면으로 이동
                Intent intent = new Intent(getApplicationContext(), WeekActivity.class);
                startActivity(intent);
            }
        });

        cslBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //버튼 클릭시 MainActivity로 이동
                onBackPressed();
            }
        });
    }


    public void onBackPressed() {

        super.onBackPressed();

    }
}